/* Copyright 2005 I Serv Consultoria Empresarial Ltda.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.helianto.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.enums.ActivityState;
import org.helianto.user.domain.enums.UserState;
import org.helianto.user.domain.enums.UserType;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * An user account (or group) represents a set of roles within an <code>Entity</code>. 
 * 
 * @author Mauricio Fernandes de Castro
 */
@Entity
@Table(name="core_user",
        uniqueConstraints = {@UniqueConstraint(columnNames={"entityId", "userKey"})}
)
public class User implements Comparable<User>{
	
	@Id @Column(length=32)
	private String id;

	@Version
	private Integer version;

	@Column(length=32)
	private String entityId;

    @Column(length=12)
    private UserType userType = UserType.INTERNAL;

	@Column(length=40)
    private String userKey = "";
    
    @Column(length=64)
    private String userName = "";
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEvent = new Date();

    @Enumerated(EnumType.STRING) @Column(length = 12)
    private UserState userState = UserState.ACTIVE;

    private boolean accountNonExpired = true;
    
	@Column(length=512)
    private String userDesc = "";

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="identityId", nullable=true)
    private Identity identity;

    @Transient
    private String identityId = "";

    private int minimalEducationRequirement;

    private Character privacyLevel = '0';

    @Column(length=255)
    private String scriptItems = "";

    @JsonIgnore
    @OneToMany(mappedBy="child")
    private Set<UserAssociation> parentAssociations = new HashSet<UserAssociation>();
    
    @JsonIgnore
    @OneToMany(mappedBy="parent")
    private Set<UserAssociation> childAssociations = new HashSet<UserAssociation>();
    
	/**
	 * Default constructor.
	 */
    public User() {
    	super();
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
    }

	/**
	 * User group constructor.
	 * 
	 * @param entityId
	 * @param userKey
	 */
    public User(String entityId, String userKey) {
    	this();
        setEntityId(entityId);
    	setUserKey(userKey);
    }

    /**
     * UserType constructor.
     *
     * @param entityId
     * @param userType
     * @param userKey
     * @param userName
     */
    public User(String entityId, UserType userType, String userKey, String userName) {
        this(entityId, userKey);
        this.userType = userType;
        this.userName = userName;
    }

    /**
     * Identity constructor.
     *
     * @param entityId
     * @param userType
     * @param identity
     * @param userState
     */
    public User(String entityId, UserType userType, Identity identity, UserState userState) {
        this(entityId, userType, identity.getPrincipal(), identity.getIdentityName());
        setIdentity(identity);
        setUserState(userState);
    }

    /**
     * True if user command is active.
     */
    public boolean isAccountNonLocked() {
        if (getUserState().equals(ActivityState.ACTIVE)) {
            return true;
        }
        return false;
    }
    
	/**
	 * True if user group is system.
	 */
	public boolean isSystemGroup() {
		return this.userType==UserType.SYSTEM;
	}

	/**
	 * Merger.
	 * 
	 * @param command
	 */
	public User merge(User command) {
		setUserName(command.getUserName());
		setLastEvent(command.getLastEvent());
		setUserState(command.getUserState());
		setUserType(command.getUserType());
		setAccountNonExpired(command.isAccountNonExpired());
		setUserDesc(command.getUserDesc());
		setMinimalEducationRequirement(command.getMinimalEducationRequirement());
		setScriptItems(command.getScriptItems());
		return this;
	}

    public User updateLastEvent() {
        this.lastEvent = new Date();
        return this;
    }

	/**
	 * Comparator.
	 * 
	 * @param other
	 */
	public int compareTo(User other) {
		if (getUserKey()!=null && other!=null && other.getUserKey()!=null) {
			return getUserKey().compareTo(other.getUserKey());
		}
		return 1;
	}

    public String getId() {
        return this.id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public Boolean isGroup() {
        if (getUserType()!=null) {
            return getUserType().isGroup();
        }
        return false;
    }

    public String getUserKey() {
        if (getIdentity()!=null) {
            return getIdentity().getIdentityName();
        }
        return this.userKey;
    }

    public String getUserName() {
        return this.userName;
    }

    public Date getLastEvent() {
        return this.lastEvent;
    }

    public UserState getUserState() {
        return this.userState;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public String getUserDesc() {
        return this.userDesc;
    }

    public Identity getIdentity() {
        return this.identity;
    }

    public String getIdentityId() {
        return this.identityId;
    }

    public int getMinimalEducationRequirement() {
        return this.minimalEducationRequirement;
    }

    public Character getPrivacyLevel() {
        return this.privacyLevel;
    }

    public String getScriptItems() {
        return this.scriptItems;
    }

    public Set<UserAssociation> getParentAssociations() {
        return this.parentAssociations;
    }

    public Set<UserAssociation> getChildAssociations() {
        return this.childAssociations;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLastEvent(Date lastEvent) {
        this.lastEvent = lastEvent;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public void setMinimalEducationRequirement(int minimalEducationRequirement) {
        this.minimalEducationRequirement = minimalEducationRequirement;
    }

    public void setPrivacyLevel(Character privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public void setScriptItems(String scriptItems) {
        this.scriptItems = scriptItems;
    }

    public void setParentAssociations(Set<UserAssociation> parentAssociations) {
        this.parentAssociations = parentAssociations;
    }

    public void setChildAssociations(Set<UserAssociation> childAssociations) {
        this.childAssociations = childAssociations;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.id;
        final Object other$id = other.id;
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$version = this.version;
        final Object other$version = other.version;
        if (this$version == null ? other$version != null : !this$version.equals(other$version)) return false;
        final Object this$entityId = this.entityId;
        final Object other$entityId = other.entityId;
        if (this$entityId == null ? other$entityId != null : !this$entityId.equals(other$entityId)) return false;
        final Object this$userKey = this.userKey;
        final Object other$userKey = other.userKey;
        if (this$userKey == null ? other$userKey != null : !this$userKey.equals(other$userKey)) return false;
        final Object this$userName = this.userName;
        final Object other$userName = other.userName;
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) return false;
        final Object this$lastEvent = this.lastEvent;
        final Object other$lastEvent = other.lastEvent;
        if (this$lastEvent == null ? other$lastEvent != null : !this$lastEvent.equals(other$lastEvent)) return false;
        final Object this$userState = this.userState;
        final Object other$userState = other.userState;
        if (this$userState == null ? other$userState != null : !this$userState.equals(other$userState)) return false;
        final Object this$userType = this.userType;
        final Object other$userType = other.userType;
        if (this$userType == null ? other$userType != null : !this$userType.equals(other$userType)) return false;
        if (this.accountNonExpired != other.accountNonExpired) return false;
        final Object this$userDesc = this.userDesc;
        final Object other$userDesc = other.userDesc;
        if (this$userDesc == null ? other$userDesc != null : !this$userDesc.equals(other$userDesc)) return false;
        final Object this$identity = this.identity;
        final Object other$identity = other.identity;
        if (this$identity == null ? other$identity != null : !this$identity.equals(other$identity)) return false;
        final Object this$identityId = this.identityId;
        final Object other$identityId = other.identityId;
        if (this$identityId == null ? other$identityId != null : !this$identityId.equals(other$identityId))
            return false;
        if (this.minimalEducationRequirement != other.minimalEducationRequirement) return false;
        final Object this$privacyLevel = this.privacyLevel;
        final Object other$privacyLevel = other.privacyLevel;
        if (this$privacyLevel == null ? other$privacyLevel != null : !this$privacyLevel.equals(other$privacyLevel))
            return false;
        final Object this$scriptItems = this.scriptItems;
        final Object other$scriptItems = other.scriptItems;
        if (this$scriptItems == null ? other$scriptItems != null : !this$scriptItems.equals(other$scriptItems))
            return false;
        final Object this$parentAssociations = this.parentAssociations;
        final Object other$parentAssociations = other.parentAssociations;
        if (this$parentAssociations == null ? other$parentAssociations != null : !this$parentAssociations.equals(other$parentAssociations))
            return false;
        final Object this$childAssociations = this.childAssociations;
        final Object other$childAssociations = other.childAssociations;
        if (this$childAssociations == null ? other$childAssociations != null : !this$childAssociations.equals(other$childAssociations))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.id;
        result = result * PRIME + ($id == null ? 0 : $id.hashCode());
        final Object $version = this.version;
        result = result * PRIME + ($version == null ? 0 : $version.hashCode());
        final Object $entityId = this.entityId;
        result = result * PRIME + ($entityId == null ? 0 : $entityId.hashCode());
        final Object $userKey = this.userKey;
        result = result * PRIME + ($userKey == null ? 0 : $userKey.hashCode());
        final Object $userName = this.userName;
        result = result * PRIME + ($userName == null ? 0 : $userName.hashCode());
        final Object $lastEvent = this.lastEvent;
        result = result * PRIME + ($lastEvent == null ? 0 : $lastEvent.hashCode());
        final Object $userState = this.userState;
        result = result * PRIME + ($userState == null ? 0 : $userState.hashCode());
        final Object $userType = this.userType;
        result = result * PRIME + ($userType == null ? 0 : $userType.hashCode());
        result = result * PRIME + (this.accountNonExpired ? 79 : 97);
        final Object $userDesc = this.userDesc;
        result = result * PRIME + ($userDesc == null ? 0 : $userDesc.hashCode());
        final Object $identity = this.identity;
        result = result * PRIME + ($identity == null ? 0 : $identity.hashCode());
        final Object $identityId = this.identityId;
        result = result * PRIME + ($identityId == null ? 0 : $identityId.hashCode());
        result = result * PRIME + this.minimalEducationRequirement;
        final Object $privacyLevel = this.privacyLevel;
        result = result * PRIME + ($privacyLevel == null ? 0 : $privacyLevel.hashCode());
        final Object $scriptItems = this.scriptItems;
        result = result * PRIME + ($scriptItems == null ? 0 : $scriptItems.hashCode());
        final Object $parentAssociations = this.parentAssociations;
        result = result * PRIME + ($parentAssociations == null ? 0 : $parentAssociations.hashCode());
        final Object $childAssociations = this.childAssociations;
        result = result * PRIME + ($childAssociations == null ? 0 : $childAssociations.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof User;
    }

    public String toString() {
        return "org.helianto.User(id=" + this.id + ", version=" + this.version + ", entityId=" + this.entityId + ", userKey=" + this.userKey + ", userName=" + this.userName + ", lastEvent=" + this.lastEvent + ", userState=" + this.userState + ", userType=" + this.userType + ", accountNonExpired=" + this.accountNonExpired + ", userDesc=" + this.userDesc + ", identity=" + this.identity + ", identityId=" + this.identityId + ", minimalEducationRequirement=" + this.minimalEducationRequirement + ", privacyLevel=" + this.privacyLevel + ", scriptItems=" + this.scriptItems + ", parentAssociations=" + this.parentAssociations + ", childAssociations=" + this.childAssociations + ")";
    }
}
