/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.dto;

import java.util.Date;

/**
 * DTO Representation of
 * {@link com.tdudhatra.games.connect4.entity.GameMoveHistory}
 * 
 * @author tdudhatra
 */
public class GameMoveHistoryDto extends AbstractBaseDto {
  private static final long serialVersionUID = -7677658982161433540L;

  private GameDto game;

  private String movesJson;

  public GameMoveHistoryDto() {
    this(null, null, null, null, null, null);
  }

  public GameMoveHistoryDto(Long id, String guid, Date createTime,
      Date updateTime, GameDto game, String movesJson) {
    super(id, guid, createTime, updateTime);
    this.game = game;
    this.movesJson = movesJson;
  }

  public GameDto getGame() {
    return game;
  }

  public void setGame(GameDto game) {
    this.game = game;
  }

  public String getMovesJson() {
    return movesJson;
  }

  public void setMovesJson(String movesJson) {
    this.movesJson = movesJson;
  }

}
