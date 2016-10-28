package org.helianto.core.service;


import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;

public interface EntityPostInstallService {

    Entity entityPostInstall(Entity entity, Identity identity);

}
