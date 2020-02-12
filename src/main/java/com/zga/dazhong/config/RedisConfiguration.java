package com.zga.dazhong.config;

import com.zga.dazhong.common.redis.RedisCache;
import com.zga.dazhong.common.redis.RedisLockService;
import com.zga.dazhong.common.redis.RedisLockServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Configuration
//@AutoConfigureAfter(org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
//@ConditionalOnBean(RedisTemplate.class)
public class RedisConfiguration {
    @Resource
    private  RedisTemplate redisTemplate;
   @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        return template;
    }
    @Bean
    public RedisCache redisCache() {
        return new RedisCache(redisTemplate);
    }

    @Bean
    public RedisLockService redisLockService() {
        return new RedisLockServiceImpl(redisTemplate);
    }
}
