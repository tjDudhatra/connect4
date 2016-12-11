/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.util;

import java.util.List;
import java.util.Random;

/**
 * Util methods for collections.
 * 
 * @author tdudhatra
 */
public class CollectionUtils {

  private static Random rand = new Random();

  /**
   * This method will return a random element of the given list.
   * 
   * @param list
   *          - list from which a random element will be picked up.
   * @return an element picked up from the supplied list.
   */
  public static <T> T getRandomItem(List<T> list) {
    if (list == null || list.size() <= 0) {
      return null;
    }
    return list.get(rand.nextInt(list.size()));
  }

}
