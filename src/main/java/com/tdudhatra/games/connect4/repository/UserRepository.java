/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.repository;

import com.tdudhatra.games.connect4.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link User} entity.
 * 
 * @author tdudhatra
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  public User findByUsername(String username);
}
