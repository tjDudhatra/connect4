/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.repository;

import com.tdudhatra.games.connect4.entity.Game;
import com.tdudhatra.games.connect4.entity.GameMove;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for {@link GameMove} entity.
 * 
 * @author tdudhatra
 */
@Repository
public interface GameMoveRepository extends JpaRepository<GameMove, Long> {

  @Query("select obj from GameMove obj where obj.game = :game")
  public List<GameMove> findLatestMoveForTheGame(@Param("game") Game game, Pageable pageable);

  @Query("select obj from GameMove obj where obj.game = :game and obj.xIndex = :xIndex")
  public List<GameMove> findLatestMoveForTheGivenColumnForTheGame(@Param("game") Game game,
      @Param("xIndex") int xIndex, Pageable pageable);

  public List<GameMove> findByGame(Game game);

}
