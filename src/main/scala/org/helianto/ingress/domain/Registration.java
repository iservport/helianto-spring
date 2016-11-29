package org.helianto.ingress.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.IdentityData;
import org.helianto.core.domain.PersonalData;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name="core_registration")
public class Registration extends AbstractRegistration implements IdentityData {

    @Id @Column(length=32)
    private String id;

    @Column(length=32)
    private String contextName = "";

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

    @Transient
    private String password = "";

    @Column(length=64)
    private String entityAlias = "";

    @Column(length=128)
    private String entityName = "";

    @Column(length=20)
    private String pun = "";

    private boolean admin;

    private boolean isDomain;

    @Column(length=32)
    private String stateCode;

    @Column(length=32)
    private String cityId;

    @Column(length=64)
    private String providerUserId;

    @Column(length=20)
    private String role = "ADMIN";

    public Registration() {
        super();
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        setIssueDate(new Date());
    }

    public Registration(String contextName, boolean failIfNoContext) {
        this();
        if (failIfNoContext && Optional.ofNullable(contextName).orElse("").equals("")) {
            throw new IllegalArgumentException("Context name required");
        }
        this.contextName = contextName;
    }

    public Registration(String principal) {
        this();
        this.principal = principal;
    }

    public Registration(String principal, PersonalData personalData) {
        this(principal);
        this.personalData = personalData;
    }

    public Registration(String principal, String firstName, String lastName) {
        this(principal, new PersonalData(firstName, lastName, ""));
    }

    public Registration(String principal, String firstName, String lastName, String imageUrl) {
        this(principal, new PersonalData(firstName, lastName, imageUrl));
    }

    public Registration(String principal, String firstName, String lastName, String imageUrl, String providerUserId) {
        this(principal, new PersonalData(firstName, lastName, imageUrl));
        this.providerUserId = providerUserId;
    }

    public Registration(Identity identity) {
        this(Objects.requireNonNull(identity, "Identity must not be null").getPrincipal(), identity.getPersonalData());
    }

    public String getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public String getContextName() {
        return contextName;
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

    public String getEntityName() {
        return entityName;
    }

    public String getPun() {
        return pun;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public boolean isDomain() {
        return this.isDomain;
    }

    public String getStateCode() {
        if (this.stateCode!=null) {
            return stateCode.replace("string:", "").replace(" ","");
        }
        return stateCode;
    }

    public String getCityId() {
        return this.cityId;
    }

    public String getProviderUserId() {
        return this.providerUserId;
    }

    public String getRole() {
        if (this.role!=null && this.role.isEmpty()) {
            return "ADMIN";
        }
        return role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
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

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setPun(String pun) {
        this.pun = pun;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setDomain(boolean isDomain) {
        this.isDomain = isDomain;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Merger
     *
     * @param command
     */
    public Registration merge(Registration command) {
        super.merge(command);
        setPrincipal(command.getPrincipal());
        setCellPhone(command.getCellPhone());
        setDisplayName(command.getDisplayName());
        setPersonalData(command.getPersonalData());
        setEntityAlias(command.getEntityAlias());
        setEntityName(command.getEntityName());
        setPun(command.getPun());
        setAdmin(command.isAdmin());
        setStateCode(command.getStateCode());
        setCityId(command.getCityId());
        setProviderUserId(command.getProviderUserId());
        setRole(command.getRole());
        return this;
    }

    /**
     * Merge remote address.
     *
     * @param remoteAddress
     */
    public Registration merge(String remoteAddress) {
        if(remoteAddress!=null && !remoteAddress.isEmpty()) {
            super.setRemoteAddress(remoteAddress);
        }
        return this;
    }


    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Registration)) return false;
        final Registration other = (Registration) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$contextName = this.getContextName();
        final Object other$contextName = other.getContextName();
        if (this$contextName == null ? other$contextName != null : !this$contextName.equals(other$contextName))
            return false;
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
        final Object this$entityName = this.getEntityName();
        final Object other$entityName = other.getEntityName();
        if (this$entityName == null ? other$entityName != null : !this$entityName.equals(other$entityName))
            return false;
        final Object this$pun = this.getPun();
        final Object other$pun = other.getPun();
        if (this$pun == null ? other$pun != null : !this$pun.equals(other$pun)) return false;
        if (this.isAdmin() != other.isAdmin()) return false;
        if (this.isDomain() != other.isDomain()) return false;
        final Object this$stateCode = this.getStateCode();
        final Object other$stateCode = other.getStateCode();
        if (this$stateCode == null ? other$stateCode != null : !this$stateCode.equals(other$stateCode)) return false;
        final Object this$cityId = this.getCityId();
        final Object other$cityId = other.getCityId();
        if (this$cityId == null ? other$cityId != null : !this$cityId.equals(other$cityId)) return false;
        final Object this$providerUserId = this.getProviderUserId();
        final Object other$providerUserId = other.getProviderUserId();
        if (this$providerUserId == null ? other$providerUserId != null : !this$providerUserId.equals(other$providerUserId))
            return false;
        final Object this$role = this.getRole();
        final Object other$role = other.getRole();
        if (this$role == null ? other$role != null : !this$role.equals(other$role)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $contextName = this.getContextName();
        result = result * PRIME + ($contextName == null ? 43 : $contextName.hashCode());
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
        final Object $entityName = this.getEntityName();
        result = result * PRIME + ($entityName == null ? 43 : $entityName.hashCode());
        final Object $pun = this.getPun();
        result = result * PRIME + ($pun == null ? 43 : $pun.hashCode());
        result = result * PRIME + (this.isAdmin() ? 79 : 97);
        result = result * PRIME + (this.isDomain() ? 79 : 97);
        final Object $stateCode = this.getStateCode();
        result = result * PRIME + ($stateCode == null ? 43 : $stateCode.hashCode());
        final Object $cityId = this.getCityId();
        result = result * PRIME + ($cityId == null ? 43 : $cityId.hashCode());
        final Object $providerUserId = this.getProviderUserId();
        result = result * PRIME + ($providerUserId == null ? 43 : $providerUserId.hashCode());
        final Object $role = this.getRole();
        result = result * PRIME + ($role == null ? 43 : $role.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Registration;
    }

    public String toString() {
        return "org.helianto.ingress.domain.Registration(id=" + this.getId() + ", contextName=" + this.getContextName() + ", version=" + this.getVersion() + ", principal=" + this.getPrincipal() + ", cellPhone=" + this.getCellPhone() + ", displayName=" + this.getDisplayName() + ", personalData=" + this.getPersonalData() + ", password=[******], entityAlias=" + this.getEntityAlias() + ", entityName=" + this.getEntityName() + ", pun=" + this.getPun() + ", admin=" + this.isAdmin() + ", isDomain=" + this.isDomain() + ", stateCode=" + this.getStateCode() + ", cityId=" + this.getCityId() + ", providerUserId=" + this.getProviderUserId() + ", role=" + this.getRole() + ")";
    }
}
