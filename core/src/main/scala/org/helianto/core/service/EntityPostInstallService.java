package org.helianto.core.service;


import org.helianto.core.domain.Entity;

public interface EntityPostInstallService {

    Entity entityPostInstall(Entity entity, String principal);

}
