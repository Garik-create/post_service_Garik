ALTER TABLE post ADD COLUMN verified BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE post ADD COLUMN verified_date TIMESTAMPTZ;