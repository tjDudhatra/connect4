/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Once the game has been finished/destroyed, after that all the moves of that
 * game will be dumped into this table. This table should be cleaned regularly
 * by moving the not required history data into nosql storage or hdfc/hive/hbase
 * to avoid storage issues.
 * 
 * @author tdudhatra
 */
@Entity
public class GameMoveHistory extends AbstractBaseEntity {
  private static final long serialVersionUID = -7677658982161433540L;

  @OneToOne
  private Game game;

  @Type(type = "text")
  private String movesJson;

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public String getMovesJson() {
    return movesJson;
  }

  public void setMovesJson(String movesJson) {
    this.movesJson = movesJson;
  }

}
