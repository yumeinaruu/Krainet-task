--liquibase formatted sql

--changeset yumeinaruu:7
--comment status column created for records
alter table records
    add status varchar not null;