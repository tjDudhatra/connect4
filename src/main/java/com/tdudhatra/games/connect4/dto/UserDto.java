/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.dto;

import java.util.Date;
import java.util.List;

/**
 * DTO Representation of {@link com.tdudhatra.games.connect4.entity.User}
 * 
 * @author tdudhatra
 */
public class UserDto extends AbstractBaseDto {
  private static final long serialVersionUID = 2449195615262298295L;

  private String username;

  private String password;

  private String userFullName;

  private Boolean enabled;

  private List<RoleDto> roles;

  public UserDto() {
    this(null, null, null, null, null, null, null, null, null);
  }

  public UserDto(Long id, String guid, Date createTime, Date updateTime,
      String emailId, String password, String userFullName, Boolean enabled, List<RoleDto> roles) {
    super(id, guid, createTime, updateTime);
    this.username = emailId;
    this.password = password;
    this.userFullName = userFullName;
    this.enabled = enabled;
    this.roles = roles;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUserFullName() {
    return userFullName;
  }

  public void setUserFullName(String userFullName) {
    this.userFullName = userFullName;
  }

  public Boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public List<RoleDto> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleDto> roles) {
    this.roles = roles;
  }

}
