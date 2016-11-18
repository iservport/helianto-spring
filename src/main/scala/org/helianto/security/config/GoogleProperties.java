package org.helianto.security.config;

import org.springframework.boot.autoconfigure.social.SocialProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for Spring Social Facebook.
 *
 * @author Stephane Nicoll
 * @since 1.2.0
 */
@ConfigurationProperties("spring.social.google")
public class GoogleProperties extends SocialProperties {

}
