package org.helianto.security.domain;

import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

/**
 * ORM implementation for Spring Social User Connection.
 * 
 * <p>
 * If using Spring Social {@link JdbcUsersConnectionRepository}, table prefix
 * property of this bean must be set to "core_".
 * </p>
 * 
 * @author mauriciofernandesdecastro
 */
@javax.persistence.Entity
@Table(name="core_UserConnection",
	uniqueConstraints = {@UniqueConstraint(columnNames = { "userId", "providerId", "providerUserId" }),
                         @UniqueConstraint(columnNames = { "userId", "providerId", "rank" })}
)
public class UserConnection 
	implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String userId;
	
	@Column(nullable=false)
	private String providerId;
	
	private String providerUserId;
	
	private int rank;
	
	@Column(length=128)
	private String displayName;
	
	@Column(length=512)
	private String profileUrl;
	
	@Column(length=512)
	private String imageUrl;
	
	@Column(nullable=false)
	private String accessToken;
	
	private String secret;
	
	private String refreshToken;
	
	private long expireTime;
	
	/**
	 * Constructor.
	 */
	public UserConnection() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public String getProviderUserId() {
		return providerUserId;
	}
	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((providerId == null) ? 0 : providerId.hashCode());
		result = prime * result
				+ ((providerUserId == null) ? 0 : providerUserId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof UserConnection)) {
			return false;
		}
		UserConnection other = (UserConnection) obj;
		if (providerId == null) {
			if (other.providerId != null) {
				return false;
			}
		} else if (!providerId.equals(other.providerId)) {
			return false;
		}
		if (providerUserId == null) {
			if (other.providerUserId != null) {
				return false;
			}
		} else if (!providerUserId.equals(other.providerUserId)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

}

