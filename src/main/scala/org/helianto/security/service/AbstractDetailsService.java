package org.helianto.security.service;

import org.helianto.security.domain.Secret;
import org.helianto.security.repository.SecretRepository;
import org.helianto.user.repository.UserProjection;
import org.helianto.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Base class to security details services.
 * 
 * @author mauriciofernandesdecastro
 */
public class AbstractDetailsService {

    protected static Logger logger = LoggerFactory.getLogger(AbstractDetailsService.class);
    
	@Autowired
    private SecretRepository secretRepository;

	@Autowired
	protected UserRepository userGroupRepository;

	/**
	 * Step 1: retrieve the identity secret.
	 * 
	 * @param identityKey
	 *
	 * @throws UsernameNotFoundException
	 * @throws DataAccessException
	 */
	@Transactional
	protected Secret loadIdentitySecretByKey(String identityKey) throws UsernameNotFoundException, DataAccessException {

		Secret secret = secretRepository.findByIdentityKeyOrEmail(identityKey);

		if (secret !=null) {
			return secret;
		}
		
		logger.error("Unable to load by user name with {}.", identityKey);
		throw new UsernameNotFoundException("Unable to find user name for "+identityKey);
		
	}
	
	/**
	 * Step 2: list users given the identity.
	 * 
	 * @param identityId
	 */
	protected List<UserProjection> loadUserListByIdentityId(int identityId) {
		List<UserProjection> userList = userGroupRepository.findByIdentity_IdOrderByLastEventDesc(identityId);
		
		if (userList!=null && userList.size()>0) {
			logger.debug("Found {} user(s) matching id={}.", userList.size(), identityId);
			return userList;
		}

		logger.error("Unable to load by user list");
		throw new UsernameNotFoundException("Unable to find any user for identity id "+identityId);
	}
	
}
