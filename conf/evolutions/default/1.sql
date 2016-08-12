# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table a_mbs_input_errors (
  id                            number(19) not null,
  line_number                   number(10),
  line                          varchar2(255),
  messages                      varchar2(255),
  datee                         timestamp,
  constraint pk_a_mbs_input_errors primary key (id)
);
create sequence A_MBS_INPUT_ERRORS_seq;


# --- !Downs

drop table a_mbs_input_errors cascade constraints purge;
drop sequence A_MBS_INPUT_ERRORS_seq;

