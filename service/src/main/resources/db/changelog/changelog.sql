--liquibase formatted sql
--changeset Keeshan Reddy:dbf7466f-6118-4ddd-993b-7a2272047a38
create table tracc_client_instance_settings.parent
(
    Settings_Id    uuid not null,
    Le_Quota INTEGER NOT NULL,
    Se_Quota INTEGER NOT NULL,
    Allow_User_Creation BOOLEAN NOT NULL,
    Visable_To_Clients BOOLEAN NOT NULL,
    User_Limit INTEGER NOT NULL,
    Enable_Custom_Support BIT NOT NULL,
    Custom_Support_Data VARCHAR NOT NULL,
    primary key (id)
);

 --changeset Keeshan Reddy:2a8ddd29-614b-4a18-9412-0c3e2ec3111a
ALTER TABLE IF EXISTS tracc_client_instance_settings.PARENT
ADD COLUMN ORGANISATION_ID UUID NOT NULL;
ADD COLUMN Instance_Id UUID NOT NULL;


