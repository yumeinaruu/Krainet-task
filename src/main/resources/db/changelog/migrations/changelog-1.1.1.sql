--liquibase formatted sql

--changeset yumeinaruu:10
--comment rename column in records
alter table records
    rename column deadline to ended;