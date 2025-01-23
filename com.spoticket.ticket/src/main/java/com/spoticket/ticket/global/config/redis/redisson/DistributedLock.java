package com.spoticket.ticket.global.config.redis.redisson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

  // 락 이름
  String key();

  // 락 시간 단위
  TimeUnit timeUnit() default TimeUnit.SECONDS;

  // 락 획득을 위해 기다리는 시간
  long waitTime() default 5L;

  // 락 획득 이후 해제되는 시간
  long leaseTime() default 3L;
}
