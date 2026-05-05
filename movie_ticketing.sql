-- ============================================================
-- CS480 / Spring 2026 
-- Movie Ticketing System
-- ============================================================

CREATE TABLE persons (
    person_id   integer NOT NULL CHECK (person_id > 0),
    name        text NOT NULL,
    birthdate   date NOT NULL,
    biography   text,
    PRIMARY KEY (person_id)
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

CREATE TABLE theater (
    theater_id      integer NOT NULL CHECK (theater_id > 0),
    max_occupancy   integer NOT NULL,
    is_3d           boolean NOT NULL,
    fancy_sound     boolean NOT NULL,
    PRIMARY KEY (theater_id)
);

CREATE TABLE awards (
    award_id        integer NOT NULL CHECK (award_id > 0),
    award_title     text NOT NULL,
    year            integer NOT NULL,
    PRIMARY KEY (award_id)
);

CREATE TABLE awarded (
    person_id   integer NOT NULL REFERENCES persons(person_id) ON DELETE CASCADE,
    award_id    integer NOT NULL REFERENCES awards(award_id),
    movie_id    integer NOT NULL REFERENCES movies(movie_id) ON DELETE CASCADE,
    role        text,
    PRIMARY KEY (person_id, award_id, movie_id)
);

CREATE TABLE directors (
    person_id   integer NOT NULL REFERENCES persons(person_id) ON DELETE CASCADE,
    movie_id    integer NOT NULL REFERENCES movies(movie_id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, movie_id)
);

CREATE TABLE actors (
    person_id   integer NOT NULL REFERENCES persons(person_id) ON DELETE CASCADE,
    movie_id    integer NOT NULL REFERENCES movies(movie_id) ON DELETE CASCADE,
    character_name  text,
    PRIMARY KEY (person_id, movie_id, character_name)
);

CREATE TABLE writers (
    person_id   integer NOT NULL REFERENCES persons(person_id) ON DELETE CASCADE,
    movie_id    integer NOT NULL REFERENCES movies(movie_id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, movie_id)
);

CREATE TABLE producers (
    person_id   integer NOT NULL REFERENCES persons(person_id) ON DELETE CASCADE,
    movie_id    integer NOT NULL REFERENCES movies(movie_id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, movie_id)
);

CREATE TABLE movies_languages (
    language    text NOT NULL,
    movie_id    integer NOT NULL REFERENCES movies(movie_id) ON DELETE CASCADE,
    PRIMARY KEY (language, movie_id)
);

CREATE TABLE movies_genre (
    genre       text NOT NULL,
    movie_id    integer NOT NULL REFERENCES movies(movie_id) ON DELETE CASCADE,
    PRIMARY KEY (genre, movie_id)
);

CREATE TABLE screening (
    screening_id    integer NOT NULL CHECK (screening_id > 0),
    movie_id        integer NOT NULL REFERENCES movies(movie_id) ON DELETE CASCADE,
    theater_id      integer NOT NULL REFERENCES theater(theater_id),
    date_time       timestamp NOT NULL CHECK (date_time::time >= TIME '08:00' AND date_time::time < TIME '24:00'),
    seats_left      integer NOT NULL CHECK (seats_left >= 0),
    PRIMARY KEY (screening_id)
);

CREATE TABLE users (
    user_id         integer NOT NULL CHECK (user_id > 0),
    email_address   text UNIQUE NOT NULL,
    password        text NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE Reviews (
    user_id         integer NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    movie_id        integer NOT NULL REFERENCES movies(movie_id) ON DELETE CASCADE,
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

CREATE TABLE membership (
    membership_id       integer NOT NULL CHECK (membership_id > 0),
    user_id             integer UNIQUE NOT NULL REFERENCES users(user_id),
    movies_watched      integer,
    num_free_earned     integer,
    PRIMARY KEY (membership_id)
);

CREATE TABLE card_info (
    card_number     text NOT NULL,
    user_id         integer REFERENCES users(user_id),
    PRIMARY KEY (card_number)
);

CREATE TABLE credit (
    card_number         text NOT NULL REFERENCES card_info(card_number),
    billing_address     text NOT NULL,
    PRIMARY KEY (card_number)
);

CREATE TABLE ticket_sale (
    sale_id         integer NOT NULL CHECK (sale_id > 0),
    user_id         integer REFERENCES users(user_id),
    screening_id    integer NOT NULL REFERENCES screening(screening_id) ON DELETE CASCADE,
    num_tickets     integer NOT NULL CHECK (num_tickets >= 1),
    purchase_date   timestamp NOT NULL,
    total_price     numeric(10,2) NOT NULL CHECK (total_price >= 0),
    card_number     text,
    PRIMARY KEY (sale_id)
);

-- ============================================================
-- Indexes
-- ============================================================

-- search movies by title
CREATE INDEX idx_movies_title ON movies(title);

-- filter screenings by date
CREATE INDEX idx_screening_datetime ON screening(date_time);

-- most likely used foreign keys
CREATE INDEX idx_ticket_user        ON ticket_sale(user_id);
CREATE INDEX idx_actors_movie       ON actors(movie_id);
CREATE INDEX idx_awards_person      ON awarded(person_id);
CREATE INDEX idx_screening_movie    ON screening(movie_id);
CREATE INDEX idx_screening_theater  ON screening(theater_id);
CREATE INDEX idx_ticket_screening   ON ticket_sale(screening_id);


-- ============================================================
-- Example Test Data
-- ============================================================
INSERT INTO persons VALUES 
(1, 'person1', '2000-12-04', 'Actor'), 
(2, 'person2', '1999-11-03', 'Director'), 
(3, 'person3', '1998-10-02', 'Writer'),
(4, 'person4', '1997-09-01', 'Producer');

INSERT INTO movies VALUES 
(1, 'Insidious', false, 'English', '2011-04-01', 103),  
(2, 'Movie2', true, 'Spanish', '2025-05-02', 114);

INSERT INTO theater VALUES 
(1, 600, true, false), 
(2, 1000, true, true), 
(3, 200, false, false);

INSERT INTO awards VALUES 
(1, 'Oscar', 2011), 
(2, 'Academy Awards', 2005);

INSERT INTO awarded VALUES 
(1, 1, 2, 'Actor'), 
(2, 1, 1, 'Director'), 
(3, 2, 1, 'Writer');

INSERT INTO directors VALUES 
(2, 1);

INSERT INTO actors VALUES 
(1, 1, 'Josh');

INSERT INTO writers VALUES 
(3, 2);

INSERT INTO producers VALUES 
(4, 2);

INSERT INTO movies_languages VALUES 
('English', 1),
('Spanish', 1), 
('Spanish', 2),
('French', 2);

INSERT INTO movies_genre VALUES 
('Horror', 1),  
('Comedy', 2);

INSERT INTO screening VALUES 
(1, 1, 2, '2026-04-15 17:00:00', 598),
(2, 2, 3, '2023-12-25 12:00:00', 200);

INSERT INTO users VALUES
(1, 'email1@gmail.com', '1234'),
(2, 'email2@uid.edu', 'abcd');

INSERT INTO Reviews VALUES 
(1, 1, 1, 'too scary'), 
(2, 1, 5, 'horror!');

INSERT INTO administrators VALUES 
(2);

INSERT INTO administrator_manages VALUES 
(2, 2);

INSERT INTO clients VALUES 
(1, 'client1', '1234 st', 'not horror'),
(2, 'client2', 'abcd st', 'horror, comedy');

INSERT INTO membership VALUES 
(1, 1, 5, 1), 
(2, 2, 1, 0);

INSERT INTO card_info VALUES 
('123456', 1), 
('abcdef', 2);

INSERT INTO credit VALUES 
('123456', '1234 st'), 
('abcdef', 'abcd st');

INSERT INTO ticket_sale VALUES 
(1, 1, 1, 2, '2026-04-14 10:00:00', 25.00, '123456');