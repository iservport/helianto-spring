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

package org.helianto.security.domain;

import org.helianto.user.domain.User;
import org.helianto.user.repository.UserProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

/**
 * Models core user information retrieved by UserDetailsService as an adapter class
 * to {@link User}.
 * 
 * <p>
 * A new <code>UserDetailsAdapter</code> may be created from a single
 * {@link User} and the correspondent credential to be expected
 * during authentication. A new <code>UserDetailsAdapter</code> may also be created from
 * a group with no credential specified, where the authentication is then considered 
 * to be anonymous.
 * </p>
 * 
 * @author Mauricio Fernandes de Castro
 */
public class UserDetailsAdapter
	implements
      Serializable
    , UserDetails {

    static final Logger logger = LoggerFactory.getLogger(UserDetailsAdapter.class);
    
	private static final long serialVersionUID = 1L;
	
	private UserProjection user;

	private Secret secret;
    
    private Collection<GrantedAuthority> authorities = new ArrayList<>();

    /**
     * Constructor
     */
    public UserDetailsAdapter() {
    	super();
    }
    
    /**
     * Constructor.
     * 
     * @param user
     */
    public UserDetailsAdapter(UserProjection user) {
        this();
        this.user = user;
    }
    
    /**
     * Constructor.
     * 
     * @param user
     * @param identitySecurity
     */
    public UserDetailsAdapter(UserProjection user, Secret identitySecurity) {
        this(user);
        this.secret = identitySecurity;
    }
    
    /**
     * User projection.
     */
    public UserProjection getUser() {
		return user;
	}

    public String getUserId() {
        if (getUser()!=null) {
            return getUser().getUserId();
        }
        return "";
    }

    public String getIdentityId() {
        if (getUser()!=null) {
            return getUser().getIdentityId();
        }
        return "";
    }

    public boolean isAccountNonExpired() {
    	if(user !=null){
    		return user.isAccountNonExpired();
    	}
    	return false;
    }

    public boolean isAccountNonLocked() {
    	if(user !=null){
    		return user.getUserState().isNonLocked();
    	}
    	return false;
    }
    
    public boolean isCredentialsNonExpired() {
    	// delegate to the application
        return true;
    }

    public boolean isEnabled() {
    	return isAccountNonLocked();
    }

    public String getPassword() {
    	if (secret !=null && secret.getIdentitySecret()!=null) {
    		return secret.getIdentitySecret();
    	}
        return "";
     }

    public String getUsername() {
    	if(user !=null && user.getUserName()!=null){
    		return user.getUserName();
    	}
        return "";
    }
    
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

    public UserDetailsAdapter updateAuthorities(List<GrantedAuthority> authorities) {
        setAuthorities(authorities);
        return this;
    }
    
    public Set<String> getAuthoritySet() {
        if (authorities!=null) {
            return AuthorityUtils.authorityListToSet(authorities);
        }
        return new HashSet<>();
    }
    
    public Locale getUserLocale() {
    	// TODO get the actual user locale
    	return Locale.getDefault();
    }
    
    /**
     * Convenience to retrieve user details from context.
     */
    public static UserDetailsAdapter getUserDetailsFromContext() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (authentication!=null) {
    		return (UserDetailsAdapter) authentication.getPrincipal();
    	}
    	return null;
    }
    
}
