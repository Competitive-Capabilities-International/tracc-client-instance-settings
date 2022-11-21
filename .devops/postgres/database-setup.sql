/*
login to default ('postgres') database as super user
ensure auto commit is turned on before running to avoid
"SQL Error [25001]: ERROR: CREATE DATABASE cannot run inside a transaction block"

IT IS LIKELY THIS DB WILL ALREADY EXIST AFTER tracc_client_instance_settings OR KPIs GOES LIVE,
so you can skip creating the DB if this is the case.
you will still need to run the pre-liquibase-init.sql file though.
 */
CREATE DATABASE tracc;

-- after this follow instructions in ./pre-liquibase-init.sql file to setup the schema and user
