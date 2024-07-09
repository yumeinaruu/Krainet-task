--liquibase formatted sql

--changeset yumeinaruu:6
--comment login unique constraint
create unique index security_login_uindex
    on security (login);
