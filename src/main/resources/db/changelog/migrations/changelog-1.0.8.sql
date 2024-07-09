--liquibase formatted sql

--changeset yumeinaruu:8
--comment user relation in records
alter table records
    add user_id bigint not null;

alter table records
    add constraint records_users_id_fk
        foreign key (user_id) references users
            on update cascade on delete cascade;
