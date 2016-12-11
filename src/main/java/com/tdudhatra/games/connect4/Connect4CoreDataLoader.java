/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4;

import com.tdudhatra.games.connect4.entity.Role;
import com.tdudhatra.games.connect4.entity.User;
import com.tdudhatra.games.connect4.repository.RoleRepository;
import com.tdudhatra.games.connect4.repository.UserRepository;
import com.tdudhatra.games.connect4.util.AppPropertyService;
import com.tdudhatra.games.connect4.util.EnumConstants.UserRoleEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * This class is responsible for loading core data like role definitions, users
 * etc.
 * 
 * @author tdudhatra
 */
@Component
public class Connect4CoreDataLoader implements ApplicationRunner {

  @Autowired
  AppPropertyService appPropertyService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * This method will do all the required things to load the core data.
   */
  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {
    String createDataFlag = appPropertyService.getSpringJpaHibernateDdlAuto();

    if (!createDataFlag.equalsIgnoreCase("create")
        && !createDataFlag.equalsIgnoreCase("create-drop")) {
      return;
    }

    this.createRoleDefinitions();
    this.createAdminUser();
  }

  private void createAdminUser() {
    String usernameToCreate = appPropertyService.getAppAdminUsername();

    User user = userRepository.findByUsername(usernameToCreate);
    if (user == null) {
      user = new User();
    }
    user.setUsername(usernameToCreate);
    user.setEnabled(true);
    user.setPassword(bCryptPasswordEncoder.encode(appPropertyService.getAppAdminUserPassword()));
    user.setUserFullName(user.getUsername());
    user.setRoles(roleRepository.findAll());

    user = userRepository.save(user);
  }

  private void createRoleDefinitions() {
    for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
      Role role = roleRepository.findByRoleName(userRoleEnum.name());
      if (role != null) {
        continue;
      }
      role = new Role();
      role.setName(userRoleEnum.name());
      role = roleRepository.save(role);
    }
  }

}
