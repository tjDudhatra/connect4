/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.controller;

import com.tdudhatra.games.connect4.Connect4Starter;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Base test for all integration test classes.
 * 
 * @author tdudhatra
 */
@Rollback(true)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Connect4Starter.class)
@TestPropertySource(locations = { "classpath:application-test.properties" })
public abstract class AbstractBaseTestCase {

}
