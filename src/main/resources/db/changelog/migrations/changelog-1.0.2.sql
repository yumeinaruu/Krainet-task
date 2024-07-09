--liquibase formatted sql

--changeset yumeinaruu:2
--comment table projects created
create table public.projects
(
    id          bigserial
        constraint projects_pk
            primary key,
    name        varchar not null,
    description text,
    user_id     bigint
        constraint projects_users_id_fk
            references public.users
            on update cascade on delete cascade
);

alter table public.projects
    owner to postgres;

