INSERT INTO team (name, coach) VALUES
    ('team 1', 'coach 1'),
    ('team 2', 'coach 2'),
    ('team 3', 'coach 3'),
    ('team 4', 'coach 4'),
    ('team 5', 'coach 5');

INSERT INTO sportsman (current_team, name, birth_date) VALUES
    (1, 'sportsman 1', '01.01.2000'),
    (1, 'sportsman 2', '02.01.2000'),
    (2, 'sportsman 3', '03.01.2000'),
    (2, 'sportsman 4', '04.01.2000'),
    (3, 'sportsman 5', '05.01.2000'),
    (3, 'sportsman 6', '06.01.2000'),
    (4, 'sportsman 7', '07.01.2000'),
    (4, 'sportsman 8', '08.01.2000'),
    (5, 'sportsman 9', '09.01.2000'),
    (5, 'sportsman 10', '10.01.2000');

INSERT INTO team_participance (person_id, team_id, part_start, part_finish) VALUES
    (1, 5, '10.10.2010', '20.10.2010'),
    (3, 2, '10.10.2010', '20.10.2010'),
    (5, 4, '10.10.2010', '20.10.2010'),
    (7, 1, '10.10.2010', '20.10.2010'),
    (9, 3, '10.10.2010', '20.10.2010');

INSERT INTO sports (sport_name, is_team_sport) VALUES
    ('tennis', 'false'),
    ('hockey', 'true'),
    ('football', 'true'),
    ('basketball', 'true'),
    ('golf', 'false');

INSERT INTO competition(sport_id, tournament, comp_date, venue, score) VALUES
    (1, 'tournament 1', '01.01.2023', 'venue 1', NULL),
    (2, 'tournament 2', '01.01.2015', 'venue 2',  '15:10'),
    (3, 'tournament 3', '01.01.2016', 'venue 3', '3:1'),
    (4, 'tournament 4', '01.01.2017', 'venue 4', '91:57'),
    (5, 'tournament 5', '01.01.2024', 'venue 5', NULL);

INSERT INTO seats(competition_id, seats_type, seats_amount, seats_price) VALUES
     (1,  'normal', 100,  500),
     (1, 'premium', 50, 1000),
     (2,  'normal', 100,  1000),
     (2, 'premium', 50, 2000),
     (3,  'normal', 100,  1500),
     (3, 'premium', 50, 3000);

INSERT INTO team_competitions (team_id, competition_id) VALUES
    (1, 2),
    (2, 2),
    (3, 3),
    (4, 3),
    (5, 4),
    (1, 4);

INSERT INTO sportsmans_competitions (person_id, competition_id) VALUES
    (1, 1),
    (4, 1),
    (6, 5),
    (9, 5);