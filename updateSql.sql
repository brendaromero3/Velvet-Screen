ALTER TABLE screening
ADD COLUMN seats_left integer NOT NULL DEFAULT 0,
ADD CONSTRAINT seat_check CHECK (seats_left >= 0);

UPDATE screening s
SET seats_left = (SELECT (t.max_occupancy - COALESCE(SUM(ts.num_tickets), 0))
FROM theater t
LEFT JOIN ticket_sale ts ON ts.screening_id = s.screening_id
WHERE t.theater_id = s.theater_id
GROUP BY max_occupancy);