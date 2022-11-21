-- note this file is also used for the local developer setup /docker/docker-compose file

-- login to 'tracc' database as super user
-- Don't allow non super users to create objects in the public schema
REVOKE ALL ON schema public FROM public;

DROP USER IF EXISTS tracc_client_instance_settings_app_user;
-- replace 'password' with the password from keeper. DO NOT commit the password from keeper in this file.
CREATE user tracc_client_instance_settings_app_user WITH ENCRYPTED PASSWORD 'password';

CREATE SCHEMA IF NOT EXISTS tracc_client_instance_settings;
ALTER SCHEMA tracc_client_instance_settings OWNER TO tracc_client_instance_settings_app_user;

commit;
