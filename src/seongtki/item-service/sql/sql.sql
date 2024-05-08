drop table account if exists cascade;
create table account (
     account_id varchar(10),
     account integer not null default 0,
    primary key (account_id)
);


insert into account(account_id, money) values ('seongtki',10000);
insert into account(account_id, money) values ('staek',20000);
