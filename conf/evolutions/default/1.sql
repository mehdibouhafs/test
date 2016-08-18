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
  classe_class_name             varchar2(255),
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
  job_completed                 number(19),
  job_failed                    number(19),
  job_abondonned                number(19),
  total_jobs                    number(19),
  constraint pk_a_mbs3_user primary key (email)
);

alter table a_mbs3_attribute add constraint fk_a_mbs3_ttrbt_clss_clss_nm foreign key (classe_class_name) references a_mbs3_classe (class_name);
create index ix_a_mbs3_ttrbt_clss_clss_nm on a_mbs3_attribute (classe_class_name);

alter table a_mbs3_notify_mbs3_user add constraint fk_a_mbs3_ntfy_mbs3_sr__mbs_1 foreign key (a_mbs3_notify_id) references a_mbs3_notify (id);
create index ix_a_mbs3_ntfy_mbs3_sr__mbs_1 on a_mbs3_notify_mbs3_user (a_mbs3_notify_id);

alter table a_mbs3_notify_mbs3_user add constraint fk_a_mbs3_ntfy_mbs3_sr__mbs_2 foreign key (a_mbs3_user_email) references a_mbs3_user (email);
create index ix_a_mbs3_ntfy_mbs3_sr__mbs_2 on a_mbs3_notify_mbs3_user (a_mbs3_user_email);


# --- !Downs

alter table a_mbs3_attribute drop constraint if exists fk_a_mbs3_ttrbt_clss_clss_nm;
drop index if exists ix_a_mbs3_ttrbt_clss_clss_nm;

alter table a_mbs3_notify_mbs3_user drop constraint if exists fk_a_mbs3_ntfy_mbs3_sr__mbs_1;
drop index if exists ix_a_mbs3_ntfy_mbs3_sr__mbs_1;

alter table a_mbs3_notify_mbs3_user drop constraint if exists fk_a_mbs3_ntfy_mbs3_sr__mbs_2;
drop index if exists ix_a_mbs3_ntfy_mbs3_sr__mbs_2;

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

drop table a_mbs3_notify cascade constraints purge;
drop sequence A_MBS3_Notify_seq;

drop table a_mbs3_notify_mbs3_user cascade constraints purge;

drop table a_mbs3_user cascade constraints purge;

