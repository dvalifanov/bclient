package ru.atc.bclient.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.atc.bclient.model.entity.LegalEntity;
import ru.atc.bclient.model.entity.PaymentOrder;
import ru.atc.bclient.model.entity.PaymentOrderStatus;

import java.util.Date;
import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder,Integer> {
    @Query(value="select max(payment_order_id) from postgres.bclient3.fct_payment_order", nativeQuery=true)
    Integer findMaxId();

    @Query(value="select max(payment_order_num) from postgres.bclient3.fct_payment_order", nativeQuery=true)
    Integer findMaxNum();

    List<PaymentOrder> findByDateBetweenAndLegalEntitySenderInOrderByDate(Date dateFrom, Date dateTo, List<LegalEntity> legalEntities);

    List<PaymentOrder> findByDateBetweenAndPaymentOrderStatusOrderByPaymentPriorityCodeAscIdAsc(Date dateBegin, Date dateEnd, PaymentOrderStatus paymentOrderStatus);
}
