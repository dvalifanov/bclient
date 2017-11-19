package ru.atc.bclient.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "fct_payment_order", schema = "bclient3", catalog = "postgres")
public class PaymentOrder {
    @Getter @Setter
    @Id
    @Column(name = "payment_order_id", nullable = false)
    private Integer id;

    @Getter @Setter
    @Column(name = "payment_order_num", nullable = false)
    private Integer num;

    @Getter @Setter
    @Column(name = "payment_order_date", nullable = false)
    private Date date;

    @Getter @Setter
    @Column(name = "currency_code", nullable = false, length = 10)
    private String currencyCode;

    @Getter @Setter
    @Column(name = "payment_order_amt", nullable = false, precision = 2)
    private BigDecimal amt;

    @Getter @Setter
    @Column(name = "payment_reason", nullable = true, length = 500)
    private String paymentReason;

    @Getter @Setter
    @Column(name = "payment_priority_code", nullable = true, length = 2)
    private String paymentPriorityCode;

    @Getter @Setter
    @Column(name = "reject_reason", nullable = true, length = 500)
    private String rejectReason;

    //ManyToOne part

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "sender_legal_entity_id", referencedColumnName = "legal_entity_id", nullable = false)
    private LegalEntity legalEntitySender;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "sender_account_id", referencedColumnName = "account_id", nullable = false)
    private Account accountSender;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "recipient_legal_entity_id", referencedColumnName = "legal_entity_id", nullable = false)
    private LegalEntity legalEntityRecipient;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "recipient_account_id", referencedColumnName = "account_id", nullable = false)
    private Account accountRecipient;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id", nullable = false)
    private Contract contract;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "payment_order_status_id", referencedColumnName = "payment_order_status_id", nullable = false)
    private PaymentOrderStatus paymentOrderStatus;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        PaymentOrder that = (PaymentOrder) o;
//
//        if (id != null ? !id.equals(that.id) : that.id != null)
//            return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = id != null ? id.hashCode() : 0;
//        return result;
//    }
}
