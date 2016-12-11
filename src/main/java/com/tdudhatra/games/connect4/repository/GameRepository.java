/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.repository;

import com.tdudhatra.games.connect4.entity.Game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link Game} entity.
 * 
 * @author tdudhatra
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
