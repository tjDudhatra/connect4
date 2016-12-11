/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.repository;

import com.tdudhatra.games.connect4.entity.GameMoveHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link GameMoveHistory} entity.
 * 
 * @author tdudhatra
 */
@Repository
public interface GameMoveHistoryRepository extends JpaRepository<GameMoveHistory, Long> {

}
