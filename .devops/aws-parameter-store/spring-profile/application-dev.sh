echo Started adding application properties required by dev environment
# DB configuration start
aws ssm put-parameter --name "/config/tracc_client_instance_settings_dev/spring.datasource.url" --value "jdbc:postgresql://tracc-dev-postgres.cfrsb2uirrjf.eu-west-1.rds.amazonaws.com:5432/tracc" --type "SecureString" --description "JDBC URL of the database" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_dev/spring.jpa.properties.hibernate.cache.redisson.config" --value "redisson/redisson-dev.yaml" --type "SecureString" --description "path to Redisson config file that holds Redis server details" --overwrite
# DB configuration end
aws ssm put-parameter --name "/config/tracc_client_instance_settings_dev/spring.redis.host" --value "dev-re-qjmuary3omt7.iqicof.0001.euw1.cache.amazonaws.com" --type "SecureString" --description "Redis server host to be used by Spring Data RedisConnectionFactory config via Lettuce. See CacheConfig#redisConnectionFactory" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_dev/spring.redis.port" --value "8080" --type "SecureString" --description "Redis server port to be used by Spring Data LettuceConnectionFactory. See CacheConfig#redisConnectionFactory" --overwrite
echo Completed adding application properties required by dev environment
