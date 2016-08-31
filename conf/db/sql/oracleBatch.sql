DROP TABLE  BATCH_STEP_EXECUTION_CONTEXT ;
DROP TABLE  BATCH_JOB_EXECUTION_CONTEXT ;
DROP TABLE  BATCH_STEP_EXECUTION ;
DROP TABLE  BATCH_JOB_EXECUTION_PARAMS ;
DROP TABLE  BATCH_JOB_EXECUTION ;
DROP TABLE  BATCH_JOB_INSTANCE ;

DROP SEQUENCE  BATCH_STEP_EXECUTION_SEQ ;
DROP SEQUENCE  BATCH_JOB_EXECUTION_SEQ ;
DROP SEQUENCE  BATCH_JOB_SEQ ;







drop table a_mbs3_attribute cascade constraints purge;
drop sequence A_MBS3_Attribute_seq

drop table a_mbs3_classe cascade constraints purge;

drop table a_mbs_input_errors cascade constraints purge;
drop sequence A_MBS_INPUT_ERRORS_seq;

drop table a_mbs3_reader cascade constraints purge;
drop sequence a_mbs3_reader_seq;

drop table a_mbs3_user cascade constraints purge;


CREATE TABLE BATCH_JOB_INSTANCE  (
	JOB_INSTANCE_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ,
	VERSION NUMBER(19,0) ,
	JOB_NAME VARCHAR2(100) NOT NULL,
	JOB_KEY VARCHAR2(32) NOT NULL,
	constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ;

CREATE TABLE BATCH_JOB_EXECUTION  (
	JOB_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ,
	VERSION NUMBER(19,0)  ,
	JOB_INSTANCE_ID NUMBER(19,0) NOT NULL,
	CREATE_TIME TIMESTAMP NOT NULL,
	START_TIME TIMESTAMP DEFAULT NULL ,
	END_TIME TIMESTAMP DEFAULT NULL ,
	STATUS VARCHAR2(10) ,
	EXIT_CODE VARCHAR2(2500) ,
	EXIT_MESSAGE VARCHAR2(2500) ,
	LAST_UPDATED TIMESTAMP,
	JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
	constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
	references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
	JOB_EXECUTION_ID NUMBER(19,0) NOT NULL ,
	TYPE_CD VARCHAR2(6) NOT NULL ,
	KEY_NAME VARCHAR2(100) NOT NULL ,
	STRING_VAL VARCHAR2(250) ,
	DATE_VAL TIMESTAMP DEFAULT NULL ,
	LONG_VAL NUMBER(19,0) ,
	DOUBLE_VAL NUMBER ,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION  (
	STEP_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ,
	VERSION NUMBER(19,0) NOT NULL,
	STEP_NAME VARCHAR2(100) NOT NULL,
	JOB_EXECUTION_ID NUMBER(19,0) NOT NULL,
	START_TIME TIMESTAMP NOT NULL ,
	END_TIME TIMESTAMP DEFAULT NULL ,
	STATUS VARCHAR2(10) ,
	COMMIT_COUNT NUMBER(19,0) ,
	READ_COUNT NUMBER(19,0) ,
	FILTER_COUNT NUMBER(19,0) ,
	WRITE_COUNT NUMBER(19,0) ,
	READ_SKIP_COUNT NUMBER(19,0) ,
	WRITE_SKIP_COUNT NUMBER(19,0) ,
	PROCESS_SKIP_COUNT NUMBER(19,0) ,
	ROLLBACK_COUNT NUMBER(19,0) ,
	EXIT_CODE VARCHAR2(2500) ,
	EXIT_MESSAGE VARCHAR2(2500) ,
	LAST_UPDATED TIMESTAMP,
	constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
	STEP_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
	SERIALIZED_CONTEXT CLOB ,
	constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
	references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
	JOB_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
	SERIALIZED_CONTEXT CLOB ,
	constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NOCYCLE;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NOCYCLE;
CREATE SEQUENCE BATCH_JOB_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NOCYCLE;


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

create table a_mbs_input_errors (
  id                            number(19) not null,
  line_number                   number(10),
  line                          varchar2(255),
  messages                      varchar2(255),
  job_execution_id              number(19),
  constraint pk_a_mbs_input_errors primary key (id)
);
create sequence A_MBS_INPUT_ERRORS_seq;

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


