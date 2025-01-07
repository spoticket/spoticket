package com.spoticket.game.global.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  private boolean isDeleted = false;

  @CreatedDate
  private LocalDateTime createdAt;

  @CreatedBy
  private UUID createdBy;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @LastModifiedBy
  private UUID updatedBy;

  private LocalDateTime deletedAt;

  private UUID deletedBy;

  public void delete() {
    isDeleted = true;
    deletedAt = LocalDateTime.now();
  }

}
