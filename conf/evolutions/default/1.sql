# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table contributor (
  id                        bigint not null,
  showreel_id               bigint not null,
  name                      varchar(255),
  email                     varchar(255),
  constraint pk_contributor primary key (id))
;

create table session (
  id                        bigint not null,
  token                     varchar(255),
  timestamp                 timestamp,
  email                     varchar(255),
  constraint pk_session primary key (id))
;

create table showreel (
  id                        bigint not null,
  title                     varchar(255),
  text                      varchar(255),
  img_url                   varchar(255),
  showreel_url              varchar(255),
  viewable                  varchar(255),
  user_email                varchar(255),
  constraint pk_showreel primary key (id))
;

create table showreelsuser (
  email                     varchar(255) not null,
  password                  varchar(255),
  name                      varchar(255),
  constraint pk_showreelsuser primary key (email))
;

create sequence contributor_seq;

create sequence session_seq;

create sequence showreel_seq;

create sequence showreelsuser_seq;

alter table contributor add constraint fk_contributor_showreel_1 foreign key (showreel_id) references showreel (id) on delete restrict on update restrict;
create index ix_contributor_showreel_1 on contributor (showreel_id);
alter table showreel add constraint fk_showreel_user_2 foreign key (user_email) references showreelsuser (email) on delete restrict on update restrict;
create index ix_showreel_user_2 on showreel (user_email);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists contributor;

drop table if exists session;

drop table if exists showreel;

drop table if exists showreelsuser;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists contributor_seq;

drop sequence if exists session_seq;

drop sequence if exists showreel_seq;

drop sequence if exists showreelsuser_seq;

