/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.controller;

import com.tdudhatra.games.connect4.dto.GameDto;
import com.tdudhatra.games.connect4.dto.GameMoveDto;
import com.tdudhatra.games.connect4.dto.GameStatusDto;
import com.tdudhatra.games.connect4.dto.UserDto;
import com.tdudhatra.games.connect4.entity.Game;
import com.tdudhatra.games.connect4.factory.Connect4TestDtoBuilder;
import com.tdudhatra.games.connect4.repository.GameRepository;
import com.tdudhatra.games.connect4.util.AppPropertyService;
import com.tdudhatra.games.connect4.util.CollectionUtils;
import com.tdudhatra.games.connect4.util.EnumConstants.GameStatus;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * JUnit integration tests for {@link GameController}
 * 
 * @author tdudhatra
 */
public class GameControllerIntegrationTest extends AbstractBaseTestCase {

  @Autowired
  GameController gameController;

  @Autowired
  AppPropertyService appPropertyService;

  @Autowired
  UserController userController;

  @Autowired
  GameRepository gameRepository;

  /**
   * Positive test case to validate startGame by passing all the expected
   * values.
   * 
   * @throws Exception
   */
  @Test
  public void test1_startGame() throws Exception {

    // Create another user so that game can be started.
    UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    userDto = userController.registerUser(userDto);
    Assert.assertNotNull(userDto.getId());

    // Authenticate with admin user and try to start the game.
    Authentication auth = Mockito.mock(Authentication.class);
    Mockito.when(auth.getPrincipal()).thenReturn(appPropertyService.getAppAdminUsername());
    SecurityContextHolder.getContext().setAuthentication(auth);

    GameStatusDto gameStatusDto = gameController.startGame();
    Assert.assertNotNull(gameStatusDto);
    Assert.assertNotNull(gameStatusDto.getGameDto());
  }

  /**
   * Negative test case to validate startGame. Trying to start a game without
   * authentication.
   * 
   * @throws Exception
   */
  @Test
  public void test2_startGame() throws Exception {
    try {
      gameController.startGame();
      Assert.fail("If test comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  public GameStatusDto setupGameToPlayMove() throws Exception {

    UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    userDto = userController.registerUser(userDto);
    Assert.assertNotNull(userDto.getId());

    // Authenticate with admin user and try to start the game.
    Authentication auth = Mockito.mock(Authentication.class);
    Mockito.when(auth.getPrincipal()).thenReturn(appPropertyService.getAppAdminUsername());
    SecurityContextHolder.getContext().setAuthentication(auth);

    GameStatusDto gameStatusDto = gameController.startGame();
    Assert.assertNotNull(gameStatusDto);
    Assert.assertNotNull(gameStatusDto.getGameDto());

    return gameStatusDto;
  }

  /**
   * Positive test case to validate playMove by passing all the expected values.
   * Also it will try to play move by another user who will be playing with the
   * first user.
   * 
   * @throws Exception
   */
  @Test
  public void test1_playMove() throws Exception {

    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameDto gameDto = gameStatus.getGameDto();
    List<GameMoveDto> playableMoves = gameStatus.getPlayableMoves();
    GameMoveDto moveToPlay = CollectionUtils.getRandomItem(playableMoves);
    Assert.assertNotNull(moveToPlay);

    GameStatusDto gameStatusAfterMove = gameController.playMove(gameDto.getId(),
        moveToPlay.getxIndex());
    Assert.assertNotNull(gameStatusAfterMove);
    Assert.assertNotNull(gameStatusAfterMove.getGameDto());

    // Now let's play a move with second player.
    Authentication auth = Mockito.mock(Authentication.class);
    Mockito.when(auth.getPrincipal()).thenReturn(gameDto.getPlayer2().getUsername());
    SecurityContextHolder.getContext().setAuthentication(auth);

    // This move will be for the same column.
    GameStatusDto gameStatusAfterSecondMove = gameController.playMove(gameDto.getId(),
        moveToPlay.getxIndex());
    Assert.assertNotNull(gameStatusAfterSecondMove);
    Assert.assertNotNull(gameStatusAfterSecondMove.getGameDto());
  }

  /**
   * Positive test case to validate playMove by passing all the expected values.
   * Play moves till one of user don't win and see what is the behavior of move
   * played by the user which apparently be a winning move.
   * 
   * @throws Exception
   */
  @Test
  public void test2_playMove() throws Exception {

    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameDto gameDto = gameStatus.getGameDto();
    long gameId = gameDto.getId();
    String player1Username = gameDto.getPlayer1().getUsername();
    String player2Username = gameDto.getPlayer2().getUsername();

    // Now to make user1 win lets make user fill only first column and user2
    // will fill randomly but not in first column.
    this.playMoveWithGivenUserAndGivenIndex(player1Username, 1, gameId);
    this.playMoveWithGivenUserAndGivenIndex(player2Username, 2, gameId);
    this.playMoveWithGivenUserAndGivenIndex(player1Username, 1, gameId);
    this.playMoveWithGivenUserAndGivenIndex(player2Username, 2, gameId);
    this.playMoveWithGivenUserAndGivenIndex(player1Username, 1, gameId);
    this.playMoveWithGivenUserAndGivenIndex(player2Username, 2, gameId);

    // This should be the winning move.
    GameStatusDto winningMoveStatus = this.playMoveWithGivenUserAndGivenIndex(player1Username, 1,
        gameId);
    Assert.assertTrue(winningMoveStatus.getGameStatus() == GameStatus.GAME_COMPLETED);
  }

  public GameStatusDto playMoveWithGivenUserAndGivenIndex(String username, int colNo, long gameId)
      throws Exception {

    Authentication auth = Mockito.mock(Authentication.class);
    Mockito.when(auth.getPrincipal()).thenReturn(username);
    SecurityContextHolder.getContext().setAuthentication(auth);

    GameStatusDto gameStatusAfterMove = gameController.playMove(gameId, colNo);
    Assert.assertNotNull(gameStatusAfterMove);
    Assert.assertNotNull(gameStatusAfterMove.getGameDto());
    return gameStatusAfterMove;
  }

  /**
   * Negative test case to validate playMove. Try without authentication.
   * 
   * @throws Exception
   */
  @Test
  public void test3_playMove() throws Exception {

    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameDto gameDto = gameStatus.getGameDto();
    List<GameMoveDto> playableMoves = gameStatus.getPlayableMoves();
    GameMoveDto moveToPlay = CollectionUtils.getRandomItem(playableMoves);
    Assert.assertNotNull(moveToPlay);

    try {

      // Let's mock empty authentication to validate what's the behavior if user
      // is not authenticated.
      Authentication auth = Mockito.mock(Authentication.class);
      Mockito.when(auth.getPrincipal()).thenReturn(null);
      SecurityContextHolder.getContext().setAuthentication(auth);

      gameController.playMove(gameDto.getId(), moveToPlay.getxIndex());
      Assert.fail(
          "If test comes at this line, that means no exception has been thrown which is not valid.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  /**
   * Negative test case to validate playMove. This will try to play subsequent
   * moves with same user (which should not be allowed).
   * 
   * @throws Exception
   */
  @Test
  public void test4_playMove() throws Exception {

    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameDto gameDto = gameStatus.getGameDto();
    long gameId = gameDto.getId();
    String player1Username = gameDto.getPlayer1().getUsername();

    try {
      this.playMoveWithGivenUserAndGivenIndex(player1Username, 1, gameId);
      this.playMoveWithGivenUserAndGivenIndex(player1Username, 1, gameId);
      Assert.fail("If test comes at this line, that means "
          + "no exception has been thrown and that's not valid.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }

  }

  /**
   * Negative test case to validate playMove. This will try to play move using
   * one user who is not valid player of a game.
   * 
   * @throws Exception
   */
  @Test
  public void test5_playMove() throws Exception {

    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameMoveDto moveToPlay = CollectionUtils.getRandomItem(gameStatus.getPlayableMoves());
    GameDto gameDto = gameStatus.getGameDto();

    try {
      UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
      userDto = userController.registerUser(userDto);
      Assert.assertNotNull(userDto);

      // Now let's play a move with newly created user who is not part of the
      // game.
      Authentication auth = Mockito.mock(Authentication.class);
      Mockito.when(auth.getPrincipal()).thenReturn(userDto.getUsername());
      SecurityContextHolder.getContext().setAuthentication(auth);

      // This move will be for the same column.
      GameStatusDto gameStatusAfterSecondMove = gameController.playMove(gameDto.getId(),
          moveToPlay.getxIndex());
      Assert.assertNotNull(gameStatusAfterSecondMove);
      Assert.assertNotNull(gameStatusAfterSecondMove.getGameDto());
      Assert.fail("If test comes at this line, that means "
          + "no exception has been thrown and that's not valid.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }

  }

  /**
   * Positive test case which will validate stopGame by passing all the expected
   * values.
   * 
   * @throws Exception
   */
  @Test
  public void test1_stopGame() throws Exception {

    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameDto gameDto = gameStatus.getGameDto();

    GameStatusDto gameStatusAfterStoppingGame = gameController.stopGame(gameDto.getId());
    Assert.assertNotNull(gameStatusAfterStoppingGame);
    Assert.assertTrue(
        gameStatusAfterStoppingGame.getGameStatus() == GameStatus.GAME_STOPPED_BY_THE_PLAYER);
  }

  /**
   * Negative test case to validate stopGame. Try without authentication.
   * 
   * @throws Exception
   */
  @Test
  public void test2_stopGame() throws Exception {

    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameDto gameDto = gameStatus.getGameDto();

    // Let's mock empty authentication to validate what's the behavior if user
    // is not authenticated.
    Authentication auth = Mockito.mock(Authentication.class);
    Mockito.when(auth.getPrincipal()).thenReturn(null);
    SecurityContextHolder.getContext().setAuthentication(auth);

    try {
      gameController.stopGame(gameDto.getId());
      Assert.fail("If test comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  /**
   * Negative test case to validate stopGame. Try stopping game with
   * authenticated user who is not part of the game.
   * 
   * @throws Exception
   */
  @Test
  public void test3_stopGame() throws Exception {

    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameDto gameDto = gameStatus.getGameDto();

    // Let's create a new user and try stopping game using that user.
    UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    userDto = userController.registerUser(userDto);

    Authentication auth = Mockito.mock(Authentication.class);
    Mockito.when(auth.getPrincipal()).thenReturn(userDto.getUsername());
    SecurityContextHolder.getContext().setAuthentication(auth);

    try {
      gameController.stopGame(gameDto.getId());
      Assert.fail("If test comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  /**
   * Negative test case to validate stopGame. Try stopping game which is not
   * running.
   * 
   * @throws Exception
   */
  @Test
  public void test4_stopGame() throws Exception {

    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameDto gameDto = gameStatus.getGameDto();
    Game gameEntity = gameRepository.findOne(gameDto.getId());
    Assert.assertNotNull(gameEntity);

    gameEntity.setGameStatus(GameStatus.GAME_COMPLETED);
    gameRepository.save(gameEntity);

    try {
      gameController.stopGame(gameDto.getId());
      Assert.fail("If test comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  /**
   * Positive test case to read the status of the game.
   * 
   * @throws Exception
   */
  @Test
  public void test1_getStatusOfTheGame() throws Exception {
    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    GameDto gameDto = gameStatus.getGameDto();
    gameStatus = gameController.getStatusOfTheGame(gameDto.getId());
    Assert.assertNotNull(gameStatus);
    Assert.assertNotNull(gameStatus.getGameDto());
    Assert.assertTrue(gameStatus.getGameStatus() == GameStatus.GAME_STARTED_AND_RUNNING);
  }

  /**
   * Negative test case to read the status of the game. Pass invalid game id and
   * see what it returns.
   * 
   * @throws Exception
   */
  @Test
  public void test2_getStatusOfTheGame() throws Exception {
    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    try {
      gameController.getStatusOfTheGame(Long.MAX_VALUE);
      Assert.fail("If test comes at this line, that means no exception has been thrown.");
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  /**
   * Negative test case to read the status of the game. Let's try to read game
   * which is not played by the logged in user.
   * 
   * @throws Exception
   */
  @Test
  public void test3_getStatusOfTheGame() throws Exception {
    // Let's start the game first and also authenticate user.
    GameStatusDto gameStatus = this.setupGameToPlayMove();
    Assert.assertNotNull(gameStatus);

    UserDto userDto = Connect4TestDtoBuilder.buildRandomUserDtoToCreateUser();
    userDto = userController.registerUser(userDto);
    Assert.assertNotNull(userDto);
    try {

      Authentication auth = Mockito.mock(Authentication.class);
      Mockito.when(auth.getPrincipal()).thenReturn(userDto.getUsername());
      SecurityContextHolder.getContext().setAuthentication(auth);

      gameController.getStatusOfTheGame(Long.MAX_VALUE);
      Assert.fail("If test comes at this line, that means no exception has been thrown.");

    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

}
