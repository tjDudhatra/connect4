/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This class will boot the application into embedded server.
 * 
 * @author tdudhatra
 */

@Configuration
@ComponentScan(basePackages = { "com.tdudhatra.games" })
@EnableAutoConfiguration
public class Connect4Starter extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Connect4Starter.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Connect4Starter.class);
  }

}
