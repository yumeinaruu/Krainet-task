--liquibase formatted sql

--changeset yumeinaruu:3
--comment table records created
create table records
(
    id         bigserial                   not null
        constraint records_pk
            primary key,
    started    timestamp without time zone not null,
    deadline   timestamp without time zone,
    project_id bigint                      not null
        constraint records_projects_id_fk
            references projects
            on update cascade on delete cascade
);