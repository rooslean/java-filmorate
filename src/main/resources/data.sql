insert into GENRE (NAME)
select *
from VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')
WHERE C1 NOT IN (select NAME from GENRE);

insert into MPA_RATING (NAME)
select *
from VALUES ('G'),
  ('PG'),
  ('PG-13'),
  ('R'),
  ('NC-17')
WHERE C1 NOT IN (select RATING_NAME from MPA_RATING);

insert into FILM
(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
values('1+1', '', '2011-05-05', 0, 1);