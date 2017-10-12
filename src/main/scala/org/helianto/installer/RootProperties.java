package org.helianto.installer;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="helianto.root")
public class RootProperties {

    private Identity identity;

    private Entity entity;

    public Identity getIdentity() {
        return this.identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}