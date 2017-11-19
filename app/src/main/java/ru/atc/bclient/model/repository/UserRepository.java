package ru.atc.bclient.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.atc.bclient.model.entity.User;


public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByLogin(String login);

    @Query(value="select max(user_id) from postgres.bclient3.dim_user", nativeQuery=true)
    Integer findMaxId();

    @Query(value="LOCK TABLE postgres.bclient3.dim_user IN EXCLUSIVE MODE", nativeQuery=true)
    void lockTableInExclusiveMode();
}
