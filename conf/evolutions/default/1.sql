# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table client (
  id                            bigint not null,
  name                          varchar(255),
  constraint pk_client primary key (id)
);
create sequence client_seq;

create table person (
  id                            integer not null,
  last_name                     varchar(255),
  first_name                    varchar(255),
  date                          timestamp,
  constraint pk_person primary key (id)
);
create sequence person_seq;


# --- !Downs

drop table if exists client;
drop sequence if exists client_seq;

drop table if exists person;
drop sequence if exists person_seq;

