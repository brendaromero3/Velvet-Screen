-- ============================================================
-- CS480 / Spring 2026 
-- Movie Ticketing System
-- ============================================================

CREATE TABLE persons (
    name        text NOT NULL,
    birthdate   text NOT NULL,
    biography   text,
    PRIMARY KEY (name, birthdate)
);

CREATE TABLE awards (
    award_title     text NOT NULL,
    year            integer NOT NULL,
    PRIMARY KEY (award_title, year)
);

CREATE TABLE awarded (
    name            text NOT NULL REFERENCES persons(name),
    birthdate       text NOT NULL REFERENCES persons(birthdate),
    award           text NOT NULL REFERENCES awards(award_title),
    year            integer NOT NULL REFERENCES awards(year),
    title           text NOT NULL REFERENCES movies(title),
    release_date    date NOT NULL REFERENCES movies(release_date),
    PRIMARY KEY (name, birthdate, award_title, year, title, release_date)
);

CREATE TABLE directors (
    name            text NOT NULL REFERENCES persons(name),
    birthdate       text NOT NULL REFERENCES persons(birthdate),
    title           text NOT NULL REFERENCES movies(title),
    release_date    date NOT NULL REFERENCES movies(release_date),
    PRIMARY KEY (name, birthdate, title, release_date)
);

CREATE TABLE actors (
    name            text NOT NULL REFERENCES persons(name),
    birthdate       text NOT NULL REFERENCES persons(birthdate),
    character_name  text,
    title           text NOT NULL REFERENCES movies(title),
    release_date    date NOT NULL REFERENCES movies(release_date),
    PRIMARY KEY (name, birthdate, title, release_date)
);

CREATE TABLE writers (
    name            text NOT NULL REFERENCES persons(name),
    birthdate       text NOT NULL REFERENCES persons(birthdate),
    title           text NOT NULL REFERENCES movies(title),
    release_date    date NOT NULL REFERENCES movies(release_date),
    PRIMARY KEY (name, birthdate, title, release_date)
);

CREATE TABLE producers (
    name            text NOT NULL REFERENCES persons(name),
    birthdate       text NOT NULL REFERENCES persons(birthdate),
    title           text NOT NULL REFERENCES movies(title),
    release_date    date NOT NULL REFERENCES movies(release_date),
    PRIMARY KEY (name, birthdate, title, release_date)
);

CREATE TABLE movies (
    title               text NOT NULL,
    major_studio        boolean NOT NULL, 
    original_language   text NOT NULL,
    release_date        date NOT NULL,
    length              integer NOT NULL CHECK (length > 0),
    is_old              boolean NOT NULL,
    PRIMARY KEY (title, release_date)
);

CREATE TABLE movies_langauges (
    language        text NOT NULL,
    title           text NOT NULL REFERENCES movies(title),
    release_date    date NOT NULL REFERENCES movies(release_date),
    PRIMARY KEY (language, title, release_date)
);

CREATE TABLE movies_genre (
    genre           text NOT NULL,
    title           text NOT NULL REFERENCES movies(title),
    release_date    date NOT NULL REFERENCES movies(release_date),
    PRIMARY KEY (genre, title, release_date)
);

CREATE TABLE screening (
    screeningID     integer PRIMARY KEY CHECK (screeningID > 0),
    title           text NOT NULL REFERENCES movies(title),
    release_date    date NOT NULL REFERENCES movies(release_date),
    theaterID       integer NOT NULL REFERENCES theater(theaterID),
    date_time       timestamp NOT NULL
);

CREATE TABLE theater (
    theaterID       integer PRIMARY KEY CHECK (theaterID > 0),
    max_occupancy   integer NOT NULL
);

CREATE TABLE three_d_theaters (
    theaterID   integer NOT NULL REFERENCES theater(theaterID)
);

CREATE TABLE fancy_sound (
    theaterID   integer NOT NULL REFERENCES theater(theaterID)
);

CREATE TABLE users (
    email_address   text NOT NULL,
    password        text NOT NULL,
    PRIMARY KEY (email_address, password)
);

CREATE TABLE Reviews (
    email_address   text NOT NULL REFERENCES users(email_address),
    title           text NOT NULL REFERENCES movies(title),
    release_date    date NOT NULL REFERENCES movies(release_date),
    rating          integer NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment         text,
    PRIMARY KEY (email_address, title, release_date)
);

CREATE TABLE administrators (
    email_address   text NOT NULL REFERENCES users(email_address),
    theaterID       integer NOT NULL REFERENCES theater(theaterID),
    PRIMARY KEY (email_address, theaterID)
);

CREATE TABLE clients (
    email_address   text NOT NULL REFERENCES users(email_address),
    name            text NOT NULL,
    address         text NOT NULL,
    interests       text,
    PRIMARY KEY (email_address, name, address)
);

CREATE TABLE buys_ticket (
    name            text NOT NULL REFERENCES clients(name),
    address         text NOT NULL REFERENCES clients(address),
    screeningID     integer NOT NULL REFERENCES screening(screeningID),
    seat_number     integer NOT NULL CHECK (seat_number >= 1),
    purchase_date   timestamp NOT NULL,
    total_price     numeric(10,2) NOT NULL CHECK (total_price >= 0),
    PRIMARY KEY (name, address, screeningID)
);

CREATE TABLE membership (
    membershipID        integer NOT NULL CHECK (membershipID > 0),
    name                text NOT NULL REFERENCES clients(name),
    address             text NOT NULL REFERENCES clients(address),
    movies_watched      integer,
    num_free_earned     integer,
    PRIMARY KEY (membershipID, name, address)
);

CREATE TABLE card_info (
    card_number         text NOT NULL,
    name                text NOT NULL REFERENCES clients(name),
    address             text NOT NULL REFERENCES clients(address),
    PRIMARY KEY (card_number, name, address)
);

CREATE TABLE credit (
    card_number         text NOT NULL REFERENCES card_info(card_number),
    billing_address     text NOT NULL,
    PRIMARY KEY (card_number)
);
