/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Entity to store all the moves of players for particular game. Moves for
 * particular game will be stored in this table only till the game is not
 * finished/destroyed and after that these all moves will be moved into the
 * {@link GameMoveHistory} table.
 * 
 * @author tdudhatra
 */
@Entity
public class GameMove extends AbstractBaseEntity {
  private static final long serialVersionUID = 7959961467806894895L;

  @ManyToOne
  private Game game;

  @ManyToOne
  private User player;

  private int xIndex;

  private int yIndex;

  private boolean isWinningMove;

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public User getPlayer() {
    return player;
  }

  public void setPlayer(User player) {
    this.player = player;
  }

  public int getxIndex() {
    return xIndex;
  }

  public void setxIndex(int xIndex) {
    this.xIndex = xIndex;
  }

  public int getyIndex() {
    return yIndex;
  }

  public void setyIndex(int yIndex) {
    this.yIndex = yIndex;
  }

  public boolean isWinningMove() {
    return isWinningMove;
  }

  public void setWinningMove(boolean isWinningMove) {
    this.isWinningMove = isWinningMove;
  }

}
