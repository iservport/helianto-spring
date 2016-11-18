package org.helianto.core.domain;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@javax.persistence.Entity
@Table(name="core_phone",
        uniqueConstraints = {@UniqueConstraint(columnNames={"identityId", "phoneNumber", "phoneLabel"})}
)
public class PersonalPhone {

    @Id @Column(length = 32)
    private String id;

    @Column(length=32)
    private String identityId = "";

    @Column(length=20)
    private String phoneNumber = "";

    @Column(length=12)
    private String phoneLabel = "CELL";

    public PersonalPhone() {
        super();
    }

    public PersonalPhone(String identityId) {
        this();
        this.identityId = identityId;
    }

    public PersonalPhone(String identityId, String phoneNumber) {
        this(identityId);
        this.phoneNumber = phoneNumber;
    }

    public PersonalPhone(String identityId, String phoneNumber, String phoneLabel) {
        this(identityId, phoneNumber);
        this.phoneLabel = phoneLabel;
    }

    public String getId() {
        return this.id;
    }

    public String getIdentityId() {
        return this.identityId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getPhoneLabel() {
        return this.phoneLabel;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhoneLabel(String phoneLabel) {
        this.phoneLabel = phoneLabel;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PersonalPhone)) return false;
        final PersonalPhone other = (PersonalPhone) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$identityId = this.getIdentityId();
        final Object other$identityId = other.getIdentityId();
        if (this$identityId == null ? other$identityId != null : !this$identityId.equals(other$identityId))
            return false;
        final Object this$phoneNumber = this.getPhoneNumber();
        final Object other$phoneNumber = other.getPhoneNumber();
        if (this$phoneNumber == null ? other$phoneNumber != null : !this$phoneNumber.equals(other$phoneNumber))
            return false;
        final Object this$phoneLabel = this.getPhoneLabel();
        final Object other$phoneLabel = other.getPhoneLabel();
        if (this$phoneLabel == null ? other$phoneLabel != null : !this$phoneLabel.equals(other$phoneLabel))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $identityId = this.getIdentityId();
        result = result * PRIME + ($identityId == null ? 43 : $identityId.hashCode());
        final Object $phoneNumber = this.getPhoneNumber();
        result = result * PRIME + ($phoneNumber == null ? 43 : $phoneNumber.hashCode());
        final Object $phoneLabel = this.getPhoneLabel();
        result = result * PRIME + ($phoneLabel == null ? 43 : $phoneLabel.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PersonalPhone;
    }

    public String toString() {
        return "org.helianto.core.domain.PersonalPhone(id=" + this.getId() + ", identityId=" + this.getIdentityId() + ", phoneNumber=" + this.getPhoneNumber() + ", phoneLabel=" + this.getPhoneLabel() + ")";
    }
}
