package com.ps.cinema.server.config;

import com.ps.cinema.server.model.Token;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory getConnectionFactory(){
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, Token> redisTemplate(){
        RedisTemplate<String, Token> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(getConnectionFactory());
        return redisTemplate;
    }

}
