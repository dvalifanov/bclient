package ru.atc.bclient.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.atc.bclient.model.entity.AccountStatus;

public interface AccountStatusRepository  extends JpaRepository<AccountStatus,Integer> {
    public AccountStatus findByCode(String code);
}
