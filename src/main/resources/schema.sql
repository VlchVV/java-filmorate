CREATE TABLE IF NOT EXISTS "user" (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email VARCHAR,
login VARCHAR,
name VARCHAR,
birthday_dt DATE,
created_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rating (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR,
description VARCHAR,
created_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS film (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR,
description VARCHAR,
release_dt DATE,
duration INT,
rating_id INTEGER,
created_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS genre (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR,
created_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS friends (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
user_id INTEGER,
friend_id INTEGER,
status VARCHAR,
created_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_film_likes (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
user_id INTEGER,
film_id INTEGER,
created_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS film_genre (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
film_id INTEGER,
genre_id INTEGER,
created_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
