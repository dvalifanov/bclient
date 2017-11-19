package ru.atc.bclient.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.atc.bclient.model.entity.Account;
import ru.atc.bclient.model.entity.AccountBalance;

import java.math.BigDecimal;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance,Integer>{
    AccountBalance findFirstByAccountOrderByDateDesc (Account account);

    @Query(value = "SELECT MAX(account_balance_id) FROM postgres.bclient3.fct_account_balance", nativeQuery = true)
    Integer findMaxId();
}
