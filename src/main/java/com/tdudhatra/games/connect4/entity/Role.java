/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.entity;

import javax.persistence.Entity;

/**
 * Entity to store user roles.
 * 
 * @author tdudhatra
 */
@Entity
public class Role extends AbstractBaseEntity {
  private static final long serialVersionUID = -2342901129379352305L;

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
