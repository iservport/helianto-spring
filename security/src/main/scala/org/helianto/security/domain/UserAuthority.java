package org.helianto.security.domain;

import org.helianto.core.domain.enums.ActivityState;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Domain class to represent user authority.
 * 
 * Grants roles to user groups.
 * 
 * @author mauriciofernandesdecastro
 */
@javax.persistence.Entity
@Table(name="core_authority",
    uniqueConstraints = {
		 @UniqueConstraint(columnNames={"userGroupId", "serviceCode"})
    }
)
public class UserAuthority implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @Column(length=32)
    private String id;
    
    @Version
    private int version;

	@Column(length=32)
	private String userId;
	
    @Column(length=20)
	private String serviceCode;
	
    @Column(length=128)
	private String serviceExtension;

    @Column(length=20) @Enumerated(EnumType.STRING)
    private ActivityState authorityState = ActivityState.ACTIVE;
    
    /**
     * Constructor.
     */
    public UserAuthority() {
		super();
	}
    
    /**
     * Constructor.
     * 
     * @param userId
     * @param serviceCode
     */
    public UserAuthority(String userId, String serviceCode) {
    	this();
		setUserId(userId);
		setServiceCode(serviceCode);
	}

    /**
     * Constructor.
     * 
     * @param userId
     * @param serviceCode
     * @param extensions
     */
    public UserAuthority(String userId, String serviceCode, String extensions) {
    	this(userId, serviceCode);
    	setServiceExtension(extensions);
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * User group where authorities are applied.
	 */
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * Service code.
	 */
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceExtension() {
		return serviceExtension;
	}
	public void setServiceExtension(String serviceExtension) {
		this.serviceExtension = serviceExtension;
	}

	public ActivityState getAuthorityState() {
		return authorityState;
	}
	public void setAuthorityState(ActivityState authorityState) {
		this.authorityState = authorityState;
	}
	
	/**
	 * Merger.
	 * 
	 * @param command
	 */
	public UserAuthority merge(UserAuthority command) {
		setServiceCode(command.getServiceCode());
		setServiceExtension(command.getServiceExtension());
		setAuthorityState(command.getAuthorityState());
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serviceCode == null) ? 0 : serviceCode.hashCode());
		result = prime * result
				+ ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAuthority other = (UserAuthority) obj;
		if (serviceCode == null) {
			if (other.serviceCode != null)
				return false;
		} else if (!serviceCode.equals(other.serviceCode))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
    
}
