/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.service;

import com.tdudhatra.games.connect4.config.Connect4UserDetailsService;
import com.tdudhatra.games.connect4.controller.GameController;
import com.tdudhatra.games.connect4.dto.GameStatusDto;
import com.tdudhatra.games.connect4.entity.Game;
import com.tdudhatra.games.connect4.entity.GameMove;
import com.tdudhatra.games.connect4.entity.User;
import com.tdudhatra.games.connect4.factory.GameObjectFactory;
import com.tdudhatra.games.connect4.repository.GameMoveRepository;
import com.tdudhatra.games.connect4.repository.GameRepository;
import com.tdudhatra.games.connect4.util.AppPropertyService;
import com.tdudhatra.games.connect4.util.EnumConstants.GameDestroyReason;
import com.tdudhatra.games.connect4.util.EnumConstants.GameStatus;
import com.tdudhatra.games.connect4.util.EnumConstants.GameWinningReason;
import com.tdudhatra.games.connect4.util.EnumConstants.TypeOfMove;
import com.tdudhatra.games.connect4.util.EnumConstants.WinPossibleSide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Service class for {@link GameController}
 * 
 * @author tdudhatra
 */
@Service
public class GameService {

  @Autowired
  Connect4UserDetailsService connect4UserDetailsService;

  @Autowired
  UserService userService;

  @Autowired
  GameRepository gameRepository;

  @Autowired
  GameMoveRepository gameMoveRepository;

  @Autowired
  GameObjectFactory gameObjectFactory;

  @Autowired
  AppPropertyService appPropertyService;

  public GameStatusDto startGame() throws Exception {

    User loggedInUser = connect4UserDetailsService.getLoggedInUserOrThrow();

    // TODO: This logic can be improvised. Ideally we should be fetching only
    // those users who are online at the moment and not playing any game.
    User player2 = userService.getRandomUserFromDatabaseToJoinTheGame(loggedInUser.getUsername());
    if (player2 == null) {
      throw new Exception("No other user found to play the game. Please try later.");
    }

    Game game = new Game();
    game.setPlayer1(loggedInUser);
    game.setPlayer2(player2);
    game.setGameStartedByUser(loggedInUser);
    game.setGameStatus(GameStatus.GAME_STARTED_AND_RUNNING);

    game = gameRepository.save(game);

    if (game == null) {
      throw new Exception("Caught unknown error while starting game.");
    }

    return gameObjectFactory.buildGameStatusDto(game);
  }

  public GameStatusDto playMove(long gameId, int columnNo) throws Exception {
    if (columnNo <= 0 || gameId <= 0) {
      throw new Exception("Invalid input values.");
    }

    User loggedInUser = connect4UserDetailsService.getLoggedInUserOrThrow();

    Game game = gameRepository.findOne(gameId);
    if (game == null || game.getGameStatus() != GameStatus.GAME_STARTED_AND_RUNNING) {
      throw new Exception("No valid game found to play move.");
    }

    Long loggedInUserId = loggedInUser.getId();
    if (!loggedInUserId.equals(game.getPlayer1().getId())
        && !loggedInUserId.equals(game.getPlayer2().getId())) {
      throw new Exception("User is not allowed to play move for the game of given game id.");
    }

    List<GameMove> latestMovesOfTheGame = gameMoveRepository.findLatestMoveForTheGame(game,
        new PageRequest(0, 1, Sort.Direction.DESC, "id"));
    GameMove latestMoveOfTheGame = CollectionUtils.isEmpty(latestMovesOfTheGame) ? null
        : latestMovesOfTheGame.get(0);

    if (latestMoveOfTheGame != null
        && loggedInUserId.equals(latestMoveOfTheGame.getPlayer().getId())) {
      throw new Exception(
          "Invalid player's turn. One player can not play more than one subsequent moves.");
    }

    List<GameMove> latestMovesOnColumn = gameMoveRepository
        .findLatestMoveForTheGivenColumnForTheGame(game, columnNo,
            new PageRequest(0, 1, Sort.Direction.DESC, "id"));

    int rowNo = CollectionUtils.isEmpty(latestMovesOnColumn) ? 1
        : latestMovesOnColumn.get(0).getyIndex() + 1;

    TypeOfMove currentMoveType = gameObjectFactory.findTypeOfCurrentMoveInGivenGame(columnNo, rowNo,
        game);

    if (currentMoveType != TypeOfMove.PLAYABLE_MOVE) {
      throw new Exception("Invalid Move. Not playable.");
    }

    GameMove gameMove = new GameMove();
    gameMove.setPlayer(loggedInUser);
    gameMove.setxIndex(columnNo);
    gameMove.setyIndex(rowNo);
    gameMove.setGame(game);
    gameMove = gameMoveRepository.save(gameMove);

    if (gameMove == null) {
      throw new Exception("Caught unknown error while playing move.");
    }

    // In case this is the winning move, populate the winning message in
    // GameStatusDto.
    String message = null;
    if (this.isWinningMove(gameMove, game, loggedInUserId)) {
      gameMove.setWinningMove(true);
      gameMove = gameMoveRepository.save(gameMove);

      game.setGameStatus(GameStatus.GAME_COMPLETED);
      game.setGameWinningTime(new Date());
      game.setWinnerPlayer(loggedInUser);
      game.setWinningReason(GameWinningReason.WON_GAME_BY_PLAYING);

      game = gameRepository.save(game);
      message = loggedInUser.getUserFullName() + " has won the game.";

      // TODO: Now game has been closed and moves in game_move table makes no
      // sense to be there. Better to delete moves from there and dump it into
      // game_move_history table and when required (rare case) read it from
      // there.
    }

    GameStatusDto gameStatusDto = gameObjectFactory.buildGameStatusDto(game);
    gameStatusDto.setGameStatus(game.getGameStatus());
    gameStatusDto.setMessage(message);

    return gameStatusDto;
  }

  private boolean isWinningMove(GameMove gameMove, Game game, long loggedInUserId) {

    int maxCols = appPropertyService.getNoOfColumnsOnBoard();
    int maxRows = appPropertyService.getNoOfRowsOnBoard();

    int xIndex = gameMove.getxIndex();
    int yIndex = gameMove.getyIndex();

    for (WinPossibleSide winPossibleSide : WinPossibleSide.values()) {
      if (xIndex == 1 && (winPossibleSide == WinPossibleSide.LEFT
          || winPossibleSide == WinPossibleSide.TOP_LEFT_DIAGONAL
          || winPossibleSide == WinPossibleSide.BOTTOM_LEFT_DIAGONAL)) {
        continue;
      }

      if (xIndex == maxCols && (winPossibleSide == WinPossibleSide.RIGHT
          || winPossibleSide == WinPossibleSide.TOP_RIGHT_DIAGONAL
          || winPossibleSide == WinPossibleSide.BOTTOM_RIGHT_DIAGONAL)) {
        continue;
      }

      if (yIndex == 1 && (winPossibleSide == WinPossibleSide.DOWN
          || winPossibleSide == WinPossibleSide.BOTTOM_LEFT_DIAGONAL
          || winPossibleSide == WinPossibleSide.BOTTOM_RIGHT_DIAGONAL)) {
        continue;
      }

      if (yIndex == maxRows && (winPossibleSide == WinPossibleSide.TOP_LEFT_DIAGONAL
          || winPossibleSide == WinPossibleSide.TOP_RIGHT_DIAGONAL)) {
        continue;
      }

      if (this.checkIfWinningMoveOnGivenSide(game, xIndex, yIndex, winPossibleSide, maxCols,
          maxRows, loggedInUserId)) {
        return true;
      }
    }

    return false;
  }

  private boolean checkIfWinningMoveOnGivenSide(Game game, int xIndex, int yIndex,
      WinPossibleSide sideToCheck, int maxCols, int maxRows, long loggedInUserId) {

    int winAt = appPropertyService.getWinnerByConnect();
    if (winAt <= 1) {
      return true;
    }

    int matchCount = 0;
    if (sideToCheck == WinPossibleSide.RIGHT) {
      // If it's RIGHT side, X++ & Y constant
      int newYIndexToCheck = yIndex;
      int newXIndexToCheck;
      for (newXIndexToCheck = xIndex + 1; newXIndexToCheck <= maxCols; newXIndexToCheck++) {
        if (matchCount == winAt - 1) {
          break;
        }
        if (!gameObjectFactory.isValidMoveToCountAsWinningMove(newXIndexToCheck, newYIndexToCheck,
            game, loggedInUserId)) {
          break;
        }
        matchCount++;
      }
    } else if (sideToCheck == WinPossibleSide.LEFT) {
      // If it's LEFT side, X-- & Y constant
      int newYIndexToCheck = yIndex;
      int newXIndexToCheck;
      for (newXIndexToCheck = xIndex - 1; newXIndexToCheck >= 1; newXIndexToCheck--) {
        if (matchCount == winAt - 1) {
          break;
        }
        if (!gameObjectFactory.isValidMoveToCountAsWinningMove(newXIndexToCheck, newYIndexToCheck,
            game, loggedInUserId)) {
          break;
        }
        matchCount++;
      }
    } else if (sideToCheck == WinPossibleSide.DOWN) {
      // If it's DOWN side, X constant & Y--
      int newXIndexToCheck = xIndex;
      int newYIndexToCheck;
      for (newYIndexToCheck = yIndex - 1; newYIndexToCheck >= 1; newYIndexToCheck--) {
        if (matchCount == winAt - 1) {
          break;
        }
        if (!gameObjectFactory.isValidMoveToCountAsWinningMove(newXIndexToCheck, newYIndexToCheck,
            game, loggedInUserId)) {
          break;
        }
        matchCount++;
      }
    } else if (sideToCheck == WinPossibleSide.TOP_LEFT_DIAGONAL) {
      // If it's TOP_LEFT_DIAGONAL side, X-- & Y++
      int newXIndexToCheck;
      int newYIndexToCheck;
      for (newYIndexToCheck = yIndex + 1, newXIndexToCheck = xIndex
          - 1; (newYIndexToCheck <= maxRows
              && newXIndexToCheck >= 1); newYIndexToCheck++, newXIndexToCheck--) {
        if (matchCount == winAt - 1) {
          break;
        }
        if (!gameObjectFactory.isValidMoveToCountAsWinningMove(newXIndexToCheck, newYIndexToCheck,
            game, loggedInUserId)) {
          break;
        }
        matchCount++;
      }
    } else if (sideToCheck == WinPossibleSide.TOP_RIGHT_DIAGONAL) {
      // If it's TOP_RIGHT_DIAGONAL side, X++ & Y++
      int newXIndexToCheck;
      int newYIndexToCheck;
      for (newYIndexToCheck = yIndex + 1, newXIndexToCheck = xIndex
          + 1; (newYIndexToCheck <= maxRows
              && newXIndexToCheck <= maxCols); newYIndexToCheck++, newXIndexToCheck++) {
        if (matchCount == winAt - 1) {
          break;
        }
        if (!gameObjectFactory.isValidMoveToCountAsWinningMove(newXIndexToCheck, newYIndexToCheck,
            game, loggedInUserId)) {
          break;
        }
        matchCount++;
      }
    } else if (sideToCheck == WinPossibleSide.BOTTOM_RIGHT_DIAGONAL) {
      // If it's BOTTOM_RIGHT_DIAGONAL side, X++ & Y--
      int newXIndexToCheck;
      int newYIndexToCheck;
      for (newYIndexToCheck = yIndex - 1, newXIndexToCheck = xIndex + 1; (newYIndexToCheck >= 1
          && newXIndexToCheck <= maxCols); newYIndexToCheck--, newXIndexToCheck++) {
        if (matchCount == winAt - 1) {
          break;
        }
        if (!gameObjectFactory.isValidMoveToCountAsWinningMove(newXIndexToCheck, newYIndexToCheck,
            game, loggedInUserId)) {
          break;
        }
        matchCount++;
      }
    } else if (sideToCheck == WinPossibleSide.BOTTOM_LEFT_DIAGONAL) {
      // If it's BOTTOM_LEFT_DIAGONAL side, X-- & Y--
      int newXIndexToCheck;
      int newYIndexToCheck;
      for (newYIndexToCheck = yIndex - 1, newXIndexToCheck = xIndex - 1; (newYIndexToCheck >= 1
          && newXIndexToCheck >= 1); newYIndexToCheck--, newXIndexToCheck--) {
        if (matchCount == winAt - 1) {
          break;
        }
        if (!gameObjectFactory.isValidMoveToCountAsWinningMove(newXIndexToCheck, newYIndexToCheck,
            game, loggedInUserId)) {
          break;
        }
        matchCount++;
      }
    }

    // If win is at 4 and found 3 potential neighbor with the same
    // player's move then 3 matched move + current move = 4 = winning moves.
    return matchCount == winAt - 1;
  }

  public GameStatusDto changeGameStatus(long gameId, GameStatus gameStatus) throws Exception {
    if (gameId <= 0) {
      throw new Exception("Invalid input values.");
    }

    Game game = gameRepository.findOne(gameId);
    if (game == null || game.getGameStatus() != GameStatus.GAME_STARTED_AND_RUNNING) {
      throw new Exception("No valid game found to change status.");
    }

    User loggedInUser = connect4UserDetailsService.getLoggedInUserOrThrow();
    Long loggedInUserId = loggedInUser.getId();
    if (!loggedInUserId.equals(game.getPlayer1().getId())
        && !loggedInUserId.equals(game.getPlayer2().getId())) {
      throw new Exception("User is not allowed to change status of the game of given game id.");
    }

    String message = null;
    if (gameStatus == GameStatus.GAME_STOPPED_BY_THE_PLAYER) {
      game.setDestroyerPlayer(loggedInUser);
      game.setDestroyReason(GameDestroyReason.GAME_DESTROYED_BY_PLAYER);
      message = loggedInUser + " has stopped the game.";
    } else if (gameStatus == GameStatus.GAME_STOPPED_BY_THE_SYSTEM) {
      game.setDestroyReason(GameDestroyReason.SYSTEM_DESTROYED_THE_GAME);
      message = "Due to some internal error, your game has been stopped by the system.";
    }

    game.setGameStatus(gameStatus);
    game = gameRepository.save(game);

    GameStatusDto gameStatusDto = gameObjectFactory.buildGameStatusDto(game);
    gameStatusDto.setGameStatus(game.getGameStatus());
    gameStatusDto.setMessage(message);

    return gameStatusDto;
  }

  public GameStatusDto getStatusOfTheGame(Long gameId) throws Exception {

    Game game = gameRepository.findOne(gameId);
    if (game == null) {
      throw new Exception("No Game found to read the status.");
    }

    User loggedInUser = connect4UserDetailsService.getLoggedInUserOrThrow();
    Long loggedInUserId = loggedInUser.getId();

    if (!game.getPlayer1().getId().equals(loggedInUserId)
        && !game.getPlayer2().getId().equals(loggedInUserId)) {
      throw new Exception("LoggedIn user is not allowed to view the the game.");
    }

    return gameObjectFactory.buildGameStatusDto(game);
  }

}
