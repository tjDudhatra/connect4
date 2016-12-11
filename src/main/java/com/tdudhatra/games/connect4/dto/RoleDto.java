/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.dto;

import com.tdudhatra.games.connect4.entity.Role;

import java.util.Date;

/**
 * DTO Representation of {@link Role}
 * 
 * @author tdudhatra
 */
public class RoleDto extends AbstractBaseDto {
  private static final long serialVersionUID = -6630076741752833774L;

  private String name;

  public RoleDto() {
    this(null, null, null, null, null);
  }

  public RoleDto(Long id, String guid, Date createTime, Date updateTime, String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
