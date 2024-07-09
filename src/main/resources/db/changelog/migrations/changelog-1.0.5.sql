--liquibase formatted sql

--changeset yumeinaruu:5
--comment when created and changed project added
alter table projects
    add created timestamp without time zone not null;

alter table projects
    add changed timestamp without time zone;