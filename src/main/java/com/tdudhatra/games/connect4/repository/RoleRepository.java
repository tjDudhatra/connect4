/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.repository;

import com.tdudhatra.games.connect4.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link Role} entity.
 * 
 * @author tdudhatra
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  @Query("select obj from Role obj where obj.name = :userRoleName")
  public Role findByRoleName(@Param("userRoleName") String userRoleName);

}
