ALTER TABLE album
    ADD visibility varchar(64) NOT NULL DEFAULT 0,
    ADD beholders_ids JSON;