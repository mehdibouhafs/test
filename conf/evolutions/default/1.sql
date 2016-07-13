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

create table upload_result (
  id                            integer auto_increment not null,
  url                           varchar(255),
  thumbnail_url                 varchar(255),
  name                          varchar(255),
  type                          varchar(255),
  size                          bigint,
  delete_url                    varchar(255),
  delete_type                   varchar(255),
  ref                           varchar(255),
  constraint pk_upload_result primary key (id)
);


# --- !Downs

drop table if exists client;
drop sequence if exists client_seq;

drop table if exists person;
drop sequence if exists person_seq;

drop table if exists upload_result;

