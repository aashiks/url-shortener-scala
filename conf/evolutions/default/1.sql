# --- !Ups

create table "shorturls" (
  "id" varchar not null primary key,
  "original_url" varchar not null
);

# --- !Downs

drop table "shorturls" if exists;
