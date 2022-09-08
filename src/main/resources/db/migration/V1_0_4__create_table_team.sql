create table IF NOT EXISTS TRADING_SOLUTION.team
(
    team_id   long PRIMARY KEY auto_increment,
    team_name varchar(50),
    league_id long,

    CONSTRAINT league_id_fk FOREIGN KEY (league_id) REFERENCES TRADING_SOLUTION.league (league_id)

)