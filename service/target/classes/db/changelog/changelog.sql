--liquibase formatted sql
--changeset Keeshan Reddy:dbf7466f-6118-4ddd-993b-7a2272047a38
create table scaffold.parent
(
    id          uuid not null,
    description TEXT not null,
    name        TEXT not null,
    primary key (id)
);

create table scaffold.child
(
    id                     uuid not null,
    display_order          int4 not null,
    parent_id              uuid not null,
    action_id         uuid not null,
    platform_dependency_id uuid not null,
    primary key (id)
);

alter table if exists scaffold.child
    add constraint PARENT_ID_FK
    foreign key (parent_id)
    references scaffold.parent;

--changeset Keeshan Reddy:2a8ddd29-614b-4a18-9412-0c3e2ec3111a
ALTER TABLE IF EXISTS SCAFFOLD.PARENT
ADD COLUMN ORGANISATION_ID UUID NOT NULL;

--changeset Keeshan Reddy:456bb897-f2df-495f-91a1-287d637a44cf
alter table scaffold.child
drop column platform_dependency_id;

--changeset Keeshan Reddy:46f457cb-d144-4daa-9a6d-d6703a1708d0
ALTER TABLE SCAFFOLD.CHILD ALTER COLUMN PARENT_ID SET NOT NULL;
ALTER TABLE IF EXISTS SCAFFOLD.CHILD
    ADD COLUMN TRACC_ID UUID NOT NULL,
    ADD COLUMN STAGE_ID UUID NOT NULL;
