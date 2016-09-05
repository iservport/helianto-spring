package org.helianto.security.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.helianto.security.domain.Secret;
import org.helianto.security.domain.UserAuthority;
import org.helianto.security.domain.UserDetailsAdapter;
import org.helianto.security.repository.UserAuthorityProjection;
import org.helianto.security.repository.UserAuthorityRepository;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Return an <code>UserDetails</code> instance.
 * 
 * @author mauriciofernandesdecastro
 */
@Service("userDetailsService")
@Qualifier("default")
public class UserDetailsServiceImpl
	extends AbstractDetailsService
	implements UserDetailsService 
{

	@Autowired
	private UserAuthorityRepository userAuthorityRepository;

	@Transactional
	public UserDetails loadUserByUsername(String userKey) throws UsernameNotFoundException, DataAccessException {

		Secret secret = loadIdentitySecretByKey(userKey);
		List<UserProjection> userAdapterList = loadUserListByIdentityId(secret.getIdentity().getId());
				
		if (userAdapterList!=null && userAdapterList.size()>0) {
			// first userId in the list is the last logged one
			UserProjection userReadAdapter = userAdapterList.get(0);
			UserDetailsAdapter userDetails = new UserDetailsAdapter(userReadAdapter, secret);
			
			// update the last event date
			User user = (User) userGroupRepository.findOne(userReadAdapter.getUserId());
			user.setLastEvent(new Date());
			user = userGroupRepository.saveAndFlush(user);
			
			// grant the roles
			return updateAuthorities(userDetails);
		}
		else {
			logger.info("User list has no valid users.");
		}
		throw new UsernameNotFoundException("Unable to extract valid user from a list.");
	}

	/**
	 * Updates authorities for the given user.
	 *
	 * @param userDetailsAdapter
	 */
	public UserDetailsAdapter updateAuthorities(UserDetailsAdapter userDetailsAdapter) {
        List<Integer> parentGroupIds = userGroupRepository.findParentsByChildId(userDetailsAdapter.getUser().getUserId());
		return updateAuthorities(userDetailsAdapter, parentGroupIds);
	}

	/**
	 * List parent groups.
	 *
	 * @param userId
	 */
	public List<Integer> listParentGxroups(int userId) {
		return userGroupRepository.findParentsByChildId(userId);
	}

	/**
	 * Updates authorities for the given user.
	 *
	 * @param userDetailsAdapter
	 * @param parentGroups
	 */
	public UserDetailsAdapter updateAuthorities(UserDetailsAdapter userDetailsAdapter, List<Integer> parentGroups) {
		List<UserAuthorityProjection> adapterList = userAuthorityRepository.findByUserGroupIdOrderByServiceCodeAsc(parentGroups);
		List<String> roleNames = UserAuthority.getRoleNames(adapterList, userDetailsAdapter.getUser().getIdentityId());

		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String roleName: roleNames) {
			authorities.add(new SimpleGrantedAuthority(roleName));
			logger.info("Granted authority: {}.", roleName);
		}
		userDetailsAdapter.setAuthorities(authorities);
		return userDetailsAdapter;
	}

}
