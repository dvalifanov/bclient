package ru.atc.bclient.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "fct_account_balance", schema = "bclient3", catalog = "postgres")
public class AccountBalance {

    @Getter @Setter
    @Id
    @Column(name = "account_balance_id", nullable = false)
    private Integer id;

    @Getter @Setter
    @Column(name = "account_balance_date", nullable = false)
    private Date date;

    @Getter @Setter
    @Column(name = "account_balance_amt", nullable = false, precision = 2)
    private BigDecimal amt;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        AccountBalance that = (AccountBalance) o;
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
