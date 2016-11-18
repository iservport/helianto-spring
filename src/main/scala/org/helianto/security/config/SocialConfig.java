package org.helianto.security.config;

import org.helianto.security.domain.UserDetailsAdapter;
import org.helianto.security.service.SimpleSignInAdapter;
import org.helianto.security.service.UserSignInService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.*;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Provides APIs for facebook and google.
 */
@Configuration
@Import(GoogleAutoConfiguration.class)
public class SocialConfig implements SocialConfigurer {

    @Inject
    private DataSource dataSource;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new UserIdSource() {
            @Override
            public String getUserId() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) {
                    throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
                }
                return ((UserDetailsAdapter) authentication.getPrincipal()).getUserId()+"";
            }
        };
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository =
                new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        repository.setTablePrefix("core_");
        return repository;
    }

    /**
     * Sign in configuration.
     */
    @Configuration
//    @ConditionalOnProperty(prefix = "spring", name = "social")
//    @ConditionalOnBean({ConnectionFactoryLocator.class, UsersConnectionRepository.class})
    static class ProviderSignInConfigurer  {

        @Inject
        private UserSignInService userSignInService;

        /**
         * A controller to ccordinate the OAuth dance and sign-in the remote user locally.
         *
         * @param connectionFactoryLocator
         * @param usersConnectionRepository
         */
        @Bean
        public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator,
                                                                 UsersConnectionRepository usersConnectionRepository) {
            return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository,
                    new SimpleSignInAdapter(new HttpSessionRequestCache(), userSignInService));
        }

        /**
         * Utils to retrieve a connection from session and do post sign up tasks.
         *
         * @param connectionFactoryLocator
         * @param usersConnectionRepository
         */
        @Bean
        public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository){
            return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
        }

    }


}
