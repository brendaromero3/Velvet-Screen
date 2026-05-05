-- ============================================================
-- Example Test Data
-- ============================================================

INSERT INTO persons (person_id, name, birthdate, biography) VALUES 
(5, 'Mason Thames', '2007-07-10', 'American actor and trained dancer'), 
(6, 'Nico Parker', '2004-12-09', 'English actress'), 
(7, 'Dean Deblois', '1970-07-07', 'Canadian writer, director, and producer'),
(8, 'Bonnie Arnold', '1955-01-01', 'American film producer and executive'),
(9, 'Helena Bonham Carter', '1966-05-26', 'English actress and BAFTA and SAG award winner'),
(10, 'Johnny Depp', '1963-07-09', 'American actor, musician, and filmmaker'),
(11, 'Mike Johnson', '1989-10-20', 'Academy award winner'),
(12, 'Tim Burton', '1958-08-25', 'American animator, filmmaker, and painter'),
(13, 'Allison Abbate', '1965-07-23', 'American film producer'),
(14, 'Jack Black', '1969-08-28', 'American actor, comedian, and musician'),
(15, 'Jared Hess', '1979-08-18', 'Filmmaker'),
(16, 'Vu Bui', '1977-07-26', 'Chief Content Officer of Mojang Studies'),
(17, 'Dylan Minnette', '1996-12-29', 'American actor and musician'),
(18, 'Rob Letterman', '1970-10-31', 'American film director and screenwriter'),
(19, 'Darren Lemke', '1969-03-16', 'American screenwriter'),
(20, 'Cardo', '1984-09-24', 'American record producer and rapper'),
(21, 'Takuya Kimura', '1972-11-13', 'Japanese actor, singer, and radio personality'),
(22, 'Emily Mortimer', '1971-02-28', 'English actress, writer, and filmmaker'),
(23, 'Hayao Miyazaki', '1941-01-05', 'Japanese animator, manga artist, and filmmaker'),
(24, 'Toshio Suzuki', '1948-08-19', 'Japanese film producer and studio executive');


INSERT INTO movies (movie_id, title, major_studio, original_language, release_date, length) VALUES 
(3, 'How to Train Your Dragon', true, 'English', '2025-06-13', 125),
(4, 'Corpse Bride', false, 'English', '2005-09-23', 83),
(5, 'A Minecraft Movie', false, 'Spanish', '2025-04-04', 150),
(6, 'Goosebumps', true, 'German', '2015-11-16', 121),
(7, 'Howls Moving Castle', true, 'Japanese', '2005-06-17', 203);

INSERT INTO theater (theater_id, max_occupancy, is_3d, fancy_sound) VALUES 
(4, 40, true, false),
(5, 58, false, false),
(6, 24, true, true),
(7, 85, false, true);

INSERT INTO awards (award_id, award_title, year) VALUES 
(3, 'Nebula Award for Best Script', 2007),
(4, 'Best Acting', 2005),
(5, 'Nickelodeon Kids Choice Awards for books', 2018),
(6, 'Best Animated Feature', 2005),
(7, 'Annie Award for Outstanding Achievement in Writing', 2025);

INSERT INTO awarded (person_id, award_id, movie_id, role) VALUES 
(23, 3, 7, 'Writer'),
(21, 4, 7, 'Actor'),
(22, 4, 7, 'Actress'),
(19, 5, 6, 'Writer'),
(13, 6, 4, 'Producer'),
(11, 6, 4, 'Director'),
(7, 7, 3, 'Writer');

INSERT INTO directors (person_id, movie_id) VALUES 
(7, 3),
(11, 4),
(15, 5),
(18, 6),
(23, 7);

INSERT INTO actors (person_id, movie_id, character_name) VALUES 
(5, 3, 'Hiccup'),
(6, 3, 'Astrid'),
(9, 4, 'Emily'),
(10, 4, 'Victor'),
(14, 5, 'Steve'),
(14, 6, 'Slappy'),
(17, 6, 'Zach'),
(21, 7, 'Howl'),
(22, 7, 'Sophie');

INSERT INTO writers (person_id, movie_id) VALUES 
(7, 3),
(12, 4),
(14, 5),
(19, 6),
(23, 7);

INSERT INTO producers (person_id, movie_id) VALUES 
(8, 3),
(13, 4),
(16, 5),
(20, 6),
(24, 7);

INSERT INTO movies_languages (language, movie_id) VALUES 
('Italian', 3),
('German', 3),
('French', 3),
('Italian', 4),
('Spanish', 4),
('French', 4),
('German', 5),
('Italian', 5),
('English', 6),
('English', 7),
('Spanish', 7),
('German', 7);

INSERT INTO movies_genre (genre, movie_id) VALUES 
('Action', 3),
('Thriller', 3),
('Drama', 4),
('Sci-Fi', 5),
('Comedy', 5),
('Crime', 6),
('Comedy', 6),
('Horror', 6),
('Romance', 7),
('Muscial', 7);

INSERT INTO screening (screening_id, movie_id, theater_id, date_time) VALUES 
(3, 3, 4, '2026-04-28 14:00:00'),
(4, 3, 6, '2026-04-30 10:00:00'),
(5, 3, 4, '2026-05-01 18:30:00'),
(6, 3, 5, '2026-04-28 14:00:00'),
(7, 4, 6, '2026-04-25 19:00:00'),
(8, 4, 7, '2026-04-24 16:00:00'),
(9, 4, 7, '2026-04-28 17:00:00'),
(10, 5, 4, '2026-05-03 13:30:00'),
(11, 5, 5, '2026-05-08 12:00:00'),
(12, 5, 6, '2026-04-26 15:00:00'),
(13, 5, 7, '2026-04-27 11:30:00'),
(14, 6, 6, '2026-05-02 09:00:00'),
(15, 6, 6, '2026-05-10 18:00:00'),
(16, 6, 7, '2026-04-26 17:00:00'),
(17, 6, 7, '2026-05-04 14:30:00'),
(18, 7, 6, '2026-05-11 15:00:00'),
(19, 7, 6, '2026-05-11 12:00:00'),
(20, 7, 6, '2026-05-06 18:00:00'),
(21, 7, 7, '2026-04-26 11:00:00'),
(22, 7, 5, '2026-05-09 13:30:00'),
(23, 7, 5, '2026-05-12 15:40:00');

INSERT INTO users (user_id, email_address, password) VALUES
(4, 'ashley@gmail.com', 'eevee'),
(5, 'lena@gmail.com', 'smiski'),
(6, 'brenda@gmai.com', 'lego'),
(7, 'admin@uic.edu', 'adminpass'),
(8, 'bob@yahoo.com', '9874'),
(9, 'sarah@uic.edu', 'hello');

INSERT INTO Reviews (user_id, movie_id, rating, comment) VALUES 
(4, 3, 3, 'Too much action'),
(4, 5, 4, 'I love Minecraft!!'),
(4, 7, 5, 'Great music'),
(5, 6, 0, 'Too scary'),
(5, 7, 3, 'Not long enough'),
(5, 4, 5, 'Very interesting'),
(5, 5, 2, 'Poor quality'),
(6, 3, 4, 'Good movie'),
(6, 4, 2, 'Not enough action'),
(6, 5, 1, 'Not accurate'),
(6, 6, 0, 'Very bad'),
(6, 7, 5, 'Amazing');

INSERT INTO administrators (user_id) VALUES 
(7),
(8),
(9);

INSERT INTO administrator_manages (user_id, theater_id) VALUES 
(7, 4),
(7, 5),
(7, 6),
(8, 7),
(9, 5),
(9, 6);

INSERT INTO clients (user_id, name, address, interests) VALUES 
(4, 'Ashley Daly', '1234 S 9th st', 'Thriller, Action'),
(5, 'Lena Tran', '5678 W 11th st', 'Drama, Comedy'),
(6, 'Brenda Romero', '91011 N 3rd st', 'Horror, Crime');

INSERT INTO membership (membership_id, user_id, movies_watched, num_free_earned) VALUES 
(4, 4, 20, 2),
(5, 5, 15, 1),
(6, 6, 17, 1);

INSERT INTO card_info (card_number, user_id) VALUES 
('1234567', 4),
('abcd123', 4),
('efgh567', 5),
('6vk83f75', 5),
('4b83863', 6);

INSERT INTO credit (card_number, billing_address) VALUES 
('abcd123', '1234 S 9th st'),
('6vk83f75', '5678 W 11th st');

INSERT INTO ticket_sale (sale_id, user_id, screening_id, num_tickets, purchase_date, total_price, card_number) VALUES 
(2, 4, 3, 1, '2026-04-20 10:00:00', 45.00, '1234567'),
(3, 4, 12, 1, '2026-04-18 16:43:26', 20.00, 'abcd123'),
(4, 4, 21, 2, '2026-04-05 19:27:29', 40.00, '1234567'),
(5, 5, 15, 3, '2026-04-21 23:19:00', 60.00, 'efgh567'),
(6, 5, 20, 1, '2026-04-16 01:39:15', 23.00, 'efgh567'),
(7, 5, 8, 2, '2026-04-19 18:52:56', 45.00, '6vk83f75'),
(8, 5, 13, 1, '2026-04-01 09:58:35', 18.00, 'efgh567'),
(9, 6, 5, 1, '2026-03-29 11:26:55', 15.00, '4b83863'),
(10, 6, 7, 4, '2026-03-01 16:59:26', 70.00, '4b83863'),
(11, 6, 10, 5, '2026-04-27 06:27:27', 100.00, '4b83863'),
(12, 6, 16, 2, '2026-04-19 20:01:11', 36.00, '4b83863'),
(13, 6, 23, 1, '2026-04-03 14:29:46', 15.00, '4b83863');