package ru.atc.bclient.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.atc.bclient.model.entity.LegalEntity;
import ru.atc.bclient.model.entity.User;
import ru.atc.bclient.model.entity.UserLegalEntity;
import ru.atc.bclient.model.entity.UserLegalEntityPK;

import java.util.List;

public interface UserLegalEntityRepository extends JpaRepository<UserLegalEntity,UserLegalEntityPK> {
}
