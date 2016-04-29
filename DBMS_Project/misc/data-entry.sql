use dbmsProject;
show tables;

desc author;
select * from author;
select * from conference;
select * from papers;
select * from isAuthorOf;

insert into author values(1, 'tom', 'iiitd');
insert into author values(2, 'harry', 'iiitd');

select ath.name from author as ath where (select count(*) as c from author natural join isAuthorOf where author.aid = ath.aid) >= 2;
select * from author natural join isAuthorOf natural join papers;

insert into papers values(100, 'Improving Adversarial Samples', 10);
insert into papers values(101, 'Designing better NNs', 10);
insert into papers values(102, 'Getting Access', 10);
insert into papers values(103, 'Improving Error Surfaces of DBNs', 10);
insert into papers values(105, 'Improving Adversarial Samples 2', 10);

select * from isCitationOf;
insert into isCitationOf values(100, 101);
insert into isCitationOf values(100, 102);
insert into isCitationOf values(100, 103);
insert into isCitationOf values(100, 104);
insert into isCitationOf values(101, 102);
insert into isCitationOf values(101, 103);
insert into isCitationOf values(101, 104);
insert into isCitationOf values(102, 103);
insert into isCitationOf values(102, 104);
insert into isCitationOf values(103, 104);

insert into isCitationOf values(105, 100);

insert into isAuthorOf values(100, 1);
insert into isAuthorOf values(101, 1);
insert into isAuthorOf values(102, 1);
insert into isAuthorOf values(103, 1);

insert into isAuthorOf values(100, 4);
insert into isAuthorOf values(101, 4);
insert into isAuthorOf values(102, 4);
insert into isAuthorOf values(103, 4);

select name, title, citationcount from author natural join isAuthorOf natural join papers where citationcount >= 10;

update papers set citationcount = 5 where pid = 100;
update papers set citationcount = 7 where pid = 101;

select distinct p.title from papers as p where (select count(*) from author natural join isAuthorOf natural join papers where papers.pid = p.pid) >= 2;

select * from isPublishedIn;
select * from conference;

insert into conference values (10, 'ica', '2016-04-14');
insert into isPublishedIn values(100, 10);

select * from author natural join isAuthorOf natural join papers;
select ath.name from author as ath where ath.aid in (select aid from papers natural join isAuthorOf natural join conference authors natural join isPublishedIn where cid = 10);

select p1_pid from isCitationOf where p2_pid in (select distinct pid from papers natural join isAuthorOf natural join conference authors natural join isPublishedIn where cid = 10);