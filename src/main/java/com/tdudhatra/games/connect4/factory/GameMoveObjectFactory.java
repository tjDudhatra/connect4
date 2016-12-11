/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.factory;

import com.tdudhatra.games.connect4.dto.GameMoveDto;
import com.tdudhatra.games.connect4.entity.GameMove;
import com.tdudhatra.games.connect4.repository.GameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Component class for building DTOs/Entities from Entities/DTOs.
 * 
 * @author tdudhatra
 */
@Component
public class GameMoveObjectFactory implements ObjectFactory<GameMoveDto, GameMove> {

  @Autowired
  UserObjectFactory userObjectFactory;

  @Autowired
  GameObjectFactory gameObjectFactory;

  @Autowired
  GameRepository gameRepository;

  @Override
  public GameMoveDto buildDtoFromEntity(GameMove gameMove) {
    if (gameMove == null) {
      return null;
    }

    GameMoveDto gameMoveDto = new GameMoveDto(gameMove.getId(), gameMove.getGuid(),
        gameMove.getCreateTime(), gameMove.getUpdateTime(), gameMove.getGame().getId(),
        userObjectFactory.buildDtoFromEntity(gameMove.getPlayer()), gameMove.getxIndex(),
        gameMove.getyIndex(), gameMove.isWinningMove());

    return gameMoveDto;
  }

  @Override
  public GameMove buildEntityFromDto(GameMoveDto gameMoveDto) {
    if (gameMoveDto == null) {
      return null;
    }

    GameMove gameMove = new GameMove();

    gameMove.setId(gameMoveDto.getId());
    gameMove.setGuid(gameMoveDto.getGuid());
    gameMove.setCreateTime(gameMoveDto.getCreateTime());
    gameMove.setxIndex(gameMoveDto.getxIndex());
    gameMove.setGame(gameRepository.findOne(gameMoveDto.getGameId()));
    gameMove.setPlayer(userObjectFactory.buildEntityFromDto(gameMoveDto.getPlayer()));
    gameMove.setyIndex(gameMoveDto.getyIndex());
    gameMove.setWinningMove(gameMoveDto.isWinningMove());

    return gameMove;
  }

  public List<GameMoveDto> buildDtoListFromEntityList(List<GameMove> moves) {
    if (CollectionUtils.isEmpty(moves)) {
      return new ArrayList<>();
    }

    List<GameMoveDto> moveDtos = new ArrayList<>();
    for (GameMove move : moves) {
      moveDtos.add(this.buildDtoFromEntity(move));
    }

    return moveDtos;
  }

}
