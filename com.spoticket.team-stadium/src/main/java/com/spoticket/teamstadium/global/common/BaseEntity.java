package com.spoticket.teamstadium.global.common;

import com.spoticket.teamstadium.global.util.RequestUtils;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public abstract class BaseEntity {

  @Column(name = "created_by", nullable = false, length = 100)
  private UUID createdBy;

  @Column(name = "updated_by", length = 100)
  private UUID updatedBy;

  @Column(name = "deleted_by", length = 100)
  private UUID deletedBy;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "is_deleted", nullable = false)
  @ColumnDefault("false")
  private boolean isDeleted;

  // 생성
  @PrePersist
  public void createBase() {
    this.createdAt = LocalDateTime.now();
    this.createdBy = RequestUtils.getCurrentUserId();
  }

  // 수정
  @PreUpdate
  public void updateBase() {
    this.updatedAt = LocalDateTime.now();
    this.updatedBy = RequestUtils.getCurrentUserId();
  }

  // 삭제
  public void deleteBase() {
    this.isDeleted = true;
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = RequestUtils.getCurrentUserId();
  }

}
