--liquibase formatted sql

--changeset yumeinaruu:1
--comment table users created
create table public.users
(
    id      bigserial
        constraint users_pk
            primary key,
    name    varchar   not null,
    created timestamp not null,
    changed timestamp
);

alter table public.users
    owner to postgres;