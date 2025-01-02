package com.spoticket.teamstadium.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
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
  private String createdBy;

  @Column(name = "updated_by", length = 100)
  private String updatedBy;

  @Column(name = "deleted_by", length = 100)
  private String deletedBy;

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
    this.createdBy = "temp_username"; // 임시데이터
  }

  // 수정
  @PreUpdate
  public void updateBase() {
    this.updatedAt = LocalDateTime.now();
    this.updatedBy = "temp_username"; // 임시데이터
  }

  // 삭제
  public void deleteBase(String username) {
    this.isDeleted = true;
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = username;
  }

}
