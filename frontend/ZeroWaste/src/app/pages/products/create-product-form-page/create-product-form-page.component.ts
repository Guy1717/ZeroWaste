import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { InputComponent } from "../../../components/form/input/input.component";
import { TextareaComponent } from "../../../components/form/textarea/textarea.component";
import { SelectComponent } from "../../../components/form/select/select.component";
import { ButtonComponent } from "../../../components/form/button/button.component";

@Component({
  selector: 'app-create-product-form-page',
  imports: [CommonModule, ReactiveFormsModule, RouterModule, InputComponent, TextareaComponent, SelectComponent, ButtonComponent],
  templateUrl: './create-product-form-page.component.html',
  styleUrl: './create-product-form-page.component.css'
})
export class CreateProductFormPageComponent {
  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);

  public productForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3) , Validators.maxLength(100)]],
    description: ['', [Validators.nullValidator, Validators.minLength(3), Validators.maxLength(255)]],
    brand: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    category: ['', [Validators.required]],
    price: ['', [Validators.required, Validators.min(0.00)]],
    stock: ['', [Validators.required, Validators.min(0)]],
    expiresAt: ['', [Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]],
  });

  getErrorMessage(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.productForm.get(controlName)!);

    return validationErrorMessage
  }

  onClick() {
    console.log('Button clicked')
  }

  onSubmit() {
    console.log(this.productForm.value)
  }
}
