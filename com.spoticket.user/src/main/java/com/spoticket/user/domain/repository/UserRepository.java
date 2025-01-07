package com.spoticket.user.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.spoticket.user.domain.model.entity.QUser;
import com.spoticket.user.domain.model.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID>,
        QuerydslPredicateExecutor<User>,
        QuerydslBinderCustomizer<QUser> {

    @Override
    default void customize(QuerydslBindings querydslBindings,@NotNull QUser qUser) {
        querydslBindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            List<String> valueList = new ArrayList<>(values.stream().map(String::trim).toList());
            if (valueList.isEmpty()) {
                return Optional.empty();
            }
            BooleanBuilder booleanbuilder = new BooleanBuilder();
            for (String value : valueList) {
                booleanbuilder.or(path.containsIgnoreCase(value));
            }
            return Optional.of(booleanbuilder);
        });
    }

    Optional<User> findByEmail(String email);
}
