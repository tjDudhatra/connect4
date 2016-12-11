/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.controller;

import com.tdudhatra.games.connect4.dto.GameStatusDto;
import com.tdudhatra.games.connect4.service.GameService;
import com.tdudhatra.games.connect4.util.EnumConstants.GameStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;

/**
 * Controller for Game related API endpoints.
 * 
 * @author tdudhatra
 */
@Controller
@Transactional
@RequestMapping("/game")
public class GameController {

  @Autowired
  GameService gameService;

  @PostMapping("/start")
  public @ResponseBody GameStatusDto startGame() throws Exception {
    return gameService.startGame();
  }

  @PostMapping("/play/move/{gameId}/col/{colNo}")
  public @ResponseBody GameStatusDto playMove(@PathVariable(value = "gameId") long gameId,
      @PathVariable(value = "colNo") int columnNo) throws Exception {
    return gameService.playMove(gameId, columnNo);
  }

  @PostMapping("/stop/{gameId}")
  public @ResponseBody GameStatusDto stopGame(@PathVariable(value = "gameId") long gameId)
      throws Exception {
    return gameService.changeGameStatus(gameId, GameStatus.GAME_STOPPED_BY_THE_PLAYER);
  }

  @GetMapping("/{gameId}")
  public @ResponseBody GameStatusDto getStatusOfTheGame(@PathVariable("gameId") Long gameId)
      throws Exception {
    return gameService.getStatusOfTheGame(gameId);
  }

}
