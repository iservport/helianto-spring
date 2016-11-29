package org.helianto.core.service;

import org.helianto.core.domain.Identity;

public interface IdentityPostInstallService {

    Identity identityPostInstall(Identity identity, String password);

}
