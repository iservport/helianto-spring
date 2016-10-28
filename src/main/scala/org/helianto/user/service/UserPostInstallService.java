package org.helianto.user.service;


import org.helianto.user.domain.User;

public interface UserPostInstallService {

    User systemPostInstall(User group);

    User userPostInstall(User user);

}
