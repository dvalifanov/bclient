package ru.atc.bclient.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "dim_legal_entity", schema = "bclient3", catalog = "postgres")
public class LegalEntity {

    @Getter @Setter
    @Id
    @Column(name = "legal_entity_id", nullable = false)
    private Integer id;

    @Getter @Setter
    @Column(name = "legal_entity_short_name", nullable = false, length = 100)
    private String shortName;

    @Getter @Setter
    @Column(name = "legal_entity_full_name", nullable = false, length = 300)
    private String fullName;

    @Getter @Setter
    @Column(name = "legal_entity_inn", nullable = false, length = 20)
    private String inn;

    @Getter @Setter
    @Column(name = "legal_entity_kpp", nullable = false, length = 20)
    private String kpp;

    @Getter @Setter
    @Column(name = "legal_entity_ogrn", nullable = true, length = 20)
    private String ogrn;

    @Getter @Setter
    @Column(name = "legal_address", nullable = true, length = 500)
    private String address;

    //OneToMany part

    @Getter @Setter
    @OneToMany(mappedBy = "legalEntity")
    private Collection<Account> accounts;

//    @Getter @Setter
//    @OneToMany(mappedBy = "legalEntityIssuer", fetch = FetchType.LAZY)
//    private Collection<Contract> issuedContracts;
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "legalEntitySigner", fetch = FetchType.LAZY)
//    private Collection<Contract> signedContracts;
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "legalEntitySender", fetch = FetchType.LAZY)
//    private Collection<PaymentOrder> sendedPaymentOrders;
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "legalEntityRecipient", fetch = FetchType.LAZY)
//    private Collection<PaymentOrder> receivedPaymentOrders;
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "primaryKey.legalEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Collection<UserLegalEntity> userLegalEntities;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        LegalEntity that = (LegalEntity) o;
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
