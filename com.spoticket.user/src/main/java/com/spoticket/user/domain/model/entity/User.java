package com.spoticket.user.domain.model.entity;

import com.spoticket.user.domain.model.UserRole;
import com.spoticket.user.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


import java.sql.Date;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static io.micrometer.common.util.StringUtils.isNotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_users", schema = "user_service")
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String email;

    private String password;

    private String slackId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String name;

    private String gender;

    private Date birthday;

    private Integer post;

    private String address;
    private String addressDetail;

    public static User of(String email, String password, String slackId, UserRole role, String name, String gender, Date birthday, Integer post, String address, String addressDetail) {
        return User.builder()
                .email(email)
                .password(password)
                .slackId(slackId)
                .role(role)
                .name(name)
                .gender(gender)
                .birthday(birthday)
                .post(post)
                .address(address)
                .addressDetail(addressDetail)
                .build();
    }

    public void update(String email, String password, String slackId, String name, String gender, Date birthday, Integer post, String address, String addressDetail) {
        if (isNotBlank(email)) this.email = email;
        if (isNotBlank(password)) this.password = password;
        if (isNotBlank(slackId)) this.slackId = slackId;
        if (isNotBlank(name)) this.name = name;
        if (isNotBlank(gender)) this.gender = gender;
        if (nonNull(birthday)) this.birthday = birthday;
        if (nonNull(post)) this.post = post;
        if (isNotBlank(address)) this.address = address;
        if (isNotBlank(addressDetail)) this.addressDetail = addressDetail;
    }

    public void roleChange(UserRole newRole) {
        this.role = newRole;
    }
}
