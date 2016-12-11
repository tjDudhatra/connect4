/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.service;

import com.tdudhatra.games.connect4.config.Connect4UserDetailsService;
import com.tdudhatra.games.connect4.config.SpringSecurityService;
import com.tdudhatra.games.connect4.dto.UserDto;
import com.tdudhatra.games.connect4.entity.Role;
import com.tdudhatra.games.connect4.entity.User;
import com.tdudhatra.games.connect4.factory.UserObjectFactory;
import com.tdudhatra.games.connect4.repository.RoleRepository;
import com.tdudhatra.games.connect4.repository.UserRepository;
import com.tdudhatra.games.connect4.util.CollectionUtils;
import com.tdudhatra.games.connect4.util.EnumConstants.UserRoleEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Service class that contains business logic for user related APIs.
 * 
 * @author tdudhatra
 */
@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserObjectFactory userObjectFactory;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  SpringSecurityService springSecurityService;

  @Autowired
  Connect4UserDetailsService connect4UserDetailsService;

  public UserDto registerUser(UserDto userDto) throws Exception {

    if (userDto == null) {
      throw new Exception("No User data found to create.");
    }

    String usernameToBeCreated = userDto.getUsername();

    if (StringUtils.isEmpty(usernameToBeCreated) || StringUtils.isEmpty(userDto.getPassword())
        || StringUtils.isEmpty(userDto.getUserFullName())) {
      throw new Exception(
          "Username, password, user's fullName are the mandatory fields, can not be empty.");
    }

    User existingUser = userRepository.findByUsername(usernameToBeCreated);
    if (existingUser != null) {
      throw new Exception(
          "User already exists with the same username: [ " + usernameToBeCreated + " ]");
    }

    String plainPwd = userDto.getPassword();
    String encryptedPwd = bCryptPasswordEncoder.encode(userDto.getPassword());

    User user = new User();
    user.setEnabled(true);
    user.setUsername(usernameToBeCreated);
    user.setPassword(encryptedPwd);
    user.setUserFullName(userDto.getUserFullName());

    Role userRole = roleRepository.findByRoleName(UserRoleEnum.ROLE_USER.name());
    user.setRoles(Arrays.asList(userRole));

    user = userRepository.save(user);

    userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    List<Role> userRoles = user.getRoles();
    userDto.setRoles(userObjectFactory.buildRoleDtoListFromEntityList(userRoles));

    springSecurityService.login(usernameToBeCreated, plainPwd);

    return userDto;
  }

  public UserDto findByUsername(String username) {
    return userObjectFactory.buildDtoFromEntity(userRepository.findByUsername(username));
  }

  public List<UserDto> listAllUsers() throws Exception {
    connect4UserDetailsService.getLoggedInUserOrThrow();
    return userObjectFactory.buildUserDtoListFromEntityList(userRepository.findAll());
  }

  public User getRandomUserFromDatabaseToJoinTheGame(String excludeUsername) {
    List<User> allUsers = userRepository.findAll();
    allUsers.removeIf(x -> x.getUsername().equals(excludeUsername));
    User randomUser = CollectionUtils.getRandomItem(allUsers);
    return randomUser;
  }

  public UserDto login(String username, String password) {
    springSecurityService.login(username, password);
    return userObjectFactory.buildDtoFromEntity(userRepository.findByUsername(username));
  }

}
