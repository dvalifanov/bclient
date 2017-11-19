package ru.atc.bclient.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "fct_operation", schema = "bclient3", catalog = "postgres")
public class Operation {

    @Getter @Setter
    @Id
    @Column(name = "operation_id", nullable = false)
    private Integer id;

    @Getter @Setter
    @Column(name = "operation_date", nullable = false)
    private Date date;

    @Getter @Setter
    @Column(name = "operation_amt", nullable = false, precision = 2)
    private BigDecimal amt;

    @Getter @Setter
    @Column(name = "operation_descr", nullable = true, length = 300)
    private String descr;

    //ManyToOne part

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "debet_account_id", referencedColumnName = "account_id", nullable = false)
    private Account accountDebet;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "kredit_account_id", referencedColumnName = "account_id", nullable = false)
    private Account accountKredit;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Operation that = (Operation) o;
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
