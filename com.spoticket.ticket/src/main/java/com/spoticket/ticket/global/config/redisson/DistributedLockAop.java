package com.spoticket.ticket.global.config.redisson;

import com.spoticket.ticket.application.dtos.request.CreateTicketRequest;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

  private static final String REDISSON_LOCK_PREFIX = "LOCK:";

  private final RedissonClient redissonClient;
  private final AopForTransaction aopForTransaction;

  @Around("@annotation(DistributedLock)")
  public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

    Object[] args = joinPoint.getArgs();
    String seatName = null;
    if (args.length > 0 && args[0] instanceof CreateTicketRequest) {
      seatName = ((CreateTicketRequest) args[0]).seatName();
    }

    // seatName을 기반으로 key 생성
    if (seatName == null) {
      throw new IllegalArgumentException("Seat name must not be null for locking.");
    }
    String key = REDISSON_LOCK_PREFIX + seatName;

    RLock rLock = redissonClient.getLock(key);

    try {
      boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(),
          distributedLock.timeUnit());
      if (!available) {
        return false;
      }
      log.info("락 획득 성공 {}", key);
      return aopForTransaction.proceed(joinPoint);
    } catch (InterruptedException e) {
      throw new InterruptedException();
    } finally {
      rLock.unlock();
    }
  }
}
