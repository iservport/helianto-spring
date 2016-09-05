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
import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.enums.Appellation;
import org.helianto.core.domain.enums.Gender;

import javax.persistence.*;
import java.util.Date;
/**
 * <p>
 * The user account.
 * </p>
 * <p>
 * An user account represents the association between an <code>Identity</code>
 * and an <code>Entity</code>. Such association allows for a singly identified 
 * actor, i.e. a person or any other organizational <code>Identity</code>, to keep
 * a single authentication scheme and have multiple authorization schemes, one per
 * <code>Entity</code>.
 * </p>
 * @author Mauricio Fernandes de Castro
 * 			
 */
@javax.persistence.Entity
@DiscriminatorValue("U")
public class User 
	extends UserGroup {

	/**
	 * <<Transient>> Exposes the discriminator.
	 */
	public char getDiscriminator() {
		return 'U';
	}

    private static final long serialVersionUID = 1L;
    
    @Transient
	private String entityAlias;

    @Transient
    private Integer userGroupId;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="identityId", nullable=true)
    private Identity identity;
    
    @Transient
    private Integer identityId;
    
	@Transient
	private String displayName;

	@Transient
	private String firstName;

	@Transient
	private String lastName;

	@Transient
	private char userGender = Gender.NOT_SUPPLIED.getValue();

	@Transient
	private String userImageUrl;

    @Column(length=4)
    private String initials;
    
    private char privacyLevel = '0';
    
	/**
	 * Empty constructor.
	 */
    public User() {
    	super();
    }

	/** 
	 * Key constructor.
	 * 
	 * @param entity
	 * @param identity
	 */
    public User(Entity entity, Identity identity) {
    	this();
        setEntity(entity);
    	setIdentity(identity);
    }

	/**
	 * Helper method to chain last event setter.
	 */
	public User updateLastEvent() {
		setLastEvent(new Date());
		return this;
	}

    /**
     * Overridden to obtain the user key from the identity principal.
     */
    protected String getInternalUserKey() {
		if (super.getInternalUserKey()==null || super.getInternalUserKey().isEmpty()) {
	    	if (getIdentity()!=null && getIdentity().getPrincipal()!=null && getIdentity().getPrincipal().length()>0) {
	    		return getIdentity().getPrincipal();
	    	}
		}
		return super.getInternalUserKey();
    }
    
    /**
     * <<Transient>> entityAlias.
     */
    public String getEntityAlias() {
		return entityAlias;
	}
    public void setEntityAlias(String entityAlias) {
		this.entityAlias = entityAlias;
	}
    
    /**
     * <<Transient>> user group id.
     */
    public Integer getUserGroupId() {
		return userGroupId;
	}
    public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}
    
    /**
     * Identity.
     */
    public Identity getIdentity() {
        return this.identity;
    }
    
    /**
     * Setting the identity also sets the user key.
     * 
     * @param identity
     */
    public void setIdentity(Identity identity) {
        this.identity = identity;
    	setUserKey(getInternalUserKey());
		if (identity!=null) {
			this.identityId = identity.getId();
			this.firstName = identity.getIdentityFirstName();
			this.lastName = identity.getIdentityLastName();
			this.displayName = identity.getDisplayName();
			this.userGender = identity.getGender();
			this.userImageUrl = identity.getImageUrl();
		}
    }
    
    /**
     * <<Transient>> identity id.
     */
    public Integer getIdentityId() {
		return identityId;
	}
    public void setIdentityId(Integer identityId) {
		this.identityId = identityId;
	}
    
    /**
     * <<Transient>> Safe user principal.
     */
    public String getUserPrincipal() {
    	if (getIdentity()==null) {
    		return "";
    	}
        return getIdentity().getPrincipal();
    }
    
    /**
     * "<<Transient>> Safe user principal name.
     */
    public String getUserPrincipalName() {
    	if (getIdentity()!=null) {
    		return getIdentity().getPrincipalName();
    	}
        return "";
    }
    
    /**
     * <<Transient>> Internal user full name.
     */
    @Override
    protected String getInternalUserName() {
    	if (super.getInternalUserName()==null || super.getInternalUserName().isEmpty()) {
        	if (getIdentity()!=null) {
        		return getIdentity().getIdentityName();
        	}
    	}
    	return super.getInternalUserName();
    }
    
    /**
     * <<Transient>> Safe user principal domain.
     */
    public String getUserPrincipalDomain() {
    	if (getIdentity()!=null) {
    		return getIdentity().getPrincipalDomain();
    	}
        return "";
    }
    
    /**
     * <<Transient>> Safe user optional alias.
     * @deprecated
     */
    public String getUserOptionalAlias() {
    	if (getIdentity()!=null) {
    		return getIdentity().getDisplayName();
    	}
        return "";
    }
    
    /**
     * <<Transient>> Safe user display name.
     * @deprecated
     */
    public String getUserDisplayName() {
    	if (getIdentity()!=null) {
    		return getIdentity().getDisplayName();
    	}
        return displayName;
    }
    
    /**
     * <<Transient>> user display name.
     */
    public String getDisplayName() {
    	if (getIdentity()!=null) {
    		return getIdentity().getDisplayName();
    	}
        return displayName;
    }
    public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
    
    /**
     * <<Transient>> Safe user first name.
     * @deprecated
     */
    public String getUserFirstName() {
    	if (getIdentity()!=null) {
    		return getIdentity().getIdentityFirstName();
    	}
        return firstName;
    }
    
    /**
     * <<Transient>> user first name.
     */
    public String getFirstName() {
    	if (getIdentity()!=null) {
    		return getIdentity().getIdentityFirstName();
    	}
        return firstName;
    }
    public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
    
    /**
     * <<Transient>> Safe user last name.
     * @deprecated
     */
    public String getUserLastName() {
    	if (getIdentity()!=null) {
    		return getIdentity().getIdentityLastName();
    	}
        return lastName;
    }
    
    /**
     * <<Transient>> user last name.
     */
    public String getLastName() {
    	if (getIdentity()!=null) {
    		return getIdentity().getIdentityLastName();
    	}
        return lastName;
    }
    public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    
    /**
     * <<Transient>> user gender.
     */
    public char getUserGender() {
    	if (getIdentity()!=null) {
    		return getIdentity().getGender();
    	}
        return userGender ;
    }
    public void setUserGender(char userGender) {
		this.userGender = userGender;
	}
    
    /**
     * <<Transient>> user image URL.
     */
    public String getUserImageUrl() {
		return userImageUrl;
	}
    public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}
    
    /**
     * <<Transient>> Safe user appellation.
     */
    public char getUserAppelation() {
    	if (getIdentity()!=null) {
    		return getIdentity().getAppellation();
    	}
        return Appellation.NOT_SUPPLIED.getValue();
    }
    
    /**
     * <<Transient>> Safe user birth date.
     */
    public Date getUserBirthDate() {
    	if (getIdentity()!=null) {
    		return getIdentity().getBirthDate();
    	}
        return new Date();
    }
    
    /**
     * User initials (optional), like JFK, etc..
     */
    public String getInitials() {
		return initials;
	}
    public void setInitials(String initials) {
		this.initials = initials;
	}

    /**
     * Privacy level
     */
    public char getPrivacyLevel() {
        return this.privacyLevel;
    }
    public void setPrivacyLevel(char privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

	/**
	 * Merger.
	 * 
	 * @param command
	 */
	public User user(User command) {
		super.merge(command);
		setInitials(command.getInitials());
		setPrivacyLevel(command.getPrivacyLevel());
		return this;
	}
	
}
