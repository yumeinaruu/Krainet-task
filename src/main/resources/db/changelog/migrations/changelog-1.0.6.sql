--liquibase formatted sql

--changeset yumeinaruu:5
--comment login unique constraint
create unique index security_login_uindex
    on security (login);
