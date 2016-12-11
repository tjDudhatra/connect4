/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.controller;

import com.tdudhatra.games.connect4.dto.UserDto;
import com.tdudhatra.games.connect4.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.transaction.Transactional;

/**
 * Controller for user related API endpoints.
 * 
 * @author tdudhatra
 */

@Controller
@Transactional
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserService userService;

  @PostMapping(value = "/register")
  public @ResponseBody UserDto registerUser(@RequestBody UserDto userDto) throws Exception {
    userDto = userService.registerUser(userDto);
    return userDto;
  }

  @PostMapping(value = "/login")
  public @ResponseBody UserDto login(
      @RequestParam(required = true, name = "username") String username,
      @RequestParam(required = true, name = "password") String password) throws Exception {
    return userService.login(username, password);
  }

  @GetMapping
  public @ResponseBody List<UserDto> listAllUsers() throws Exception {
    return userService.listAllUsers();
  }

}
