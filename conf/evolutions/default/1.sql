# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table a_mbs3_attribute (
  id                            number(10),
  type                          varchar2(255),
  nameo                         varchar2(255),
  sizeo                         varchar2(255),
  pko                           number(1),
  non_null                      number(1),
  defautl_val                   varchar2(255),
  commentaire                   varchar2(255),
  classe_id                     number(19)
);

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

alter table a_mbs3_attribute add constraint fk_a_mbs3_attribute_classe_id foreign key (classe_id) references a_mbs3_classe (id);
create index ix_a_mbs3_attribute_classe_id on a_mbs3_attribute (classe_id);


# --- !Downs

alter table a_mbs3_attribute drop constraint if exists fk_a_mbs3_attribute_classe_id;
drop index if exists ix_a_mbs3_attribute_classe_id;

drop table a_mbs3_attribute cascade constraints purge;

drop table a_mbs3_classe cascade constraints purge;
drop sequence A_MBS3_CLASSE_seq;

drop table a_mbs_input_errors cascade constraints purge;
drop sequence A_MBS_INPUT_ERRORS_seq;

