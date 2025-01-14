package com.spoticket.user.domain.model.entity;

import com.spoticket.user.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_users_coupons", schema = "user_service")
@Builder
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userCouponId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ColumnDefault("'NOT_USE'")
    private String status = "NOT_USE";

    public void use() {
        this.status = "USED";
    }

    public void expiry() {
        this.status = "EXPIRED";
    }

    public static UserCoupon of(User user, Coupon coupon){
        return UserCoupon.builder()
                .user(user)
                .coupon(coupon)
                .build();
    }
}
