/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.factory;

import com.tdudhatra.games.connect4.dto.RoleDto;
import com.tdudhatra.games.connect4.dto.UserDto;
import com.tdudhatra.games.connect4.entity.Role;
import com.tdudhatra.games.connect4.entity.User;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Component class for building DTOs/Entities from Entities/DTOs.
 * 
 * @author tdudhatra
 */

@Component
public class UserObjectFactory implements ObjectFactory<UserDto, User> {

  @Override
  public UserDto buildDtoFromEntity(User user) {
    if (user == null) {
      return null;
    }

    UserDto userDto = new UserDto(user.getId(), user.getGuid(), user.getCreateTime(),
        user.getUpdateTime(), user.getUsername(), null, user.getUserFullName(), user.isEnabled(),
        this.buildRoleDtoListFromEntityList(user.getRoles()));

    return userDto;
  }

  @Override
  public User buildEntityFromDto(UserDto userDto) {
    if (userDto == null) {
      return null;
    }

    User user = new User();
    user.setId(userDto.getId());
    user.setGuid(userDto.getGuid());
    user.setCreateTime(userDto.getCreateTime());
    user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());
    user.setUserFullName(userDto.getUserFullName());
    user.setEnabled(userDto.isEnabled());

    user.setRoles(this.buildRoleEntityListFromDtoList(userDto.getRoles()));

    return user;
  }

  public List<UserDto> buildUserDtoListFromEntityList(List<User> users) {
    if (CollectionUtils.isEmpty(users)) {
      return new ArrayList<>();
    }

    List<UserDto> userDtos = new ArrayList<>();
    for (User user : users) {
      userDtos.add(this.buildDtoFromEntity(user));
    }

    return userDtos;
  }

  public List<User> buildUserEntityListFromDtoList(List<UserDto> userDtos) {
    if (CollectionUtils.isEmpty(userDtos)) {
      return new ArrayList<>();
    }

    List<User> users = new ArrayList<>();
    for (UserDto user : userDtos) {
      users.add(this.buildEntityFromDto(user));
    }

    return users;
  }

  public RoleDto buildRoleDtoFromEntity(Role role) {
    if (role == null) {
      return null;
    }

    RoleDto roleDto = new RoleDto(role.getId(), role.getGuid(), role.getCreateTime(),
        role.getUpdateTime(), role.getName());
    return roleDto;
  }

  public Role buildRoleEntityFromDto(RoleDto roleDto) {
    if (roleDto == null) {
      return null;
    }

    Role role = new Role();
    role.setId(roleDto.getId());
    role.setGuid(roleDto.getGuid());
    role.setCreateTime(roleDto.getCreateTime());
    role.setName(roleDto.getName());

    return role;
  }

  public List<RoleDto> buildRoleDtoListFromEntityList(List<Role> roles) {
    if (CollectionUtils.isEmpty(roles)) {
      return new ArrayList<>();
    }
    List<RoleDto> roleDtos = new ArrayList<>();

    for (Role role : roles) {
      roleDtos.add(this.buildRoleDtoFromEntity(role));
    }

    return roleDtos;
  }

  public List<Role> buildRoleEntityListFromDtoList(List<RoleDto> roleDtos) {
    if (CollectionUtils.isEmpty(roleDtos)) {
      return new ArrayList<>();
    }

    List<Role> roles = new ArrayList<>();

    for (RoleDto roleDto : roleDtos) {
      roles.add(this.buildRoleEntityFromDto(roleDto));
    }

    return roles;
  }

}
