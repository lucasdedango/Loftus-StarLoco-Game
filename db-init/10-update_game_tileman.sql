USE starloco_login;

CREATE TABLE IF NOT EXISTS `player_unlocked_maps` (
  `player_id` int(11) NOT NULL,
  `map_id` int(11) NOT NULL,
  PRIMARY KEY (`player_id`, `map_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

