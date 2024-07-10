--liquibase formatted sql

--changeset yumeinaruu:9
--comment user relation in projects deleted
alter table projects
drop constraint projects_users_id_fk;

alter table projects
drop column user_id;
