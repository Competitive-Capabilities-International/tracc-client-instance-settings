package com.traccsolution.tracc_client_instance_settings.model.cache;

import com.traccsolution.tracc_client_instance_settings.extension.containers.RedisTestContainer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import java.util.Map;
/**
 * Custom region factory to create redisson Config at runtime instead of using static values from redisson*.yaml,
 * so that the random port exposed by TestContainers can be set at runtime.
 * @author KeeshanReddy
 */
@Component
public class RedissonRegionFactory extends org.redisson.hibernate.RedissonRegionFactory {
    @Override
    protected RedissonClient createRedissonClient(Map properties) {
        String redisPort = System.getProperty(RedisTestContainer.REDIS_PORT);
        String redisHost = System.getProperty(RedisTestContainer.REDIS_HOST);
        String address = "redis://" + redisHost + ":" + redisPort;
        Config config = new Config();
        config.useSingleServer().setAddress(address);
        return Redisson.create(config);
    }
}
