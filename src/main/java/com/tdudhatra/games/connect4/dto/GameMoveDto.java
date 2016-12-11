/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.dto;

import java.util.Date;

/**
 * DTO Representation of {@link com.tdudhatra.games.connect4.entity.GameMove}
 * 
 * @author tdudhatra
 */
public class GameMoveDto extends AbstractBaseDto {
  private static final long serialVersionUID = 7959961467806894895L;

  private Long gameId;

  private UserDto player;

  private Integer yIndex;

  private Integer xIndex;

  private Boolean isWinningMove;

  public GameMoveDto() {
    this(null, null, null, null, null, null, null, null, null);
  }

  public GameMoveDto(Long id, String guid, Date createTime, Date updateTime, Long gameId,
      UserDto player, Integer columnNo, Integer rowNo, Boolean isWinningMove) {
    super(id, guid, createTime, updateTime);
    this.gameId = gameId;
    this.player = player;
    this.yIndex = columnNo;
    this.xIndex = rowNo;
    this.isWinningMove = isWinningMove;
  }

  public Long getGameId() {
    return gameId;
  }

  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }

  public UserDto getPlayer() {
    return player;
  }

  public void setPlayer(UserDto player) {
    this.player = player;
  }

  public Integer getyIndex() {
    return yIndex;
  }

  public void setyIndex(Integer yIndex) {
    this.yIndex = yIndex;
  }

  public Integer getxIndex() {
    return xIndex;
  }

  public void setxIndex(Integer xIndex) {
    this.xIndex = xIndex;
  }

  public Boolean isWinningMove() {
    return isWinningMove;
  }

  public void setWinningMove(Boolean isWinningMove) {
    this.isWinningMove = isWinningMove;
  }

}
