SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE FILM_GENRE;

TRUNCATE TABLE FRIENDS;

TRUNCATE TABLE user_film_likes;

TRUNCATE TABLE FILM RESTART IDENTITY;

TRUNCATE TABLE "user" RESTART IDENTITY;

TRUNCATE TABLE GENRE RESTART IDENTITY;

TRUNCATE TABLE RATING RESTART IDENTITY;


SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO rating (id,name, description) VALUES
   (1,'G','У фильма нет  возрастных ограничений'),
   (2,'PG','детям рекомендуется смотреть фильм с родителями'),
   (3,'PG-13','детям до 13 лет просмотр не желателен'),
   (4,'R','лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
   (5,'NC-17','лицам до 18 лет просмотр запрещён');
ALTER TABLE rating ALTER COLUMN id RESTART WITH 6;

INSERT INTO genre (id,name) VALUES
   (1,'Комедия'),
   (2,'Драма'),
   (3,'Мультфильм'),
   (4,'Триллер'),
   (5,'Документальный'),
   (6,'Боевик');
ALTER TABLE genre ALTER COLUMN id RESTART WITH 7;