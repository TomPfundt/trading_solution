create table IF NOT EXISTS TRADING_SOLUTION.position
(
    position_id   long PRIMARY KEY auto_increment,
    position_name varchar(50) unique,
    sport_id      long,

    CONSTRAINT sport_id_position_fk FOREIGN KEY (sport_id) REFERENCES TRADING_SOLUTION.sport (sport_id)
)