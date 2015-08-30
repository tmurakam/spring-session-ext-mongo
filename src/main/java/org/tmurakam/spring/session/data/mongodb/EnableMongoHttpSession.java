package org.tmurakam.spring.session.data.mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Enables Mongo Session Configuration
 */
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value={java.lang.annotation.ElementType.TYPE})
@Import(RedisHttpSessionConfiguration.class)
@Configuration
public @interface EnableMongoHttpSession {
}
