/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.dto;

import com.tdudhatra.games.connect4.util.EnumConstants.GameDestroyReason;
import com.tdudhatra.games.connect4.util.EnumConstants.GameStatus;
import com.tdudhatra.games.connect4.util.EnumConstants.GameWinningReason;

import java.util.Date;
import java.util.List;

/**
 * DTO Representation of {@link com.tdudhatra.games.connect4.entity.Game}
 * 
 * @author tdudhatra
 */
public class GameDto extends AbstractBaseDto {
  private static final long serialVersionUID = 4865588196100576667L;

  private UserDto player1;

  private UserDto player2;

  private UserDto gameStartedByUser;

  private GameStatus gameStatus;

  private UserDto winnerPlayer;

  private GameWinningReason winningReason;

  private Date gameWinningTime;

  private UserDto destroyerPlayer;

  private GameDestroyReason destroyReason;

  private List<GameMoveDto> moves;

  public GameDto() {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  public GameDto(Long id, String guid, Date createTime, Date updateTime, UserDto player1,
      UserDto player2, UserDto gameStartedByUser, GameStatus gameStatus, UserDto winnerPlayer,
      GameWinningReason winningReason, Date gameWinningTime, UserDto destroyerPlayer,
      GameDestroyReason destroyReason, List<GameMoveDto> moves) {
    super(id, guid, createTime, updateTime);
    this.player1 = player1;
    this.player2 = player2;
    this.gameStartedByUser = gameStartedByUser;
    this.gameStatus = gameStatus;
    this.winnerPlayer = winnerPlayer;
    this.winningReason = winningReason;
    this.gameWinningTime = gameWinningTime;
    this.destroyerPlayer = destroyerPlayer;
    this.destroyReason = destroyReason;
    this.moves = moves;
  }

  public UserDto getPlayer1() {
    return player1;
  }

  public void setPlayer1(UserDto player1) {
    this.player1 = player1;
  }

  public UserDto getPlayer2() {
    return player2;
  }

  public void setPlayer2(UserDto player2) {
    this.player2 = player2;
  }

  public UserDto getGameStartedByUser() {
    return gameStartedByUser;
  }

  public void setGameStartedByUser(UserDto gameStartedByUser) {
    this.gameStartedByUser = gameStartedByUser;
  }

  public GameStatus getGameStatus() {
    return gameStatus;
  }

  public void setGameStatus(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  public UserDto getWinnerPlayer() {
    return winnerPlayer;
  }

  public void setWinnerPlayer(UserDto winnerPlayer) {
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

  public UserDto getDestroyerPlayer() {
    return destroyerPlayer;
  }

  public void setDestroyerPlayer(UserDto destroyerPlayer) {
    this.destroyerPlayer = destroyerPlayer;
  }

  public GameDestroyReason getDestroyReason() {
    return destroyReason;
  }

  public void setDestroyReason(GameDestroyReason destroyReason) {
    this.destroyReason = destroyReason;
  }

  public List<GameMoveDto> getMoves() {
    return moves;
  }

  public void setMoves(List<GameMoveDto> moves) {
    this.moves = moves;
  }

}
