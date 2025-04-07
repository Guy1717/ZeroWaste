package com.zerowaste.services.promotions;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.promotions.EditPromotionDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import com.zerowaste.services.promotions.exceptions.InvalidDatePeriodException;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@Service
public class EditPromotionService {

    private final PromotionsRepository promotionsRepository;
    private final ProductsRepository productRepository;

    public EditPromotionService(PromotionsRepository promotionsRepository, ProductsRepository productRepository) {
        this.promotionsRepository = promotionsRepository;
        this.productRepository = productRepository;
    }

    public void execute(Long id, EditPromotionDTO dto)
            throws PromotionNotFoundException, ProductNotFoundException, InvalidDatePeriodException {

        Optional<Promotion> promotionOpt = promotionsRepository.findById(id);
        
        if (!promotionOpt.isPresent() || promotionOpt.get().getDeletedAt() != null)
            throw new PromotionNotFoundException("Promoção não encontrada!");
        
        if (dto.startsAt().isAfter(dto.endsAt()))
            throw new InvalidDatePeriodException("The start date must be before the end date.");
        
        Promotion p = promotionOpt.get();
        
        p.setName(dto.name());
        p.setPercentage(dto.percentage());
        p.setStartsAt(dto.startsAt());
        p.setEndsAt(dto.endsAt());
        p.setUpdatedAt(LocalDate.now());

        // Encontra todos os produtos válidos
        Set<Product> products = new HashSet<Product>(productRepository.findAllById(dto.productIds()));

        // Caso nenhum produto seja válido
        if (products.isEmpty() && !dto.productIds().isEmpty())
            throw new ProductNotFoundException("Nenhum produto válido foi encontrado!" + products.isEmpty());

        p.setProducts(products);

        promotionsRepository.save(p);

        // Atualiza o preço promocional dos produtos
        for (Product product : products) {
            int percentage = p.getPercentage();
            double unitPrice = product.getUnitPrice();

            double promotionPrice = unitPrice - (unitPrice * percentage)/100;
            product.setPromotionPrice(promotionPrice);
            productRepository.save(product);
        }

    }
}
