echo Started adding application properties required by local environment
aws ssm put-parameter --name "/config/tracc_client_instance_settings_local/server.port" --value "8882" --type "SecureString" --description "application port number" --overwrite
# DB configuration start
aws ssm put-parameter --name "/config/tracc_client_instance_settings_local/spring.datasource.url" --value "jdbc:postgresql://localhost:5432/tracc" --type "SecureString" --description "JDBC URL of the database" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_local/spring.datasource.password" --value "password" --type "SecureString" --description "Login password of the database" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_local/spring.jpa.show-sql" --value "true" --type "SecureString" --description "Whether to enable logging of SQL statements" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_local/spring.jpa.properties.hibernate.cache.redisson.config" --value "redisson/redisson-local.yaml" --type "SecureString" --description "path to Redisson config file that holds Redis server details" --overwrite
# DB configuration end
aws ssm put-parameter --name "/config/tracc_client_instance_settings_local/spring.redis.host" --value "localhost" --type "SecureString" --description "Redis server host to be used by Spring Data LettuceConnectionFactory. See CacheConfig#redisConnectionFactory" --overwrite
aws ssm put-parameter --name "/config/tracc_client_instance_settings_local/spring.redis.port" --value "6379" --type "SecureString" --description "Redis server port to be used by Spring Data RedisConnectionFactory config via Lettuce. See CacheConfig#redisConnectionFactory" --overwrite
echo Completed adding application properties required by local environment
