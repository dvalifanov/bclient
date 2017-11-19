package ru.atc.bclient.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.atc.bclient.model.entity.Operation;

public interface OperationRepository extends JpaRepository<Operation,Integer> {
    @Query(value = "SELECT MAX(operation_id) FROM postgres.bclient3.fct_operation", nativeQuery = true)
    Integer findMaxId();
}
