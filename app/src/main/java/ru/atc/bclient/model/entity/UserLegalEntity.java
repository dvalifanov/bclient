package ru.atc.bclient.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rel_user_legal_entity", schema = "bclient3", catalog = "postgres")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.user",
                joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "primaryKey.legalEntity",
                joinColumns = @JoinColumn(name = "legal_entity_id")) })

public class UserLegalEntity {

    @Getter @Setter
    @EmbeddedId
    private UserLegalEntityPK primaryKey = new UserLegalEntityPK();

    @Transient
    public User getUser() {
        return getPrimaryKey().getUser();
    }

    public void setUser(User user) {
        getPrimaryKey().setUser(user);
    }

    @Transient
    public LegalEntity getLegalEntity() {
        return getPrimaryKey().getLegalEntity();
    }

    public void setLegalEntity(LegalEntity legalEntity) {
        getPrimaryKey().setLegalEntity(legalEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLegalEntity that = (UserLegalEntity) o;

        if (primaryKey.getUser() != null ? !primaryKey.getUser().equals(that.primaryKey.getUser()) : that.primaryKey.getUser() != null) return false;
        if (primaryKey.getLegalEntity() != null ? !primaryKey.getLegalEntity().equals(that.primaryKey.getLegalEntity()) : that.primaryKey.getLegalEntity() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = primaryKey.getUser() != null ? primaryKey.getUser().hashCode() : 0;
        result = 31 * result + (primaryKey.getLegalEntity() != null ? primaryKey.getLegalEntity().hashCode() : 0);
        return result;
    }
}
