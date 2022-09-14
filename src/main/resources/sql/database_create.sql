DROP TABLE IF EXISTS team CASCADE;
CREATE TABLE team (
    team_id         serial      PRIMARY KEY,
    name            text        NOT NULL,
    coach           text
);

DROP TABLE IF EXISTS sportsman CASCADE;
CREATE TABLE sportsman (
    person_id       serial      PRIMARY KEY,
    current_team    integer     REFERENCES team(team_id) ON DELETE CASCADE,
    name            text        NOT NULL,
    birth_date      text
);

DROP TABLE IF EXISTS sports CASCADE;
CREATE TABLE sports (
    sport_id        serial      PRIMARY KEY,
    sport_name      text        NOT NULL,
    is_team_sport   boolean     NOT NULL
);

DROP TABLE IF EXISTS competition CASCADE;
CREATE TABLE competition (
    competition_id  serial      PRIMARY KEY,
    sport_id        integer     REFERENCES sports(sport_id) ON DELETE CASCADE,
    tournament      text,
    comp_date       text        NOT NULL,
    venue           text,
    score           text
);

DROP TABLE IF EXISTS seats CASCADE;
CREATE TABLE seats (
    seats_id        serial      PRIMARY KEY,
    competition_id  integer     REFERENCES competition(competition_id) ON DELETE CASCADE,
    seats_type      text        NOT NULL,
    seats_amount    integer     NOT NULL,
    seats_price     integer     NOT NULL
);

DROP TABLE IF EXISTS team_participance CASCADE;
CREATE TABLE team_participance (
    team_part_id    serial      PRIMARY KEY,
    person_id       integer     REFERENCES sportsman(person_id) ON DELETE CASCADE,
    team_id         integer     REFERENCES team(team_id) ON DELETE CASCADE,
    part_start      text        NOT NULL,
    part_finish     text        NOT NULL
);

DROP TABLE IF EXISTS team_competitions CASCADE;
CREATE TABLE team_competitions (
    team_comp_id   serial      PRIMARY KEY,
    team_id         integer     REFERENCES team(team_id) ON DELETE CASCADE,
    competition_id  integer     REFERENCES competition(competition_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS sportsmans_competitions CASCADE;
CREATE TABLE sportsmans_competitions (
    sport_part_id   serial      PRIMARY KEY,
    person_id       integer     REFERENCES sportsman(person_id) ON DELETE CASCADE,
    competition_id  integer     REFERENCES competition(competition_id) ON DELETE CASCADE
);