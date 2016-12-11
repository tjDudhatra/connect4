/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO Representation of
 * {@link com.tdudhatra.games.connect4.entity.AbstractBaseEntity}
 * 
 * @author tdudhatra
 */
public abstract class AbstractBaseDto implements Serializable {
  private static final long serialVersionUID = 6503223978880085256L;

  private Long id;

  private String guid;

  private Date createTime;

  private Date updateTime;

  public AbstractBaseDto() {
    this(null, null, null, null);
  }

  public AbstractBaseDto(Long id, String guid, Date createTime, Date updateTime) {
    this.id = id;
    this.guid = guid;
    this.createTime = createTime;
    this.updateTime = updateTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

}
