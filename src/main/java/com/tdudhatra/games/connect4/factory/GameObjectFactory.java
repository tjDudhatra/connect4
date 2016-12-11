/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.factory;

import com.tdudhatra.games.connect4.dto.GameDto;
import com.tdudhatra.games.connect4.dto.GameMoveDto;
import com.tdudhatra.games.connect4.dto.GameStatusDto;
import com.tdudhatra.games.connect4.entity.Game;
import com.tdudhatra.games.connect4.entity.GameMove;
import com.tdudhatra.games.connect4.repository.GameMoveRepository;
import com.tdudhatra.games.connect4.util.AppPropertyService;
import com.tdudhatra.games.connect4.util.EnumConstants.GameStatus;
import com.tdudhatra.games.connect4.util.EnumConstants.TypeOfMove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Component class for building DTOs/Entities from Entities/DTOs.
 * 
 * @author tdudhatra
 */
@Component
public class GameObjectFactory implements ObjectFactory<GameDto, Game> {

  @Autowired
  UserObjectFactory userObjectFactory;

  @Autowired
  GameMoveObjectFactory gameMoveObjectFactory;

  @Autowired
  AppPropertyService appPropertyService;

  @Autowired
  GameMoveRepository gameMoveRepository;

  @Override
  public GameDto buildDtoFromEntity(Game game) {
    if (game == null) {
      return null;
    }

    GameDto gameDto = new GameDto(game.getId(), game.getGuid(), game.getCreateTime(),
        game.getUpdateTime(), userObjectFactory.buildDtoFromEntity(game.getPlayer1()),
        userObjectFactory.buildDtoFromEntity(game.getPlayer2()),
        userObjectFactory.buildDtoFromEntity(game.getGameStartedByUser()), game.getGameStatus(),
        userObjectFactory.buildDtoFromEntity(game.getWinnerPlayer()), game.getWinningReason(),
        game.getGameWinningTime(), userObjectFactory.buildDtoFromEntity(game.getDestroyerPlayer()),
        game.getDestroyReason(), gameMoveObjectFactory.buildDtoListFromEntityList(game.getMoves()));
    return gameDto;
  }

  @Override
  public Game buildEntityFromDto(GameDto gameDto) {
    if (gameDto == null) {
      return null;
    }

    Game game = new Game();
    game.setId(gameDto.getId());
    game.setGuid(gameDto.getGuid());
    game.setCreateTime(gameDto.getCreateTime());
    game.setDestroyerPlayer(userObjectFactory.buildEntityFromDto(gameDto.getDestroyerPlayer()));
    game.setDestroyReason(gameDto.getDestroyReason());
    game.setGameStartedByUser(userObjectFactory.buildEntityFromDto(gameDto.getGameStartedByUser()));
    game.setGameStatus(gameDto.getGameStatus());
    game.setGameWinningTime(gameDto.getGameWinningTime());
    game.setPlayer1(userObjectFactory.buildEntityFromDto(gameDto.getPlayer1()));
    game.setPlayer2(userObjectFactory.buildEntityFromDto(gameDto.getPlayer2()));
    game.setWinnerPlayer(userObjectFactory.buildEntityFromDto(gameDto.getWinnerPlayer()));
    game.setWinningReason(gameDto.getWinningReason());

    return game;
  }

  public GameStatusDto buildGameStatusDto(Game game) throws Exception {
    if (game == null) {
      return null;
    }

    GameDto gameDto = this.buildDtoFromEntity(game);

    // As game is not in started mode, then no moves information will be
    // required.
    if (game.getGameStatus() != GameStatus.GAME_STARTED_AND_RUNNING) {
      return new GameStatusDto(gameDto, null, null, null, game.getGameStatus(),
          game.getGameStatus().name());
    }

    Map<TypeOfMove, List<GameMoveDto>> moveTypeAndMovesMap = getAllTypeOfMovesForTheGame(game);

    if (moveTypeAndMovesMap == null) {
      throw new Exception("Caught unknown server error.");
    }

    List<GameMoveDto> playedMoves = moveTypeAndMovesMap.get(TypeOfMove.PLAYED_MOVE);
    List<GameMoveDto> playableMoves = moveTypeAndMovesMap.get(TypeOfMove.PLAYABLE_MOVE);
    List<GameMoveDto> neitherPlayedNorPlayableMoves = moveTypeAndMovesMap
        .get(TypeOfMove.NEITHER_PLAYED_NOR_PLAYABLE_MOVE);

    GameStatusDto gameStatusDto = new GameStatusDto(gameDto, playableMoves, playedMoves,
        neitherPlayedNorPlayableMoves, game.getGameStatus(), game.getGameStatus().name());

    return gameStatusDto;
  }

  public Map<TypeOfMove, List<GameMoveDto>> getAllTypeOfMovesForTheGame(Game game)
      throws Exception {

    int columnsOnBoard = appPropertyService.getNoOfColumnsOnBoard();
    int rowsOnBoard = appPropertyService.getNoOfRowsOnBoard();

    List<GameMoveDto> playedMoves = new ArrayList<>();
    List<GameMoveDto> playableMoves = new ArrayList<>();
    List<GameMoveDto> neitherPlayedNorPlayableMoves = new ArrayList<>();

    Map<TypeOfMove, List<GameMoveDto>> moveTypeAndMovesMap = new HashMap<>();
    moveTypeAndMovesMap.put(TypeOfMove.PLAYED_MOVE, playedMoves);
    moveTypeAndMovesMap.put(TypeOfMove.PLAYABLE_MOVE, playableMoves);
    moveTypeAndMovesMap.put(TypeOfMove.NEITHER_PLAYED_NOR_PLAYABLE_MOVE,
        neitherPlayedNorPlayableMoves);

    for (int yIndex = 1; yIndex <= rowsOnBoard; yIndex++) {
      for (int xIndex = 1; xIndex <= columnsOnBoard; xIndex++) {
        GameMoveDto move = new GameMoveDto();
        move.setxIndex(xIndex);
        move.setyIndex(yIndex);

        TypeOfMove typeOfCurrentMove = this.findTypeOfCurrentMoveInGivenGame(xIndex, yIndex, game);
        if (typeOfCurrentMove == null) {
          throw new Exception("Caught unknown server error.");
        }
        moveTypeAndMovesMap.get(typeOfCurrentMove).add(move);
      }
    }
    return moveTypeAndMovesMap;
  }

  public TypeOfMove findTypeOfCurrentMoveInGivenGame(int xIndex, int yIndex, Game game) {
    if (game == null || xIndex <= 0 || yIndex <= 0) {
      return null;
    }

    if (this.isMovePlayed(xIndex, yIndex, game)) {
      return TypeOfMove.PLAYED_MOVE;
    }

    if (yIndex == 1) {
      return TypeOfMove.PLAYABLE_MOVE;
    }

    // Now let's find the exact lower cell of the current move.
    int lowerCellxIndex = xIndex;
    int lowerCellyIndex = yIndex - 1;
    if (this.isMovePlayed(lowerCellxIndex, lowerCellyIndex, game)) {
      return TypeOfMove.PLAYABLE_MOVE;
    } else {
      return TypeOfMove.NEITHER_PLAYED_NOR_PLAYABLE_MOVE;
    }
  }

  public boolean isMovePlayed(final int xIndex, final int yIndex, Game game) {
    return this.returnMoveIfPlayedOtherwiseNull(xIndex, yIndex, game) != null;
  }

  public GameMove returnMoveIfPlayedOtherwiseNull(final int xIndex, final int yIndex, Game game) {
    if (game == null || xIndex <= 0 || yIndex <= 0) {
      return null;
    }

    List<GameMove> playedMoves = gameMoveRepository.findByGame(game);
    playedMoves = CollectionUtils.isEmpty(playedMoves) ? new ArrayList<>() : playedMoves;
    GameMove playedMove = playedMoves.stream()
        .filter(x -> (xIndex == x.getxIndex()) && (yIndex == x.getyIndex())).findAny().orElse(null);

    return playedMove;
  }

  public boolean isValidMoveToCountAsWinningMove(int xIndex, int yIndex, Game game,
      long loggedInUserId) {

    GameMove moveToCheck = this.returnMoveIfPlayedOtherwiseNull(xIndex, yIndex, game);

    if (moveToCheck == null || !moveToCheck.getPlayer().getId().equals(loggedInUserId)) {
      return false;
    }
    return true;
  }

}
