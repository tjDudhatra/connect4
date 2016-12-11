/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.entity;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

/**
 * User entity stores login information of users including roles.
 * 
 * @author tdudhatra
 */
@Entity
public class User extends AbstractBaseEntity {
  private static final long serialVersionUID = 2449195615262298295L;

  @NotEmpty
  @Column(unique = true)
  private String username;

  @NotEmpty
  private String password;

  @NotEmpty
  private String userFullName;

  private boolean enabled;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Role> roles;

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

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
