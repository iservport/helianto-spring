package org.helianto.ingress.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.IdentityData;
import org.helianto.core.domain.PersonalData;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="core_registration")
public class Registration implements IdentityData {

    @Id @Column(length=32)
    private String id;

    @Version
    private int version;

    @Column(length=64)
    private String principal = "";

    @Column(length=20)
    private String cellPhone = "";

    @Column(length=32)
    private String displayName = "";

    @JsonIgnore
    @Embedded
    private PersonalData personalData = new PersonalData();

    @Column(length=32)
    private String password = "";

    @Column(length=32)
    private String entityAlias = "";

    private boolean admin;

    private boolean isDomain;

    @Column(length=32)
    private String cityId;

    @Column(length=64)
    private String providerUserId;

    public Registration() {
        super();
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public Registration(String principal) {
        this();
        this.principal = principal;
    }

    public Registration(String principal, PersonalData personalData) {
        this(principal);
        this.personalData = personalData;
    }

    public Registration(String principal, String firstName, String lastName, String imageUrl) {
        this(principal, new PersonalData(firstName, lastName, imageUrl));
    }

    public Registration(Identity identity) {
        this(Objects.requireNonNull(identity, "Identity must not be null").getPrincipal(), identity.getPersonalData());
    }

    public String getPrincipal() {
        return this.principal;
    }

    public String getCellPhone() {
        if (this.cellPhone==null) {
            return "";
        }
        return this.cellPhone;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getFirstName() {
        if (getPersonalData()!=null) {
            return getPersonalData().getFirstName();
        }
        return "";
    }

    public String getLastName() {
        if (getPersonalData()!=null) {
            return getPersonalData().getLastName();
        }
        return "";
    }

    public String getImageUrl() {
        if (getPersonalData()!=null) {
            return getPersonalData().getImageUrl();
        }
        return "";
    }

    public PersonalData getPersonalData() {
        return this.personalData;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEntityAlias() {
        return this.entityAlias;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public boolean isDomain() {
        return this.isDomain;
    }

    public String getCityId() {
        return this.cityId;
    }

    public String getProviderUserId() {
        return this.providerUserId;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setFirstName(String firstName) {
        if (getPersonalData()!=null) {
            getPersonalData().setFirstName(firstName);
        }
    }

    public void setLastName(String lastName) {
        if (getPersonalData()!=null) {
            getPersonalData().setLastName(lastName);
        }
    }

    public void setImageUrl(String imageUrl) {
        if (getPersonalData()!=null) {
            getPersonalData().setImageUrl(imageUrl);
        }
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEntityAlias(String entityAlias) {
        this.entityAlias = entityAlias;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setDomain(boolean isDomain) {
        this.isDomain = isDomain;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Registration)) return false;
        final Registration other = (Registration) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        if (this.getVersion() != other.getVersion()) return false;
        final Object this$principal = this.getPrincipal();
        final Object other$principal = other.getPrincipal();
        if (this$principal == null ? other$principal != null : !this$principal.equals(other$principal)) return false;
        final Object this$cellPhone = this.getCellPhone();
        final Object other$cellPhone = other.getCellPhone();
        if (this$cellPhone == null ? other$cellPhone != null : !this$cellPhone.equals(other$cellPhone)) return false;
        final Object this$displayName = this.getDisplayName();
        final Object other$displayName = other.getDisplayName();
        if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName))
            return false;
        final Object this$personalData = this.getPersonalData();
        final Object other$personalData = other.getPersonalData();
        if (this$personalData == null ? other$personalData != null : !this$personalData.equals(other$personalData))
            return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$entityAlias = this.getEntityAlias();
        final Object other$entityAlias = other.getEntityAlias();
        if (this$entityAlias == null ? other$entityAlias != null : !this$entityAlias.equals(other$entityAlias))
            return false;
        if (this.isAdmin() != other.isAdmin()) return false;
        if (this.isDomain() != other.isDomain()) return false;
        final Object this$cityId = this.getCityId();
        final Object other$cityId = other.getCityId();
        if (this$cityId == null ? other$cityId != null : !this$cityId.equals(other$cityId)) return false;
        final Object this$providerUserId = this.getProviderUserId();
        final Object other$providerUserId = other.getProviderUserId();
        if (this$providerUserId == null ? other$providerUserId != null : !this$providerUserId.equals(other$providerUserId))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        result = result * PRIME + this.getVersion();
        final Object $principal = this.getPrincipal();
        result = result * PRIME + ($principal == null ? 43 : $principal.hashCode());
        final Object $cellPhone = this.getCellPhone();
        result = result * PRIME + ($cellPhone == null ? 43 : $cellPhone.hashCode());
        final Object $displayName = this.getDisplayName();
        result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
        final Object $personalData = this.getPersonalData();
        result = result * PRIME + ($personalData == null ? 43 : $personalData.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $entityAlias = this.getEntityAlias();
        result = result * PRIME + ($entityAlias == null ? 43 : $entityAlias.hashCode());
        result = result * PRIME + (this.isAdmin() ? 79 : 97);
        result = result * PRIME + (this.isDomain() ? 79 : 97);
        final Object $cityId = this.getCityId();
        result = result * PRIME + ($cityId == null ? 43 : $cityId.hashCode());
        final Object $providerUserId = this.getProviderUserId();
        result = result * PRIME + ($providerUserId == null ? 43 : $providerUserId.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Registration;
    }

    public String toString() {
        return "org.helianto.ingress.domain.Registration(id=" + this.getId() + ", version=" + this.getVersion() + ", principal=" + this.getPrincipal() + ", cellPhone=" + this.getCellPhone() + ", displayName=" + this.getDisplayName() + ", personalData=" + this.getPersonalData() + ", password=" + this.getPassword() + ", entityAlias=" + this.getEntityAlias() + ", admin=" + this.isAdmin() + ", isDomain=" + this.isDomain() + ", cityId=" + this.getCityId() + ", providerUserId=" + this.getProviderUserId() + ")";
    }
}
