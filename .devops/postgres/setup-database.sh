sudo apt-get install -y postgresql-client
psql --version
export host=localhost
export port=5432
export db_name=tracc
export db_super_user=app_user
export PGPASSWORD='password'
psql -h $host -p $port -d $db_name -U $db_super_user -f database-setup.sql
psql -h $host -p $port -d $db_name -U $db_super_user -f pre-liquibase-init.sql
