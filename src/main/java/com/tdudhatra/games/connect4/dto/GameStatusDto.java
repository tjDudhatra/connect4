/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.dto;

import com.tdudhatra.games.connect4.util.EnumConstants.GameStatus;

import java.util.List;

/**
 * DTO that will represent the current status of game.
 * 
 * @author tdudhatra
 */
public class GameStatusDto {

  private GameDto gameDto;

  private List<GameMoveDto> playableMoves;

  private List<GameMoveDto> playedMoves;

  private List<GameMoveDto> neitherPlayedNorPlayableMoves;

  private GameStatus gameStatus;

  private String message;

  public GameStatusDto() {
    this(null, null, null, null, null, null);
  }

  public GameStatusDto(GameDto gameDto, List<GameMoveDto> playableMoves,
      List<GameMoveDto> playedMoves, List<GameMoveDto> neitherPlayedNorPlayableMoves,
      GameStatus gameStatus, String message) {
    super();
    this.gameDto = gameDto;
    this.playableMoves = playableMoves;
    this.playedMoves = playedMoves;
    this.neitherPlayedNorPlayableMoves = neitherPlayedNorPlayableMoves;
    this.gameStatus = gameStatus;
    this.message = message;
  }

  public GameDto getGameDto() {
    return gameDto;
  }

  public void setGameDto(GameDto gameDto) {
    this.gameDto = gameDto;
  }

  public List<GameMoveDto> getPlayedMoves() {
    return playedMoves;
  }

  public void setPlayedMoves(List<GameMoveDto> playedMoves) {
    this.playedMoves = playedMoves;
  }

  public List<GameMoveDto> getPlayableMoves() {
    return playableMoves;
  }

  public void setPlayableMoves(List<GameMoveDto> playableMoves) {
    this.playableMoves = playableMoves;
  }

  public List<GameMoveDto> getNeitherPlayedNorPlayableMoves() {
    return neitherPlayedNorPlayableMoves;
  }

  public void setNeitherPlayedNorPlayableMoves(List<GameMoveDto> neitherPlayedNorPlayableMoves) {
    this.neitherPlayedNorPlayableMoves = neitherPlayedNorPlayableMoves;
  }

  public GameStatus getGameStatus() {
    return gameStatus;
  }

  public void setGameStatus(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
