/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.entity;

import com.tdudhatra.games.connect4.util.EnumConstants.GameDestroyReason;
import com.tdudhatra.games.connect4.util.EnumConstants.GameStatus;
import com.tdudhatra.games.connect4.util.EnumConstants.GameWinningReason;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Entity to store game details played/started by the players.
 * 
 * @author tdudhatra
 */
@Entity
public class Game extends AbstractBaseEntity {
  private static final long serialVersionUID = 4865588196100576667L;

  @ManyToOne
  private User player1;

  @ManyToOne
  private User player2;

  @ManyToOne
  private User gameStartedByUser;

  private GameStatus gameStatus;

  @ManyToOne
  private User winnerPlayer;

  private GameWinningReason winningReason;

  private Date gameWinningTime;

  @ManyToOne
  private User destroyerPlayer;

  private GameDestroyReason destroyReason;

  @OneToMany(mappedBy = "game")
  private List<GameMove> moves;

  public User getPlayer1() {
    return player1;
  }

  public void setPlayer1(User player1) {
    this.player1 = player1;
  }

  public User getPlayer2() {
    return player2;
  }

  public void setPlayer2(User player2) {
    this.player2 = player2;
  }

  public User getGameStartedByUser() {
    return gameStartedByUser;
  }

  public void setGameStartedByUser(User gameStartedBy) {
    this.gameStartedByUser = gameStartedBy;
  }

  public GameStatus getGameStatus() {
    return gameStatus;
  }

  public void setGameStatus(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  public User getWinnerPlayer() {
    return winnerPlayer;
  }

  public void setWinnerPlayer(User winnerPlayer) {
    this.winnerPlayer = winnerPlayer;
  }

  public GameWinningReason getWinningReason() {
    return winningReason;
  }

  public void setWinningReason(GameWinningReason winningReason) {
    this.winningReason = winningReason;
  }

  public Date getGameWinningTime() {
    return gameWinningTime;
  }

  public void setGameWinningTime(Date gameWinningTime) {
    this.gameWinningTime = gameWinningTime;
  }

  public User getDestroyerPlayer() {
    return destroyerPlayer;
  }

  public void setDestroyerPlayer(User destroyerPlayer) {
    this.destroyerPlayer = destroyerPlayer;
  }

  public GameDestroyReason getDestroyReason() {
    return destroyReason;
  }

  public void setDestroyReason(GameDestroyReason destroyReason) {
    this.destroyReason = destroyReason;
  }

  public List<GameMove> getMoves() {
    return moves;
  }

  public void setMoves(List<GameMove> moves) {
    this.moves = moves;
  }

}
