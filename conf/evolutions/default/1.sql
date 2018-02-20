# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table academic_program (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  accronym                  varchar(255),
  code                      varchar(255),
  status                    varchar(255),
  constraint uq_academic_program_name unique (name),
  constraint uq_academic_program_accronym unique (accronym),
  constraint uq_academic_program_code unique (code),
  constraint pk_academic_program primary key (id))
;

create table bank (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  accronym                  varchar(255),
  bnr_code                  varchar(255),
  bank_status               varchar(255),
  constraint uq_bank_name unique (name),
  constraint uq_bank_accronym unique (accronym),
  constraint uq_bank_bnr_code unique (bnr_code),
  constraint pk_bank primary key (id))
;

create table bank_account (
  id                        bigint auto_increment not null,
  bank_id                   bigint,
  faculty_id                bigint,
  institution_id            bigint,
  account_number            varchar(255),
  account_status            varchar(255),
  constraint uq_bank_account_account_number unique (account_number),
  constraint pk_bank_account primary key (id))
;

create table degree_program (
  id                        bigint auto_increment not null,
  academic_program_id       bigint,
  institution_id            bigint,
  faculty_id                bigint,
  degree_name               varchar(255),
  degree_accronym           varchar(255),
  status                    varchar(255),
  constraint pk_degree_program primary key (id))
;

create table degree_study_program (
  id                        bigint auto_increment not null,
  degree_program_id         bigint,
  study_program_type_id     bigint,
  program_study_duration    varchar(255),
  status                    varchar(255),
  constraint pk_degree_study_program primary key (id))
;

create table faculty (
  id                        bigint auto_increment not null,
  institution_id            bigint,
  name                      varchar(255),
  accronym                  varchar(255),
  code                      varchar(255),
  constraint pk_faculty primary key (id))
;

create table faculty_academic (
  id                        bigint auto_increment not null,
  academic_program_id       bigint,
  faculty_id                bigint,
  constraint pk_faculty_academic primary key (id))
;

create table inst_academic (
  id                        bigint auto_increment not null,
  academic_program_id       bigint,
  institution_id            bigint,
  status                    varchar(255),
  constraint pk_inst_academic primary key (id))
;

create table institution (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  accronym                  varchar(255),
  inst_logo                 varchar(255),
  address                   varchar(255),
  student_pay_transaction_fees varchar(255),
  phone                     varchar(255),
  email                     varchar(255),
  tin                       varchar(255),
  created_date              datetime,
  inst_code                 varchar(255),
  third_party               varchar(255),
  constraint uq_institution_name unique (name),
  constraint uq_institution_phone unique (phone),
  constraint uq_institution_email unique (email),
  constraint uq_institution_tin unique (tin),
  constraint uq_institution_inst_code unique (inst_code),
  constraint pk_institution primary key (id))
;

create table institution_calender (
  id                        bigint auto_increment not null,
  academic_year             varchar(255),
  institution_id            bigint,
  start_date                varchar(255),
  end_date                  varchar(255),
  batch_code                varchar(255),
  status                    varchar(255),
  constraint pk_institution_calender primary key (id))
;

create table message (
  id                        bigint auto_increment not null,
  date_time                 varchar(255),
  status                    integer,
  message_text              varchar(255),
  user_account_id           bigint,
  topic_id                  bigint,
  constraint pk_message primary key (id))
;

create table none_degree_program (
  id                        bigint auto_increment not null,
  academic_program_id       bigint,
  institution_id            bigint,
  faculty_id                bigint,
  degree_name               varchar(255),
  degree_accronym           varchar(255),
  status                    varchar(255),
  constraint pk_none_degree_program primary key (id))
;

create table none_degree_study_program (
  id                        bigint auto_increment not null,
  none_degree_program_id    bigint,
  study_program_type_id     bigint,
  program_study_duration    varchar(255),
  status                    varchar(255),
  constraint pk_none_degree_study_program primary key (id))
;

create table payment_log (
  id                        bigint auto_increment not null,
  student_id                bigint,
  institution_id            bigint,
  faculty_id                bigint,
  bank_id                   bigint,
  bank_account_id           bigint,
  payment_purpose_id        bigint,
  institution_calender_id   bigint,
  payer_name                varchar(255),
  msisdn                    varchar(255),
  student_names             varchar(255),
  nida                      varchar(255),
  is_registered             varchar(255),
  ext_trx_id                varchar(255),
  bank_slip                 varchar(255),
  payment_channel           varchar(255),
  operator                  varchar(255),
  status_desc               varchar(255),
  ussd_status               varchar(255),
  payment_year              integer,
  payment_device            varchar(255),
  processing_number         varchar(255),
  amount_paid               double,
  payment_date              datetime,
  posting_date              datetime,
  constraint pk_payment_log primary key (id))
;

create table payment_purpose (
  id                        bigint auto_increment not null,
  bank_account_id           bigint,
  institution_id            bigint,
  purpose                   varchar(255),
  description               varchar(255),
  acc_priority              varchar(255),
  constraint pk_payment_purpose primary key (id))
;

create table profile_privilege (
  id                        bigint auto_increment not null,
  profile_id                bigint,
  privilege_id              bigint,
  status                    varchar(255),
  reg_date                  datetime not null,
  mod_date                  datetime not null,
  constraint pk_profile_privilege primary key (id))
;

create table student (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  reg_number                varchar(255),
  institution_id            bigint,
  academic_program_id       bigint,
  degree_program_id         bigint,
  study_program_type_id     bigint,
  institution_calender_id   bigint,
  none_degree_program_id    bigint,
  nida                      varchar(255),
  phone                     varchar(255),
  dob                       varchar(255),
  std_class                 varchar(255),
  sex                       varchar(255),
  email                     varchar(255),
  faculty_id                bigint,
  std_status                varchar(9),
  applicant_status          varchar(7),
  password                  varchar(255),
  std_pic                   varchar(255),
  constraint ck_student_std_status check (std_status in ('ACTIVE','DISABLED','APPLICANT','ADMITTED','OTHER')),
  constraint ck_student_applicant_status check (applicant_status in ('ONGOING','DONE')),
  constraint uq_student_reg_number unique (reg_number),
  constraint uq_student_nida unique (nida),
  constraint uq_student_email unique (email),
  constraint pk_student primary key (id))
;

create table student_pay_log (
  id                        bigint auto_increment not null,
  student_id                bigint,
  institution_id            bigint,
  faculty_id                bigint,
  tuition_fees_id           bigint,
  status_desc               varchar(255),
  amount_paid               double,
  amount_expected           double,
  amount_prev               double,
  constraint pk_student_pay_log primary key (id))
;

create table study_program_type (
  id                        bigint auto_increment not null,
  type_name                 varchar(255),
  constraint pk_study_program_type primary key (id))
;

create table topic (
  id                        bigint auto_increment not null,
  heading_eng               varchar(255),
  heading_kiny              varchar(255),
  heading_fre               varchar(255),
  date_time                 varchar(255),
  constraint pk_topic primary key (id))
;

create table tuition_fees (
  id                        bigint auto_increment not null,
  institution_calender_id   bigint,
  degree_study_program_id   bigint,
  none_degree_study_program_id bigint,
  institution_id            bigint,
  faculty_id                bigint,
  amount                    double,
  status                    varchar(255),
  constraint pk_tuition_fees primary key (id))
;

create table ussd_temp_log (
  id                        bigint auto_increment not null,
  momo_trx                  varchar(255),
  upi_number                varchar(255),
  registeration_number      varchar(255),
  processing_number         varchar(255),
  momo_status               varchar(255),
  msisdn                    varchar(255),
  level                     integer,
  payment_log_id            bigint,
  payment_purpose_id        bigint,
  student_id                bigint,
  reg_number                varchar(255),
  nid                       varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  sex                       varchar(255),
  dob                       varchar(255),
  institution_id            bigint,
  faculty_id                bigint,
  register                  integer,
  exit_menu                 integer,
  text_message              varchar(255),
  user_account_id           bigint,
  bank_account_id           bigint,
  user_profile_id           bigint,
  topic_id                  bigint,
  status                    integer,
  date_time                 varchar(255),
  language_id               varchar(255),
  first_menu                integer,
  second_menu               integer,
  third_menu                integer,
  fourth_menu               integer,
  fifth_menu                integer,
  amount                    integer,
  sixth_menu                integer,
  institution_list          varchar(255),
  purpose_list              varchar(255),
  cell_list                 varchar(255),
  village_list              varchar(255),
  constraint pk_ussd_temp_log primary key (id))
;

create table user_account (
  id                        bigint auto_increment not null,
  institution_id            bigint,
  phone_number              varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  user_profile_id           bigint,
  status                    varchar(255),
  created_by_id             bigint,
  mod_by_id                 bigint,
  reg_date                  datetime not null,
  mod_date                  datetime not null,
  constraint uq_user_account_phone_number unique (phone_number),
  constraint uq_user_account_email unique (email),
  constraint pk_user_account primary key (id))
;

create table user_account_archive (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  inst_id                   bigint,
  phone_number              varchar(255),
  user_email                varchar(255),
  user_pass                 varchar(255),
  user_first_name           varchar(255),
  user_last_name            varchar(255),
  user_account_validate_code varchar(255),
  user_privilege            varchar(255),
  user_status               varchar(255),
  account_status            varchar(255),
  created_by_id             bigint,
  mod_by_id                 bigint,
  reg_date                  datetime,
  mod_date                  datetime,
  constraint pk_user_account_archive primary key (id))
;

create table user_privilege (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_user_privilege primary key (id))
;

create table user_profile (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_user_profile primary key (id))
;

alter table bank_account add constraint fk_bank_account_bankId_1 foreign key (bank_id) references bank (id) on delete restrict on update restrict;
create index ix_bank_account_bankId_1 on bank_account (bank_id);
alter table bank_account add constraint fk_bank_account_facultyId_2 foreign key (faculty_id) references faculty (id) on delete restrict on update restrict;
create index ix_bank_account_facultyId_2 on bank_account (faculty_id);
alter table bank_account add constraint fk_bank_account_instId_3 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_bank_account_instId_3 on bank_account (institution_id);
alter table degree_program add constraint fk_degree_program_academicProgram_4 foreign key (academic_program_id) references academic_program (id) on delete restrict on update restrict;
create index ix_degree_program_academicProgram_4 on degree_program (academic_program_id);
alter table degree_program add constraint fk_degree_program_institution_5 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_degree_program_institution_5 on degree_program (institution_id);
alter table degree_program add constraint fk_degree_program_facultyId_6 foreign key (faculty_id) references faculty (id) on delete restrict on update restrict;
create index ix_degree_program_facultyId_6 on degree_program (faculty_id);
alter table degree_study_program add constraint fk_degree_study_program_degreeProgram_7 foreign key (degree_program_id) references degree_program (id) on delete restrict on update restrict;
create index ix_degree_study_program_degreeProgram_7 on degree_study_program (degree_program_id);
alter table degree_study_program add constraint fk_degree_study_program_studyProgramId_8 foreign key (study_program_type_id) references study_program_type (id) on delete restrict on update restrict;
create index ix_degree_study_program_studyProgramId_8 on degree_study_program (study_program_type_id);
alter table faculty add constraint fk_faculty_instId_9 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_faculty_instId_9 on faculty (institution_id);
alter table faculty_academic add constraint fk_faculty_academic_academicProgram_10 foreign key (academic_program_id) references academic_program (id) on delete restrict on update restrict;
create index ix_faculty_academic_academicProgram_10 on faculty_academic (academic_program_id);
alter table faculty_academic add constraint fk_faculty_academic_facultId_11 foreign key (faculty_id) references faculty (id) on delete restrict on update restrict;
create index ix_faculty_academic_facultId_11 on faculty_academic (faculty_id);
alter table inst_academic add constraint fk_inst_academic_academicProgram_12 foreign key (academic_program_id) references academic_program (id) on delete restrict on update restrict;
create index ix_inst_academic_academicProgram_12 on inst_academic (academic_program_id);
alter table inst_academic add constraint fk_inst_academic_institution_13 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_inst_academic_institution_13 on inst_academic (institution_id);
alter table institution_calender add constraint fk_institution_calender_institution_14 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_institution_calender_institution_14 on institution_calender (institution_id);
alter table message add constraint fk_message_userAccount_15 foreign key (user_account_id) references user_account (id) on delete restrict on update restrict;
create index ix_message_userAccount_15 on message (user_account_id);
alter table message add constraint fk_message_topic_16 foreign key (topic_id) references topic (id) on delete restrict on update restrict;
create index ix_message_topic_16 on message (topic_id);
alter table none_degree_program add constraint fk_none_degree_program_academicProgram_17 foreign key (academic_program_id) references academic_program (id) on delete restrict on update restrict;
create index ix_none_degree_program_academicProgram_17 on none_degree_program (academic_program_id);
alter table none_degree_program add constraint fk_none_degree_program_institution_18 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_none_degree_program_institution_18 on none_degree_program (institution_id);
alter table none_degree_program add constraint fk_none_degree_program_facultyId_19 foreign key (faculty_id) references faculty (id) on delete restrict on update restrict;
create index ix_none_degree_program_facultyId_19 on none_degree_program (faculty_id);
alter table none_degree_study_program add constraint fk_none_degree_study_program_degreeProgram_20 foreign key (none_degree_program_id) references none_degree_program (id) on delete restrict on update restrict;
create index ix_none_degree_study_program_degreeProgram_20 on none_degree_study_program (none_degree_program_id);
alter table none_degree_study_program add constraint fk_none_degree_study_program_studyProgramId_21 foreign key (study_program_type_id) references study_program_type (id) on delete restrict on update restrict;
create index ix_none_degree_study_program_studyProgramId_21 on none_degree_study_program (study_program_type_id);
alter table payment_log add constraint fk_payment_log_studentId_22 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_payment_log_studentId_22 on payment_log (student_id);
alter table payment_log add constraint fk_payment_log_instId_23 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_payment_log_instId_23 on payment_log (institution_id);
alter table payment_log add constraint fk_payment_log_facultId_24 foreign key (faculty_id) references faculty (id) on delete restrict on update restrict;
create index ix_payment_log_facultId_24 on payment_log (faculty_id);
alter table payment_log add constraint fk_payment_log_bank_25 foreign key (bank_id) references bank (id) on delete restrict on update restrict;
create index ix_payment_log_bank_25 on payment_log (bank_id);
alter table payment_log add constraint fk_payment_log_bankAcc_26 foreign key (bank_account_id) references bank_account (id) on delete restrict on update restrict;
create index ix_payment_log_bankAcc_26 on payment_log (bank_account_id);
alter table payment_log add constraint fk_payment_log_paymentPurpose_27 foreign key (payment_purpose_id) references payment_purpose (id) on delete restrict on update restrict;
create index ix_payment_log_paymentPurpose_27 on payment_log (payment_purpose_id);
alter table payment_log add constraint fk_payment_log_academicYear_28 foreign key (institution_calender_id) references institution_calender (id) on delete restrict on update restrict;
create index ix_payment_log_academicYear_28 on payment_log (institution_calender_id);
alter table payment_purpose add constraint fk_payment_purpose_accountId_29 foreign key (bank_account_id) references bank_account (id) on delete restrict on update restrict;
create index ix_payment_purpose_accountId_29 on payment_purpose (bank_account_id);
alter table payment_purpose add constraint fk_payment_purpose_instId_30 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_payment_purpose_instId_30 on payment_purpose (institution_id);
alter table student add constraint fk_student_instId_31 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_student_instId_31 on student (institution_id);
alter table student add constraint fk_student_academicProgram_32 foreign key (academic_program_id) references academic_program (id) on delete restrict on update restrict;
create index ix_student_academicProgram_32 on student (academic_program_id);
alter table student add constraint fk_student_degreeProgram_33 foreign key (degree_program_id) references degree_program (id) on delete restrict on update restrict;
create index ix_student_degreeProgram_33 on student (degree_program_id);
alter table student add constraint fk_student_studyProgram_34 foreign key (study_program_type_id) references study_program_type (id) on delete restrict on update restrict;
create index ix_student_studyProgram_34 on student (study_program_type_id);
alter table student add constraint fk_student_academicYear_35 foreign key (institution_calender_id) references institution_calender (id) on delete restrict on update restrict;
create index ix_student_academicYear_35 on student (institution_calender_id);
alter table student add constraint fk_student_noneDegreeProgram_36 foreign key (none_degree_program_id) references none_degree_program (id) on delete restrict on update restrict;
create index ix_student_noneDegreeProgram_36 on student (none_degree_program_id);
alter table student add constraint fk_student_facultyId_37 foreign key (faculty_id) references faculty (id) on delete restrict on update restrict;
create index ix_student_facultyId_37 on student (faculty_id);
alter table student_pay_log add constraint fk_student_pay_log_studentId_38 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_student_pay_log_studentId_38 on student_pay_log (student_id);
alter table student_pay_log add constraint fk_student_pay_log_instId_39 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_student_pay_log_instId_39 on student_pay_log (institution_id);
alter table student_pay_log add constraint fk_student_pay_log_facultId_40 foreign key (faculty_id) references faculty (id) on delete restrict on update restrict;
create index ix_student_pay_log_facultId_40 on student_pay_log (faculty_id);
alter table student_pay_log add constraint fk_student_pay_log_tuitionFee_41 foreign key (tuition_fees_id) references tuition_fees (id) on delete restrict on update restrict;
create index ix_student_pay_log_tuitionFee_41 on student_pay_log (tuition_fees_id);
alter table tuition_fees add constraint fk_tuition_fees_instCalendar_42 foreign key (institution_calender_id) references institution_calender (id) on delete restrict on update restrict;
create index ix_tuition_fees_instCalendar_42 on tuition_fees (institution_calender_id);
alter table tuition_fees add constraint fk_tuition_fees_degreeStudyProgramId_43 foreign key (degree_study_program_id) references degree_study_program (id) on delete restrict on update restrict;
create index ix_tuition_fees_degreeStudyProgramId_43 on tuition_fees (degree_study_program_id);
alter table tuition_fees add constraint fk_tuition_fees_noneDegreeStudyProgramId_44 foreign key (none_degree_study_program_id) references none_degree_study_program (id) on delete restrict on update restrict;
create index ix_tuition_fees_noneDegreeStudyProgramId_44 on tuition_fees (none_degree_study_program_id);
alter table tuition_fees add constraint fk_tuition_fees_institutionId_45 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_tuition_fees_institutionId_45 on tuition_fees (institution_id);
alter table tuition_fees add constraint fk_tuition_fees_facultyId_46 foreign key (faculty_id) references faculty (id) on delete restrict on update restrict;
create index ix_tuition_fees_facultyId_46 on tuition_fees (faculty_id);
alter table ussd_temp_log add constraint fk_ussd_temp_log_paymentLog_47 foreign key (payment_log_id) references payment_log (id) on delete restrict on update restrict;
create index ix_ussd_temp_log_paymentLog_47 on ussd_temp_log (payment_log_id);
alter table ussd_temp_log add constraint fk_ussd_temp_log_paymentPurpose_48 foreign key (payment_purpose_id) references payment_purpose (id) on delete restrict on update restrict;
create index ix_ussd_temp_log_paymentPurpose_48 on ussd_temp_log (payment_purpose_id);
alter table ussd_temp_log add constraint fk_ussd_temp_log_student_49 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_ussd_temp_log_student_49 on ussd_temp_log (student_id);
alter table ussd_temp_log add constraint fk_ussd_temp_log_institution_50 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_ussd_temp_log_institution_50 on ussd_temp_log (institution_id);
alter table ussd_temp_log add constraint fk_ussd_temp_log_faculty_51 foreign key (faculty_id) references faculty (id) on delete restrict on update restrict;
create index ix_ussd_temp_log_faculty_51 on ussd_temp_log (faculty_id);
alter table ussd_temp_log add constraint fk_ussd_temp_log_userAccounts_52 foreign key (user_account_id) references user_account (id) on delete restrict on update restrict;
create index ix_ussd_temp_log_userAccounts_52 on ussd_temp_log (user_account_id);
alter table ussd_temp_log add constraint fk_ussd_temp_log_bankAccount_53 foreign key (bank_account_id) references bank_account (id) on delete restrict on update restrict;
create index ix_ussd_temp_log_bankAccount_53 on ussd_temp_log (bank_account_id);
alter table ussd_temp_log add constraint fk_ussd_temp_log_userProfile_54 foreign key (user_profile_id) references user_profile (id) on delete restrict on update restrict;
create index ix_ussd_temp_log_userProfile_54 on ussd_temp_log (user_profile_id);
alter table ussd_temp_log add constraint fk_ussd_temp_log_topic_55 foreign key (topic_id) references topic (id) on delete restrict on update restrict;
create index ix_ussd_temp_log_topic_55 on ussd_temp_log (topic_id);
alter table user_account add constraint fk_user_account_instId_56 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_user_account_instId_56 on user_account (institution_id);
alter table user_account add constraint fk_user_account_userProfile_57 foreign key (user_profile_id) references user_profile (id) on delete restrict on update restrict;
create index ix_user_account_userProfile_57 on user_account (user_profile_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table academic_program;

drop table bank;

drop table bank_account;

drop table degree_program;

drop table degree_study_program;

drop table faculty;

drop table faculty_academic;

drop table inst_academic;

drop table institution;

drop table institution_calender;

drop table message;

drop table none_degree_program;

drop table none_degree_study_program;

drop table payment_log;

drop table payment_purpose;

drop table profile_privilege;

drop table student;

drop table student_pay_log;

drop table study_program_type;

drop table topic;

drop table tuition_fees;

drop table ussd_temp_log;

drop table user_account;

drop table user_account_archive;

drop table user_privilege;

drop table user_profile;

SET FOREIGN_KEY_CHECKS=1;

