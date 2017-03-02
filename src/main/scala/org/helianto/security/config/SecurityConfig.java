package org.helianto.security.config;

import org.helianto.security.service.CustomTokenEnhancer;
import org.helianto.security.service.HeliantoTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.*;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Arrays;

@Configuration
@EnableWebSecurity //(debug = true)
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Order(6)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static int REMEMBER_ME_DEFAULT_DURATION = 14*24*60*60; // duas semanas

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    @Qualifier("preAuthProvider")
    private AuthenticationProvider preAuthProvider;

    /**
     * Authorization server
     */
    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

        @Value("${helianto.host}")
        private String heliantoHost = "";

        @Value("${key-store.password}")
        private String keyStorePassword = "";

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private CustomTokenEnhancer tokenEnhancer;

        /**
         * Configure the endpoints to collaborate with the authentication manager
         * and convert tokens to and from JWT format.
         *
         * @param endpoints
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .authenticationManager(authenticationManager).userDetailsService(userDetailsService) // very important
                    .accessTokenConverter(accessTokenConverter())
                    .tokenEnhancer(tokenEnhancerChain());
        }

        // Token Enhancer to be used to configure endpoints
        private TokenEnhancerChain tokenEnhancerChain() {
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            tokenEnhancerChain.setTokenEnhancers(
                    Arrays.asList(tokenEnhancer, accessTokenConverter()));
            return tokenEnhancerChain;
        }

        // key pair to be used by accessTokenConverter
        private KeyPair getKeyPair() {
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                    new ClassPathResource("jwt.jks"),
                    keyStorePassword.toCharArray());
            return keyStoreKeyFactory.getKeyPair("jwt");
        }

        @Bean
        JwtAccessTokenConverter accessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setKeyPair(getKeyPair());
            return converter;
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.allowFormAuthenticationForClients()
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()")
            ;
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                .withClient("helianto-client")
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                    .authorities("ROLE_USER_READ") // roles granted to this client
                    .scopes("read", "write", "trust")
                    .autoApprove(true)
                    .accessTokenValiditySeconds(30)
                    .refreshTokenValiditySeconds(3600)
            ;
        }

    }

    // end of Authorization server config

    /**
     * Resource server
     */
    @Configuration
    @EnableResourceServer
    static class ResourceServer extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.requestMatcher(new OAuthRequestedMatcher())
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        private static class OAuthRequestedMatcher implements RequestMatcher {
            public boolean matches(HttpServletRequest request) {
                String auth = request.getHeader("Authorization");
                // Determine if the client request contained an OAuth Authorization
                boolean haveOauth2Token = (auth != null) && auth.toLowerCase().startsWith("bearer");
                boolean haveAccessToken = request.getParameter("access_token")!=null;
                return haveOauth2Token || haveAccessToken;
            }
        }

//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            // @formatter:off
//            http.antMatcher("/api/**")
//                    .authorizeRequests().anyRequest().access("#oauth2.isUser()")

//                .and().csrf().disable()
//                    .exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//            ;
//            // @formatter:on
//        }
//
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/webjars/**", "/api/insecure/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
            .authorizeRequests()
                .antMatchers("/invite/**").permitAll()
                .antMatchers("/**").hasRole("USER_READ")
            .and().exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            .and().formLogin()
                .usernameParameter("principal").loginPage("/login").permitAll()
            .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
                .csrf().csrfTokenRepository(csrfTokenRepository())
                .ignoringAntMatchers("/app/content/upload", "/login/**", "/oauth/**")
            .and().logout()
                .deleteCookies("JSESSIONID")
                .deleteCookies("remember-me")
                .deleteCookies("X-XSRF-TOKEN")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider).authenticationProvider(preAuthProvider);
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CSRF header filter.
     */
    private static Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                        .getName());
                if (csrf != null) {
                    Cookie cookie = new Cookie("XSRF-TOKEN", csrf.getToken());
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    /**
     * CSRF token repository.
     */
    private static CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    /**
     * Persistent token repository.
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

}
