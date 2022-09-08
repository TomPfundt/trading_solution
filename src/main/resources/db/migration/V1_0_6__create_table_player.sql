create table IF NOT EXISTS TRADING_SOLUTION.player
(
    player_id   long PRIMARY KEY auto_increment,
    player_name varchar(50),
    squad_id    long,

    CONSTRAINT squad_id_fk FOREIGN KEY (squad_id) REFERENCES TRADING_SOLUTION.squad (squad_id)

)