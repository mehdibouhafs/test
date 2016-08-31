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
  commentaires                  varchar2(255),
  defaut                        varchar2(255),
  classe                        varchar2(255),
  constraint pk_a_mbs3_attribute primary key (id)
);
create sequence A_MBS3_Attribute_seq;

create table batch_job_execution_params (
  job_execution_id              number(19) not null,
  type_cd                       varchar2(255),
  key_name                      varchar2(255),
  string_val                    varchar2(255),
  date_val                      timestamp,
  long_val                      number(19),
  double_val                    number(19,4),
  identifying                   varchar2(255),
  constraint pk_batch_job_execution_params primary key (job_execution_id)
);
create sequence BATCH_JOB_EXECUTION_PARAMS_seq;

create table batch_job_execution (
  job_execution_id              number(19) not null,
  version                       number(19),
  job_instance_id               number(19),
  create_time                   timestamp,
  start_time                    timestamp,
  end_time                      timestamp,
  status                        varchar2(255),
  exit_code                     varchar2(255),
  exit_message                  varchar2(255),
  last_updated                  timestamp,
  job_configuration_location    varchar2(255),
  constraint pk_batch_job_execution primary key (job_execution_id)
);
create sequence BATCH_JOB_EXECUTION_seq;

create table batch_job_instance (
  job_instance_id               number(19) not null,
  version                       number(19),
  job_name                      varchar2(255),
  job_key                       varchar2(255),
  constraint pk_batch_job_instance primary key (job_instance_id)
);
create sequence BATCH_JOB_INSTANCE_seq;

create table batch_step_execution (
  step_execution_id             number(19) not null,
  version                       number(19),
  step_name                     varchar2(255),
  exit_message                  varchar2(255),
  job_execution_id              number(19),
  start_time                    timestamp,
  end_time                      timestamp,
  status                        varchar2(255),
  commit_count                  number(19),
  read_count                    number(19),
  filter_count                  number(19),
  write_count                   number(19),
  read_skip_count               number(19),
  write_skip_count              number(19),
  process_skip_count            number(19),
  rollback_count                number(19),
  exit_code                     varchar2(255),
  last_updated                  timestamp,
  constraint pk_batch_step_execution primary key (step_execution_id)
);
create sequence BATCH_STEP_EXECUTION_seq;

create table a_mbs3_classe (
  class_name                    varchar2(255) not null,
  user_email                    varchar2(255),
  viewed                        number(10),
  constraint pk_a_mbs3_classe primary key (class_name)
);

create table a_mbs_input_errors (
  id                            number(19) not null,
  line_number                   number(10),
  line                          varchar2(255),
  messages                      varchar2(255),
  job_execution_id              number(19),
  constraint pk_a_mbs_input_errors primary key (id)
);
create sequence A_MBS_INPUT_ERRORS_seq;

create table a_mbs_programing (
  id                            number(19) not null,
  reader_id                     number(19),
  datee                         timestamp,
  constraint pk_a_mbs_programing primary key (id)
);
create sequence A_MBS_Programing_seq;

create table a_mbs3_reader (
  id                            number(19) not null,
  file_path                     varchar2(255),
  separator                     varchar2(255),
  nb_line_to_skip               number(10),
  classe_name                   varchar2(255),
  columns                       varchar2(255),
  email_user                    varchar2(255),
  date_creation                 timestamp,
  date_lancement                timestamp,
  table_name                    varchar2(255),
  executed                      number(1),
  resultat                      number(1),
  executed_by                   varchar2(255),
  job_id                        number(19),
  nb_lines_success              number(19),
  nb_lines_failed               number(19),
  constraint pk_a_mbs3_reader primary key (id)
);
create sequence a_mbs3_reader_seq;

create table a_mbs3_user (
  email                         varchar2(255) not null,
  first_name                    varchar2(255),
  last_name                     varchar2(255),
  image_path                    varchar2(255),
  password                      varchar2(255),
  job_completed                 number(19),
  job_failed                    number(19),
  job_abondonned                number(19),
  total_jobs                    number(19),
  constraint pk_a_mbs3_user primary key (email)
);


# --- !Downs

drop table a_mbs3_attribute cascade constraints purge;
drop sequence A_MBS3_Attribute_seq;

drop table batch_job_execution_params cascade constraints purge;
drop sequence BATCH_JOB_EXECUTION_PARAMS_seq;

drop table batch_job_execution cascade constraints purge;
drop sequence BATCH_JOB_EXECUTION_seq;

drop table batch_job_instance cascade constraints purge;
drop sequence BATCH_JOB_INSTANCE_seq;

drop table batch_step_execution cascade constraints purge;
drop sequence BATCH_STEP_EXECUTION_seq;

drop table a_mbs3_classe cascade constraints purge;

drop table a_mbs_input_errors cascade constraints purge;
drop sequence A_MBS_INPUT_ERRORS_seq;

drop table a_mbs_programing cascade constraints purge;
drop sequence A_MBS_Programing_seq;

drop table a_mbs3_reader cascade constraints purge;
drop sequence a_mbs3_reader_seq;

drop table a_mbs3_user cascade constraints purge;

