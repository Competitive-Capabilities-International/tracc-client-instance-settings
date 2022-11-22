echo Started adding application properties required by stage environment
# DB configuration start
aws ssm put-parameter --name "/config/tracc_client_instance_settings_stage/spring.datasource.url" --value "jdbc:postgresql://tracc-postgres.cdsfjrvpqcc6.eu-west-1.rds.amazonaws.com/tracc" --type "SecureString" --description "JDBC URL of the database" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_stage/spring.jpa.properties.hibernate.cache.redisson.config" --value "redisson/redisson-stage.yaml" --type "SecureString" --description "path to Redisson config file that holds Redis server details" --overwrite
# DB configuration end
aws ssm put-parameter --name "/config/tracc_client_instance_settings_stage/spring.redis.host" --value "tra-re-1petv46n2k1ox.tyfxs0.0001.euw1.cache.amazonaws.com" --type "SecureString" --description "Redis server host to be used by Spring Data LettuceConnectionFactory. See CacheConfig#redisConnectionFactory" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_stage/spring.redis.port" --value "8080" --type "SecureString" --description "Redis server port to be used by Spring Data RedisConnectionFactory config via Lettuce. See CacheConfig#redisConnectionFactory" --overwrite
echo Completed adding application properties required by stage environment
