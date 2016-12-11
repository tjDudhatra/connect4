/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.controller;

import com.tdudhatra.games.connect4.dto.UserDto;
import com.tdudhatra.games.connect4.entity.User;
import com.tdudhatra.games.connect4.factory.Connect4TestDtoBuilder;
import com.tdudhatra.games.connect4.repository.UserRepository;
import com.tdudhatra.games.connect4.util.AppPropertyService;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * JUnit integration tests for {@link UserController}.
 * 
 * @author tdudhatra
 */
public class UserControllerIntegrationTest extends AbstractBaseTestCase {

  @Autowired
  UserController userController;

  @Autowired
  UserRepository userRepository;

  @Autowired
  AppPropertyService appPropertyService;

  /**
   * Positive test case to register user with all valid fields.
   * 
   * @throws Exception
   */
  @Test
  public void test1_registerUser() throws Exception {
    UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    userDto = userController.registerUser(userDto);

    Assert.assertNotNull(userDto);
    Assert.assertNotNull(userDto.getId());
    Assert.assertNotNull(userDto.getUsername());
    Assert.assertNotNull(userDto.getRoles());
    Assert.assertNull(userDto.getPassword()); // As password should not be
                                              // populated.
  }

  /**
   * Negative test case to register user. Checks for missing mandatory fields.
   * 
   * @throws Exception
   */
  @Test
  public void test2_registerUser() throws Exception {

    // Let's not pass username.
    UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    userDto.setUsername(null);
    try {
      userDto = userController.registerUser(userDto);
      Assert.fail("If this comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }

    // Let's not pass password.
    userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    userDto.setPassword(null);
    try {
      userDto = userController.registerUser(userDto);
      Assert.fail("If this comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }

    // Let's not pass user's full name.
    userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    userDto.setUserFullName(null);
    try {
      userDto = userController.registerUser(userDto);
      Assert.fail("If this comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  /**
   * Negative test case to register user. Checks for duplicate username.
   * 
   * @throws Exception
   */
  @Test
  public void test3_registerUser() throws Exception {
    UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    userDto = userController.registerUser(userDto);

    UserDto duplicateUserDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    duplicateUserDto.setUsername(userDto.getUsername());
    try {
      duplicateUserDto = userController.registerUser(duplicateUserDto);
      Assert.fail("If this comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  /**
   * Positive test case to validate the login functionality.
   * 
   * @throws Exception
   */
  @Test
  public void test1_login() throws Exception {
    UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    String userPassword = userDto.getPassword();
    userDto = userController.registerUser(userDto);

    userDto = userController.login(userDto.getUsername(), userPassword);
    Assert.assertNotNull(userDto);
    Assert.assertNotNull(userDto.getId());
    Assert.assertNotNull(userDto.getUserFullName());
    Assert.assertNotNull(userDto.getUsername());
    Assert.assertNotNull(userDto.getRoles());
    Assert.assertNull(userDto.getPassword()); // As password should not be
                                              // populated.

  }

  /**
   * Negative test case to validate the login functionality. Checks with invalid
   * username/password.
   * 
   * @throws Exception
   */
  @Test
  public void test2_login() throws Exception {
    UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    String userPassword = userDto.getPassword();
    userDto = userController.registerUser(userDto);

    // Let's pass the valid username and wrong password.
    try {
      userDto = userController.login(userDto.getUsername(), userPassword + "fail");
      Assert.fail("If this comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }

    // Let's pass the valid password and wrong username.
    try {
      userDto = userController.login(userDto.getUsername() + "fail", userPassword);
      Assert.fail("If this comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }

    // Let's not pass the password with valid username.
    try {
      userDto = userController.login(userDto.getUsername(), null);
      Assert.fail("If this comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }

  }

  /**
   * Positive test case to validate listAllUsers. Check with proper
   * authentication.
   * 
   * @throws Exception
   */
  @Test
  public void test1_listAllUsers() throws Exception {

    Authentication auth = Mockito.mock(Authentication.class);
    Mockito.when(auth.getPrincipal()).thenReturn(appPropertyService.getAppAdminUsername());
    SecurityContextHolder.getContext().setAuthentication(auth);

    List<UserDto> userDtos = userController.listAllUsers();
    Assert.assertNotNull(userDtos);

    List<User> users = userRepository.findAll();
    Assert.assertNotNull(users);

    Assert.assertTrue(users.size() == userDtos.size());
  }

  /**
   * Positive test case to validate listAllUsers. Check with without
   * authentication.
   * 
   * @throws Exception
   */
  @Test
  public void test2_listAllUsers() throws Exception {
    try {
      userController.listAllUsers();
      Assert.fail("If this comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

}
