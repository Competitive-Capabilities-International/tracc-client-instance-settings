-- login to 'tracc' database as super user
-- Don't allow non super users to create objects in the public schema
REVOKE ALL ON schema public FROM public;

DROP USER IF EXISTS scaffold_app_user;
-- replace 'password' with the password from keeper
CREATE user scaffold_app_user WITH ENCRYPTED PASSWORD 'password';

CREATE SCHEMA IF NOT EXISTS scaffold;
ALTER SCHEMA scaffold OWNER TO scaffold_app_user;

commit;
