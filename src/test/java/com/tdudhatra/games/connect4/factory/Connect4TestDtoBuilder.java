/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.factory;

import com.eaio.uuid.UUID;
import com.tdudhatra.games.connect4.dto.UserDto;

/**
 * @author tdudhatra
 *
 */
public class Connect4TestDtoBuilder {

  public static UserDto buildRandomUserDtoToCreateUser() {
    UserDto userDto = new UserDto();
    userDto.setUsername("test-" + new UUID().toString());
    userDto.setPassword("welcome155");
    userDto.setUserFullName(userDto.getUsername());
    userDto.setEnabled(true);

    return userDto;
  }

}
