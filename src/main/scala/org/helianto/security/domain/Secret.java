package org.helianto.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.enums.ActivityState;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Identity secret.
 * 
 * Allows one identity to have more than one secret using a
 * different key.
 * 
 * @author mauriciofernandesdecastro
 */
@Entity
@Table(name="core_secret",
    uniqueConstraints = {
		 @UniqueConstraint(columnNames={"identityKey"})
    }
)
public class Secret implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Version
    private int version;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="identityId", nullable=true)
    private Identity identity;
    
    @Column(length=40)
    private String identityKey = "";
    
    @Column(length=60)
    private String identitySecret;
    
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private ActivityState credentialState = ActivityState.ACTIVE;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    
    /**
     * Constructor.
     */
    public Secret() {
		super();
	}
    
    /**
     * Constructor.
     * 
     * @param identityKey
     */
    public Secret(String identityKey) {
		this();
		setIdentityKey(identityKey);
	}

    /**
     * Constructor.
     * 
     * @param identity
     * @param identityKey
     */
    public Secret(Identity identity, String identityKey) {
		this(identityKey);
		setIdentity(identity);
	}

    /**
     * Encode a password.
     * 
     * @param encoder
     * @param rawPassword
     */
    public Secret encode(PasswordEncoder encoder, String rawPassword) {
    	setIdentitySecret(encoder.encode(rawPassword));
    	return this;
    }
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Identity getIdentity() {
		return identity;
	}
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public String getIdentityKey() {
		return identityKey;
	}
	public void setIdentityKey(String identityKey) {
		this.identityKey = identityKey;
	}

	public String getIdentitySecret() {
		return identitySecret;
	}
	public void setIdentitySecret(String identitySecret) {
		this.identitySecret = identitySecret;
	}

	public ActivityState getCredentialState() {
		return credentialState;
	}
	public void setCredentialState(ActivityState credentialState) {
		this.credentialState = credentialState;
	}

	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identity == null) ? 0 : identity.hashCode());
		result = prime * result
				+ ((identityKey == null) ? 0 : identityKey.hashCode());
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
		Secret other = (Secret) obj;
		if (identity == null) {
			if (other.identity != null)
				return false;
		} else if (!identity.equals(other.identity))
			return false;
		if (identityKey == null) {
			if (other.identityKey != null)
				return false;
		} else if (!identityKey.equals(other.identityKey))
			return false;
		return true;
	}
    
}
