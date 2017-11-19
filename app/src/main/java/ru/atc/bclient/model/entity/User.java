package ru.atc.bclient.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "dim_user", schema = "bclient3", catalog = "postgres")
public class User {

    @Getter @Setter
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Getter @Setter
    @Column(name = "user_login", nullable = false, length = 100)
    private String login;

    @Getter @Setter
    @Column(name = "user_full_name", nullable = false, length = 300)
    private String fullName;

    @Getter @Setter
    @Column(name = "user_password", nullable = false, length = 100)
    private String password;

    //OneToMany part

    @Getter @Setter
    @OneToMany(mappedBy = "primaryKey.user", cascade = CascadeType.ALL)
    private Collection<UserLegalEntity> userLegalEntities;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        User user = (User) o;
//
//        if (id != null ? !id.equals(user.id) : user.id != null) return false;
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
