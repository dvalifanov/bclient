package ru.atc.bclient.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "dim_account_status", schema = "bclient3", catalog = "postgres")
public class AccountStatus {

    @Getter @Setter
    @Id
    @Column(name = "account_status_id", nullable = false)
    private Integer id;

    @Getter @Setter
    @Column(name = "account_status_code", nullable = false, length = 100)
    private String code;

    @Getter @Setter
    @Column(name = "account_status_name", nullable = false, length = 300)
    private String name;

    //OneToMany part
//
//    @Getter @Setter
//    @OneToMany(mappedBy = "accountStatus", fetch = FetchType.LAZY)
//    private Collection<Account> accounts;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        AccountStatus that = (AccountStatus) o;
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
