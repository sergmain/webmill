
-- Start of DDL Script for Constraint TEST.ID_FORUM_MFT_FK
-- Generated 3-фев-2005 1:18:00 from TEST@mill

-- Drop the old instance of ID_FORUM_MFT_FK
ALTER TABLE main_forum_threads
DROP CONSTRAINT id_forum_mft_fk
/

ALTER TABLE main_forum_threads
ADD CONSTRAINT id_forum_mft_fk FOREIGN KEY (id_forum)
REFERENCES main_forum (id_forum) on delete cascade
/
-- End of DDL script for Foreign Key(s)
