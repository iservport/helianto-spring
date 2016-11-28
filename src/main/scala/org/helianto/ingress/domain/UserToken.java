package org.helianto.ingress.domain;

import org.helianto.core.domain.IdentityData;
import org.helianto.core.domain.PersonalData;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Simple token registration.
 * 
 * @author mauriciofernandesdecastro
 */
@javax.persistence.Entity
@Table(name="core_token",
	uniqueConstraints = {@UniqueConstraint(columnNames={"tokenSource","principal"}),@UniqueConstraint(columnNames={"token"})}
)
public class UserToken extends AbstractRegistration
	implements Serializable, IdentityData
{

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Version
    private Integer version;
    
	private Integer identityId = 0;
	
    @Column(length=36)
    private String token;
    
	@NotEmpty
	@Column(length=20)
	private String tokenSource;
	
	@NotEmpty
	@Column(length=64)
	private String principal;
	
	@Column(length=128)
	private String firstName = "";
	
	@Column(length=128)
	private String lastName = "";
	
	public UserToken() {
		super();
		setIssueDate(new Date());
		setToken(UUID.randomUUID().toString());
	}
	
	/**
	 * UUID constructor.
	 * 
	 * @param tokenSource
	 * @param principal
	 */
	public UserToken(String tokenSource, String principal) {
		this();
		setTokenSource(tokenSource);
		setPrincipal(principal);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Integer getIdentityId() {
		return identityId;
	}
	public void setIdentityId(Integer identityId) {
		this.identityId = identityId;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTokenSource() {
		return tokenSource;
	}
	public void setTokenSource(String tokenSource) {
		this.tokenSource = tokenSource;
	}

	public String getPrincipal() {
		return principal;
	}

	@Override
	public String getDisplayName() {
		return this.firstName;
	}

	@Override
	public PersonalData getPersonalData() {
		return new PersonalData(this.firstName, this.lastName);
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public UserToken appendFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	
	public UserToken appendLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
		result = prime * result + ((tokenSource == null) ? 0 : tokenSource.hashCode());
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
		UserToken other = (UserToken) obj;
		if (principal == null) {
			if (other.principal != null)
				return false;
		} else if (!principal.equals(other.principal))
			return false;
		if (tokenSource == null) {
			if (other.tokenSource != null)
				return false;
		} else if (!tokenSource.equals(other.tokenSource))
			return false;
		return true;
	}

}
