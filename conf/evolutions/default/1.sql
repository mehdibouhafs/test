# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table a_mbs3_attribute (
  id                            number(19) not null,
  type                          varchar2(255),
  nameo                         varchar2(255),
  sizeo                         varchar2(255),
  pko                           number(1),
  non_null                      number(1),
  defautl_val                   varchar2(255),
  commentaire                   varchar2(255),
  classe_id                     number(19),
  constraint pk_a_mbs3_attribute primary key (id)
);
create sequence A_MBS3_Attribute_seq;

create table a_mbs3_classe (
  id                            number(19) not null,
  class_name                    varchar2(255),
  constraint pk_a_mbs3_classe primary key (id)
);
create sequence A_MBS3_CLASSE_seq;

create table a_mbs_input_errors (
  id                            number(19) not null,
  line_number                   number(10),
  line                          varchar2(255),
  messages                      varchar2(255),
  datee                         timestamp,
  constraint pk_a_mbs_input_errors primary key (id)
);
create sequence A_MBS_INPUT_ERRORS_seq;

create table a_mbs3_notify (
  id                            number(19) not null,
  message                       varchar2(255),
  viewed                        number(1),
  constraint pk_a_mbs3_notify primary key (id)
);
create sequence A_MBS3_Notify_seq;

create table a_mbs3_notify_mbs3_user (
  a_mbs3_notify_id              number(19) not null,
  a_mbs3_user_email             varchar2(255) not null,
  constraint pk_a_mbs3_notify_mbs3_user primary key (a_mbs3_notify_id,a_mbs3_user_email)
);

create table a_mbs3_user (
  email                         varchar2(255) not null,
  first_name                    varchar2(255),
  last_name                     varchar2(255),
  image_path                    varchar2(255),
  password                      varchar2(255),
  constraint pk_a_mbs3_user primary key (email)
);

alter table a_mbs3_attribute add constraint fk_a_mbs3_attribute_classe_id foreign key (classe_id) references a_mbs3_classe (id);
create index ix_a_mbs3_attribute_classe_id on a_mbs3_attribute (classe_id);

alter table a_mbs3_notify_mbs3_user add constraint fk_a_mbs3_ntfy_mbs3_sr__mbs_1 foreign key (a_mbs3_notify_id) references a_mbs3_notify (id);
create index ix_a_mbs3_ntfy_mbs3_sr__mbs_1 on a_mbs3_notify_mbs3_user (a_mbs3_notify_id);

alter table a_mbs3_notify_mbs3_user add constraint fk_a_mbs3_ntfy_mbs3_sr__mbs_2 foreign key (a_mbs3_user_email) references a_mbs3_user (email);
create index ix_a_mbs3_ntfy_mbs3_sr__mbs_2 on a_mbs3_notify_mbs3_user (a_mbs3_user_email);


# --- !Downs

alter table a_mbs3_attribute drop constraint if exists fk_a_mbs3_attribute_classe_id;
drop index if exists ix_a_mbs3_attribute_classe_id;

alter table a_mbs3_notify_mbs3_user drop constraint if exists fk_a_mbs3_ntfy_mbs3_sr__mbs_1;
drop index if exists ix_a_mbs3_ntfy_mbs3_sr__mbs_1;

alter table a_mbs3_notify_mbs3_user drop constraint if exists fk_a_mbs3_ntfy_mbs3_sr__mbs_2;
drop index if exists ix_a_mbs3_ntfy_mbs3_sr__mbs_2;

drop table a_mbs3_attribute cascade constraints purge;
drop sequence A_MBS3_Attribute_seq;

drop table a_mbs3_classe cascade constraints purge;
drop sequence A_MBS3_CLASSE_seq;

drop table a_mbs_input_errors cascade constraints purge;
drop sequence A_MBS_INPUT_ERRORS_seq;

drop table a_mbs3_notify cascade constraints purge;
drop sequence A_MBS3_Notify_seq;

drop table a_mbs3_notify_mbs3_user cascade constraints purge;

drop table a_mbs3_user cascade constraints purge;

