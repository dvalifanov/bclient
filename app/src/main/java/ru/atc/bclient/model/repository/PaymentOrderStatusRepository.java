package ru.atc.bclient.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.atc.bclient.model.entity.PaymentOrderStatus;

public interface PaymentOrderStatusRepository extends JpaRepository<PaymentOrderStatus,Integer> {
    PaymentOrderStatus findByCode(String code);
}
