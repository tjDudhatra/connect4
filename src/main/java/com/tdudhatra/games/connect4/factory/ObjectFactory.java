/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.factory;

import com.tdudhatra.games.connect4.dto.AbstractBaseDto;
import com.tdudhatra.games.connect4.entity.AbstractBaseEntity;

/**
 * Factory interface, identically all factory components should implement this
 * interface. This will force author of factory class to implement atleast two
 * given methods.
 * 
 * @author tdudhatra
 */
public interface ObjectFactory<D extends AbstractBaseDto, E extends AbstractBaseEntity> {

  public D buildDtoFromEntity(E e);

  public E buildEntityFromDto(D d);

}
