create table IF NOT EXISTS TRADING_SOLUTION.league
(
    league_id   long PRIMARY KEY auto_increment,
    league_name varchar(50),
    sport_id    long,

    CONSTRAINT sport_id_league_fk FOREIGN KEY (sport_id) REFERENCES TRADING_SOLUTION.sport (sport_id)
)