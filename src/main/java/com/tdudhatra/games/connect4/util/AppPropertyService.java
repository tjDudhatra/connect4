/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class that will be loading properties from property file and
 * functions to retrieve those property values.
 * 
 * @author tdudhatra
 */
@Service
public class AppPropertyService {

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${game.no.of.columns}")
  private int noOfColumnsOnBoard;

  @Value("${game.no.of.rows}")
  private int noOfRowsOnBoard;

  @Value("${game.winner.by.connect}")
  private int winnerByConnect;

  @Value("${spring.jpa.hibernate.ddl-auto}")
  private String springJpaHibernateDdlAuto;

  @Value("${application.admin.username}")
  private String appAdminUsername;

  @Value("${application.admin.password}")
  private String appAdminUserPassword;

  public String getApplicationName() {
    return applicationName;
  }

  public int getNoOfColumnsOnBoard() {
    return noOfColumnsOnBoard;
  }

  public int getNoOfRowsOnBoard() {
    return noOfRowsOnBoard;
  }

  public int getWinnerByConnect() {
    return winnerByConnect;
  }

  public String getSpringJpaHibernateDdlAuto() {
    return springJpaHibernateDdlAuto;
  }

  public String getAppAdminUsername() {
    return appAdminUsername;
  }

  public String getAppAdminUserPassword() {
    return appAdminUserPassword;
  }

}
