package ru.atc.bclient.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Collection;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "dim_bank", schema = "bclient3", catalog = "postgres")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Bank {

    @Getter @Setter
    @Id
    @Column(name = "bank_id", nullable = false)
    private Integer id;

    @Getter @Setter
    @Column(name = "bank_name", nullable = false, length = 300)
    private String name;

    @Getter @Setter
    @Column(name = "bank_inn", nullable = false, length = 50)
    private String inn;

    @Getter @Setter
    @Column(name = "bank_kpp", nullable = false, length = 50)
    private String kpp;

    @Getter @Setter
    @Column(name = "bank_bic", nullable = false, length = 50)
    private String bic;

    @Getter @Setter
    @Column(name = "bank_corr_acc", nullable = true, length = 50)
    private String corrAcc;

    //OneToMany part
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY)
//    private Collection<Account> accounts;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Bank bank = (Bank) o;
//
//        if (id != null ? !id.equals(bank.id) : bank.id != null) return false;
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
