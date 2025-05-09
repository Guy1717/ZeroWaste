package com.zerowaste.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.broadcast.BroadcastList;

@Repository
public interface BroadcastListsRepository extends JpaRepository<BroadcastList, Long> {
    @NativeQuery("SELECT * FROM broadcast_lists WHERE deleted_at IS NULL")
    List<BroadcastList> findAllNotDeleted();
}
