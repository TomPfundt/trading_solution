create table IF NOT EXISTS TRADING_SOLUTION.player_position
(
    player_id   long,
    position_id long,
    squad_id    long,
    depth       int,
    PRIMARY KEY (player_id, position_id),

    CONSTRAINT player_id_fk FOREIGN KEY (player_id) REFERENCES TRADING_SOLUTION.player (player_id),
    CONSTRAINT position_id_fk FOREIGN KEY (position_id) REFERENCES TRADING_SOLUTION.position (position_id),
    CONSTRAINT pp_squad_id_fk FOREIGN KEY (squad_id) REFERENCES TRADING_SOLUTION.squad (squad_id)
)