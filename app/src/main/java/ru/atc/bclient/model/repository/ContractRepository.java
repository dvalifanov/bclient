package ru.atc.bclient.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.atc.bclient.model.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract,Integer> {
    Contract findContractByNum(String num);
}
