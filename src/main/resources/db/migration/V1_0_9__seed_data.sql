//SEED SPORT
insert into TRADING_SOLUTION.sport(sport_id, sport_name)
values (0, 'American Football');

//SEED LEAGUE
insert into TRADING_SOLUTION.league(league_id, league_name, sport_id)
values (0, 'NFL', 0);

//SEED TEAM
insert into TRADING_SOLUTION.team(team_id, team_name, league_id)
values (0, 'Tampa Bay Buccaneers', 0);

//SEED SQUAD
insert into TRADING_SOLUTION.squad(squad_id, squad_name, season, team_id)
values (0, 'Tampa Bay Buccaneers - 2022 squad', 2022, 0);

// SEED PLAYERS
insert into TRADING_SOLUTION.player(player_id, player_name, squad_id)
values (12, 'Tom Brady', 0);
insert into TRADING_SOLUTION.player(player_id, player_name, squad_id)
values (11, 'Blaine Gabbert', 0);
insert into TRADING_SOLUTION.player(player_id, player_name, squad_id)
values (2, 'Kyle Trask', 0);
insert into TRADING_SOLUTION.player(player_id, player_name, squad_id)
values (13, 'Mike Evans', 0);
insert into TRADING_SOLUTION.player(player_id, player_name, squad_id)
values (1, 'Jaelon Darden', 0);
insert into TRADING_SOLUTION.player(player_id, player_name, squad_id)
values (10, 'Scott Miller', 0);

//SEED POSITION (OFFENSE ONLY)
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (0, 'LWR', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (1, 'RWR', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (2, 'LT', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (3, 'LG', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (4, 'C', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (5, 'RG', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (6, 'RT', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (7, 'TE', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (9, 'QB', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (10, 'RB', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (11, 'FB', 0);
insert into TRADING_SOLUTION.position(position_id, position_name, sport_id)
values (12, 'HB', 0);

