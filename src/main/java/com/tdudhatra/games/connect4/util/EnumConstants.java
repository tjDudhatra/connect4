/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.util;

/**
 * Class which stores all the enumerators used into the project.
 * 
 * @author tdudhatra
 */
public class EnumConstants {

  public static enum GameStatus {
    GAME_STARTED_AND_RUNNING, GAME_STOPPED_BY_THE_PLAYER, GAME_STOPPED_BY_THE_SYSTEM, GAME_COMPLETED
  }

  public static enum GameWinningReason {
    WON_GAME_BY_PLAYING, OPPONENT_GAVE_UP, SYSTEM_CRASHED
  }

  public static enum GameDestroyReason {
    GAME_DESTROYED_BY_PLAYER, SYSTEM_DESTROYED_THE_GAME, SYSTEM_CRASHED
  }

  public static enum UserRoleEnum {
    ROLE_USER, ROLE_ADMIN
  }

  public static enum TypeOfMove {
    PLAYABLE_MOVE, PLAYED_MOVE, NEITHER_PLAYED_NOR_PLAYABLE_MOVE
  }

  public static enum WinPossibleSide {
    RIGHT, LEFT, DOWN, TOP_RIGHT_DIAGONAL, TOP_LEFT_DIAGONAL, BOTTOM_RIGHT_DIAGONAL, BOTTOM_LEFT_DIAGONAL
  }

}
