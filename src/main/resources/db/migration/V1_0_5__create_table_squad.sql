create table IF NOT EXISTS TRADING_SOLUTION.squad
(
    squad_id   long PRIMARY KEY auto_increment,
    squad_name varchar(50),
    team_id    long,
    season     int,

    CONSTRAINT team_id_fk FOREIGN KEY (team_id) REFERENCES TRADING_SOLUTION.team (team_id)

)