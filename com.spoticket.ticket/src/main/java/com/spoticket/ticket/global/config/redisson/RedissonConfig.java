package com.spoticket.ticket.global.config.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

  
  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${spring.data.redis.database}")
  private int redisDatabase;

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer()
        .setAddress("redis://" + ":" + redisPort)  // Redis 주소 설정
        .setDatabase(redisDatabase);  // Redis 데이터베이스 번호 설정
    return Redisson.create(config);
  }
}
