/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.entity;

import com.eaio.uuid.UUID;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

/**
 * An abstract base class for all entities that use a ID as primary key and
 * createTime, updateTime fields. Identically all the entities should be derived
 * from this MappedSuperClass.
 * 
 * @author tdudhatra
 */
@MappedSuperclass
@EntityListeners({ AbstractBaseEntity.AbstractEntityListener.class })
public abstract class AbstractBaseEntity implements Serializable {
  private static final long serialVersionUID = 6503223978880085256L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotEmpty
  private String guid;

  @NotNull
  private Date createTime;

  @NotNull
  private Date updateTime;

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

  /**
   * Set the createTime, updateTime, guid fields before every database save.
   * 
   * @author tdudhatra
   */
  public static class AbstractEntityListener {

    @PrePersist
    public void onPrePersist(AbstractBaseEntity abstractEntity) {
      if (abstractEntity.getCreateTime() == null) {
        abstractEntity.setCreateTime(new Date());
      }
      abstractEntity.setUpdateTime(new Date());
      abstractEntity.setGuid(new UUID().toString());
    }

    @PreUpdate
    public void onPreUpdate(AbstractBaseEntity abstractEntity) {
      abstractEntity.setUpdateTime(new Date());
    }
  }

}
