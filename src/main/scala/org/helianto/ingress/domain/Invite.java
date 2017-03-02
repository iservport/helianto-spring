package org.helianto.ingress.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.helianto.core.domain.EntityData;
import org.helianto.core.domain.IdentityData;
import org.helianto.core.domain.PersonalData;
import org.helianto.core.domain.enums.Gender;

import javax.persistence.*;
import java.util.Date;

/**
 * Invitation form.
 */
@Entity
@Table(name="core_invite")
public class Invite extends AbstractRegistration implements IdentityData, EntityData {

    @Transient
    private boolean existing = true;

    @Id @Column(length=32)
    private String id;

    @Column(length=32)
    private String invitedByUser;

    @Column(length=32)
    private String userId;

    @Column(length=32)
    private String cityId;

    @Column(length=32)
    private String stateCode;

    @Column(length=128)
    private String entityName;

    @Column(length=64)
    private String entityAlias;

    @Column(length=20)
    private String pun = "";

    private char entityType = 'C';

    @Column(length=40)
    private String principal;

    @Column(length=64)
    private String firstName;

    @Column(length=64)
    private String lastName;

    @Column(length=32)
    private String displayName;

    private Character gender = Gender.NOT_SUPPLIED.getValue();

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate = new Date();

    @Column(length=128)
    private String imageUrl;

    public Invite() {
        super();
    }

    public Invite(String invitedByUser, boolean existing) {
        this();
        setInvitedByUser(invitedByUser);
        this.existing = existing;
    }

    public boolean isExisting() {
        return existing;
    }

    public Invite confirm(String userId) {
        setUserId(userId);
        setLastConfirmed(new Date());
        return this;
    }

    public String getId() {
        return this.id;
    }

    public String getInvitedByUser() {
        return this.invitedByUser;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getCityId() {
        return cityId;
    }

    @Override
    public String getStateCode() {
        return stateCode;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public String getEntityAlias() {
        return this.entityAlias;
    }

    @Override
    public String getPun() {
        return pun;
    }

    public char getEntityType() {
        return entityType;
    }

    public String getPrincipal() {
        return this.principal;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    @JsonIgnore
    public PersonalData getPersonalData() {
        return new PersonalData(getFirstName(), getLastName(), getGender(), '0', getBirthDate(), getImageUrl());
    }

    @Override
    public String getPassword() {
        return "";
    }

    public Character getGender() {
        if (this.gender==null) {
            return 'N';
        }
        return this.gender;
    }

    public Date getBirthDate() {
        if (this.birthDate==null) {
            return new Date();
        }
        return this.birthDate;
    }

    public String getImageUrl() {
        if (this.imageUrl==null) {
            return "";
        }
        return this.imageUrl;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInvitedByUser(String invitedByUser) {
        this.invitedByUser = invitedByUser;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setEntityAlias(String entityAlias) {
        this.entityAlias = entityAlias;
    }

    public void setPun(String pun) {
        this.pun = pun;
    }

    public void setEntityType(char entityType) {
        this.entityType = entityType;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPersonalData(PersonalData personalData) {}

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Invite merge(Invite command) {
        super.merge(command);
        setUserId(command.getUserId());
        setCityId(command.getCityId());
        setStateCode(command.getStateCode());
        setEntityName(command.getEntityName());
        setEntityAlias(command.getEntityAlias());
        setPun(command.getPun());
        setEntityType(command.getEntityType());
        setPrincipal(command.getPrincipal());
        setFirstName(command.getFirstName());
        setLastName(command.getLastName());
        setDisplayName(command.getDisplayName());
        setGender(command.getGender());
        setBirthDate(command.getBirthDate());
        setImageUrl(command.getImageUrl());
        return this;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Invite)) return false;
        final Invite other = (Invite) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.isExisting() != other.isExisting()) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$invitedByUser = this.getInvitedByUser();
        final Object other$invitedByUser = other.getInvitedByUser();
        if (this$invitedByUser == null ? other$invitedByUser != null : !this$invitedByUser.equals(other$invitedByUser))
            return false;
        final Object this$userId = this.getUserId();
        final Object other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
        final Object this$cityId = this.getCityId();
        final Object other$cityId = other.getCityId();
        if (this$cityId == null ? other$cityId != null : !this$cityId.equals(other$cityId)) return false;
        final Object this$stateCode = this.getStateCode();
        final Object other$stateCode = other.getStateCode();
        if (this$stateCode == null ? other$stateCode != null : !this$stateCode.equals(other$stateCode)) return false;
        final Object this$entityName = this.getEntityName();
        final Object other$entityName = other.getEntityName();
        if (this$entityName == null ? other$entityName != null : !this$entityName.equals(other$entityName))
            return false;
        final Object this$entityAlias = this.getEntityAlias();
        final Object other$entityAlias = other.getEntityAlias();
        if (this$entityAlias == null ? other$entityAlias != null : !this$entityAlias.equals(other$entityAlias))
            return false;
        final Object this$pun = this.getPun();
        final Object other$pun = other.getPun();
        if (this$pun == null ? other$pun != null : !this$pun.equals(other$pun)) return false;
        if (this.getEntityType() != other.getEntityType()) return false;
        final Object this$principal = this.getPrincipal();
        final Object other$principal = other.getPrincipal();
        if (this$principal == null ? other$principal != null : !this$principal.equals(other$principal)) return false;
        final Object this$firstName = this.getFirstName();
        final Object other$firstName = other.getFirstName();
        if (this$firstName == null ? other$firstName != null : !this$firstName.equals(other$firstName)) return false;
        final Object this$lastName = this.getLastName();
        final Object other$lastName = other.getLastName();
        if (this$lastName == null ? other$lastName != null : !this$lastName.equals(other$lastName)) return false;
        final Object this$displayName = this.getDisplayName();
        final Object other$displayName = other.getDisplayName();
        if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName))
            return false;
        final Object this$gender = this.getGender();
        final Object other$gender = other.getGender();
        if (this$gender == null ? other$gender != null : !this$gender.equals(other$gender)) return false;
        final Object this$birthDate = this.getBirthDate();
        final Object other$birthDate = other.getBirthDate();
        if (this$birthDate == null ? other$birthDate != null : !this$birthDate.equals(other$birthDate)) return false;
        final Object this$imageUrl = this.getImageUrl();
        final Object other$imageUrl = other.getImageUrl();
        if (this$imageUrl == null ? other$imageUrl != null : !this$imageUrl.equals(other$imageUrl)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isExisting() ? 79 : 97);
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $invitedByUser = this.getInvitedByUser();
        result = result * PRIME + ($invitedByUser == null ? 43 : $invitedByUser.hashCode());
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        final Object $cityId = this.getCityId();
        result = result * PRIME + ($cityId == null ? 43 : $cityId.hashCode());
        final Object $stateCode = this.getStateCode();
        result = result * PRIME + ($stateCode == null ? 43 : $stateCode.hashCode());
        final Object $entityName = this.getEntityName();
        result = result * PRIME + ($entityName == null ? 43 : $entityName.hashCode());
        final Object $entityAlias = this.getEntityAlias();
        result = result * PRIME + ($entityAlias == null ? 43 : $entityAlias.hashCode());
        final Object $pun = this.getPun();
        result = result * PRIME + ($pun == null ? 43 : $pun.hashCode());
        result = result * PRIME + this.getEntityType();
        final Object $principal = this.getPrincipal();
        result = result * PRIME + ($principal == null ? 43 : $principal.hashCode());
        final Object $firstName = this.getFirstName();
        result = result * PRIME + ($firstName == null ? 43 : $firstName.hashCode());
        final Object $lastName = this.getLastName();
        result = result * PRIME + ($lastName == null ? 43 : $lastName.hashCode());
        final Object $displayName = this.getDisplayName();
        result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
        final Object $gender = this.getGender();
        result = result * PRIME + ($gender == null ? 43 : $gender.hashCode());
        final Object $birthDate = this.getBirthDate();
        result = result * PRIME + ($birthDate == null ? 43 : $birthDate.hashCode());
        final Object $imageUrl = this.getImageUrl();
        result = result * PRIME + ($imageUrl == null ? 43 : $imageUrl.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Invite;
    }

    public String toString() {
        return "org.helianto.ingress.domain.Invite(existing=" + this.isExisting() + ", id=" + this.getId() + ", invitedByUser=" + this.getInvitedByUser() + ", userId=" + this.getUserId() + ", cityId=" + this.getCityId() + ", stateCode=" + this.getStateCode() + ", entityName=" + this.getEntityName() + ", entityAlias=" + this.getEntityAlias() + ", pun=" + this.getPun() + ", entityType=" + this.getEntityType() + ", principal=" + this.getPrincipal() + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ", displayName=" + this.getDisplayName() + ", gender=" + this.getGender() + ", birthDate=" + this.getBirthDate() + ", imageUrl=" + this.getImageUrl() + ")";
    }
}
