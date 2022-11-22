echo Started adding application properties required by prod environment
# DB configuration start
aws ssm put-parameter --name "/config/tracc_client_instance_settings_prod/spring.datasource.url" --value "jdbc:postgresql://localhost:5432/postgres" --type "SecureString" --description "Redis server port to be used by Spring Data RedisConnectionFactory config via Lettuce. See CacheConfig#redisConnectionFactory" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_prod/spring.jpa.properties.hibernate.cache.redisson.config" --value "redisson/redisson-prod.yaml" --type "SecureString" --description "path to Redisson config file that holds Redis server details" --overwrite
# DB configuration end
aws ssm put-parameter --name "/config/tracc_client_instance_settings_prod/spring.redis.host" --value "pro-re-voamrsylqx6x.0nntdf.0001.euw1.cache.amazonaws.com" --type "SecureString" --description "Redis server host to be used by Spring Data LettuceConnectionFactory. See CacheConfig#redisConnectionFactory" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_prod/spring.redis.port" --value "8080" --type "SecureString" --description "Redis server port to be used by Spring Data RedisConnectionFactory config via Lettuce. See CacheConfig#redisConnectionFactory" --overwrite
echo Completed adding application properties required by prod environment
