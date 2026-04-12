-- ============================================================
-- CS480 / Spring 2026 
-- Movie Ticketing System
-- ============================================================

CREATE TABLE persons (
    person_id   integer NOT NULL CHECK (person_id > 0),
    name        text NOT NULL,
    birthdate   text NOT NULL,
    biography   text,
    PRIMARY KEY (person_id)
);

CREATE TABLE awards (
    award_id        integer NOT NULL CHECK (award_id > 0),
    award_title     text NOT NULL,
    year            integer NOT NULL,
    PRIMARY KEY (award_id)
);

CREATE TABLE awarded (
    person_id   integer NOT NULL REFERENCES persons(person_id),
    award_id    integer NOT NULL REFERENCES awards(award_id),
    movie_id    integer NOT NULL REFERENCES movies(movie_id),
    PRIMARY KEY (person_id, award_id, movie_id)
);

CREATE TABLE directors (
    person_id   integer NOT NULL REFERENCES persons(person_id),
    movie_id    integer NOT NULL REFERENCES movies(movie_id),
    PRIMARY KEY (person_id, movie_id)
);

CREATE TABLE actors (
    person_id   integer NOT NULL REFERENCES persons(person_id),
    movie_id    integer NOT NULL REFERENCES movies(movie_id),
    character_name  text,
    PRIMARY KEY (person_id, movie_id)
);

CREATE TABLE writers (
    person_id   integer NOT NULL REFERENCES persons(person_id),
    movie_id    integer NOT NULL REFERENCES movies(movie_id),
    PRIMARY KEY (person_id, movie_id)
);

CREATE TABLE producers (
    person_id   integer NOT NULL REFERENCES persons(person_id),
    movie_id    integer NOT NULL REFERENCES movies(movie_id),
    PRIMARY KEY (person_id, movie_id)
);

CREATE TABLE movies (
    movie_id            integer NOT NULL CHECK (movie_id > 0),
    title               text NOT NULL,
    major_studio        boolean NOT NULL, 
    original_language   text NOT NULL,
    release_date        date NOT NULL,
    length              integer NOT NULL CHECK (length > 0),
    PRIMARY KEY (movie_id)
);

CREATE TABLE movies_langauges (
    language    text NOT NULL,
    movie_id    integer NOT NULL REFERENCES movies(movie_id),
    PRIMARY KEY (language, movie_id)
);

CREATE TABLE movies_genre (
    genre       text NOT NULL,
    movie_id    integer NOT NULL REFERENCES movies(movie_id),
    PRIMARY KEY (genre, movie_id)
);

CREATE TABLE screening (
    screening_id    integer PRIMARY KEY CHECK (screening_id > 0),
    movie_id        integer NOT NULL REFERENCES movies(movie_id),
    theater_id      integer NOT NULL REFERENCES theater(theater_id),
    date_time       timestamp NOT NULL,
    PRIMARY KEY (screening_id)
);

CREATE TABLE theater (
    theater_id      integer PRIMARY KEY CHECK (theater_id > 0),
    max_occupancy   integer NOT NULL,
    is_3d           boolean NOT NULL,
    fancy_sound     boolean NOT NULL,
    PRIMARY KEY (theater_id)
);

CREATE TABLE users (
    user_id         integer NOT NULL CHECK (user_id > 0),
    email_address   text UNIQUE NOT NULL,
    password        text NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE Reviews (
    user_id         integer NOT NULL REFERENCES users(user_id),
    movie_id        integer NOT NULL REFERENCES movies(movie_id),
    rating          integer NOT NULL CHECK (rating BETWEEN 0 AND 5),
    comment         text,
    PRIMARY KEY (user_id, movie_id)
);

CREATE TABLE administrators (
    user_id     integer NOT NULL REFERENCES users(user_id),
    PRIMARY KEY (user_id)
);

CREATE TABLE administrator_manages (
    user_id         integer NOT NULL REFERENCES users(user_id),
    theater_id      integer NOT NULL REFERENCES theater(theater_id),
    PRIMARY KEY (user_id, theater_id)
);

CREATE TABLE clients (
    user_id         integer NOT NULL REFERENCES users(user_id),
    name            text NOT NULL,
    address         text NOT NULL,
    interests       text,
    PRIMARY KEY (user_id)
);

CREATE TABLE ticket_sale (
    sale_id         integer NOT NULL CHECK (sale_id > 0),
    user_id         integer REFERENCES users(user_id),
    screening_id    integer NOT NULL REFERENCES screening(screening_id),
    num_tickets     integer NOT NULL CHECK (num_tickets >= 1),
    purchase_date   timestamp NOT NULL,
    total_price     numeric(10,2) NOT NULL CHECK (total_price >= 0),
    card_number     text NOT NULL REFERENCES card_info(card_number),
    PRIMARY KEY (sale_id)
);

CREATE TABLE membership (
    membership_id       integer NOT NULL CHECK (membership_id > 0),
    user_id             integer UNIQUE NOT NULL REFERENCES users(user_id),
    movies_watched      integer,
    num_free_earned     integer,
    PRIMARY KEY (membership_id)
);

CREATE TABLE card_info (
    card_number     text NOT NULL,
    user_id         integer NOT NULL REFERENCES users(user_id),
    PRIMARY KEY (card_number, user_id)
);

CREATE TABLE credit (
    card_number         text NOT NULL REFERENCES card_info(card_number),
    billing_address     text NOT NULL,
    PRIMARY KEY (card_number)
);
