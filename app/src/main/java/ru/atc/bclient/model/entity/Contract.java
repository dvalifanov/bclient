package ru.atc.bclient.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "dim_contract", schema = "bclient3", catalog = "postgres")
public class Contract {

    @Getter @Setter
    @Id
    @Column(name = "contract_id", nullable = false)
    private Integer id;

    @Getter @Setter
    @Column(name = "contract_name", nullable = false, length = 100)
    private String name;

    @Getter @Setter
    @Column(name = "contract_num", nullable = false, length = 20)
    private String num;

    @Getter @Setter
    @Column(name = "contract_open_date", nullable = false)
    private Date openDate;

    @Getter @Setter
    @Column(name = "contract_close_date", nullable = false)
    private Date closeDate;

    @Getter @Setter
    @Column(name = "currency_code", nullable = false, length = 10)
    private String currencyCode;

    //ManyToOne part

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "issuer_legal_entity_id", referencedColumnName = "legal_entity_id", nullable = false)
    private LegalEntity legalEntityIssuer;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "singer_legal_entity_id", referencedColumnName = "legal_entity_id", nullable = false)
    private LegalEntity legalEntitySigner;

    //OneToMany part
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
//    private Collection<PaymentOrder> paymentOrders;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Contract that = (Contract) o;
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
