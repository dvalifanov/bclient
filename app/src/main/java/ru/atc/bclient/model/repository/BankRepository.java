package ru.atc.bclient.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.atc.bclient.model.entity.Bank;

public interface BankRepository extends JpaRepository<Bank,Integer> {
}
