package ru.atc.bclient.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "dim_account", schema = "bclient3", catalog = "postgres")
public class Account {

    @Getter @Setter
    @Id
    @Column(name = "account_id", nullable = false)
    private Integer id;

    @Getter @Setter
    @Column(name = "account_name", nullable = false, length = 100)
    private String name;

    @Getter @Setter
    @Column(name = "account_num", nullable = false, length = 20)
    private String num;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "legal_entity_id", referencedColumnName = "legal_entity_id")
    private LegalEntity legalEntity;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "bank_id", nullable = false)
    private Bank bank;

    @Getter @Setter
    @Column(name = "currency_code", nullable = false, length = 10)
    private String currencyCode;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "account_status_id", referencedColumnName = "account_status_id", nullable = false)
    private AccountStatus accountStatus;

    //OneToMany part

    @Getter @Setter
    @OneToMany(mappedBy = "account")
    private Collection<AccountBalance> accountBalances;
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "accountDebet", fetch = FetchType.LAZY)
//    private Collection<Operation> operationsDebet;
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "accountKredit", fetch = FetchType.LAZY)
//    private Collection<Operation> operationsKredit;
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "accountSender", fetch = FetchType.LAZY)
//    private Collection<PaymentOrder> paymentOrdersSender;
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "accountRecipient", fetch = FetchType.LAZY)
//    private Collection<PaymentOrder> paymentOrdersRecipient;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Account that = (Account) o;
//
//        if (id != null ? !id.equals(that.id) : that.id != null) return false;
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
