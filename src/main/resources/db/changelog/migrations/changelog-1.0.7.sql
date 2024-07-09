--liquibase formatted sql

--changeset yumeinaruu:5
--comment status column created for records
alter table records
    add status varchar not null;