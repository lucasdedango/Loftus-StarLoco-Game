USE starloco_login;

ALTER TABLE `world_players` ADD COLUMN `tileman_credits` int(11) NOT NULL DEFAULT 0;
ALTER TABLE `world_players` ADD COLUMN `tileman_credit_xp` bigint(20) NOT NULL DEFAULT 0;
