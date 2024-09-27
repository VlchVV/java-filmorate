-- UserController

-- GET /users
select
	u.id,
	u.email,
	u.login,
	u.name,
	u.birthday_dt
from
	user u;

-- GET /users/{userId}
select
	u.id,
	u.email,
	u.login,
	u.name,
	u.birthday_dt
from
	user u
where
	u.id = :userId;

-- GET /users/{userId}/friends
select
	uf.id,
	uf.email,
	uf.login,
	uf.name,
	uf.birthday_dt
from
	user u
left join friends f
        on
	u.id = f.user_id
left join user uf
        on
	f.friend_id = u.id
where
	u.id = :userId;

-- GET /users/{userId}/friends/common/{otherId}
select
	uf.id,
	uf.email,
	uf.login,
	uf.name,
	uf.birthday_dt
from
	user u
left join friends f
        on
	u.id = f.user_id
left join user uf
        on
	f.friend_id = u.id
where
	u.id = :userId
intersect
select
	uf.id,
	uf.email,
	uf.login,
	uf.name,
	uf.birthday_dt
from
	user u
left join friends f
        on
	u.id = f.user_id
left join user uf
        on
	f.friend_id = u.id
where
	u.id = :otherId;

-- FilmController
-- GET /films
SELECT
	f.id,
	f.name,
	f.description,
	f.release_dt,
	f.duration,
	f.rating_id,
	r.name rating_name
FROM
	film f
JOIN rating r
	ON
	f.rating_id = r.id;

-- GET /films/{filmId}
SELECT
	f.id,
	f.name,
	f.description,
	f.release_dt,
	f.duration,
	f.rating_id,
	r.name rating_name
FROM
	film f
JOIN rating r
	ON
	f.rating_id = r.id
WHERE
	f.id = :filmId;

-- GET /films/popular?count={count}

select
	f.id,
	f.name,
	f.description,
	f.release_dt,
	f.duration,
	f.rating,
	fl.count_likes
from
	film f
left join (
	select
		fl.film_id,
		count(fl.film_id) count_likes
	from
		user_film_likes fl
	group by
		fl.film_id
) fl on
	f.id = fl.film_id
order by
	fl.count_likes desc,
	f.name
limit :count;