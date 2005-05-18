
-- Start of DDL Script for Constraint TEST.ID_NEWS_MN_FK
-- Generated 2-фев-2005 23:43:43 from TEST@mill

-- Drop the old instance of ID_NEWS_MN_FK
ALTER TABLE main_news
DROP CONSTRAINT id_news_mn_fk
/

ALTER TABLE main_news
ADD CONSTRAINT id_news_mn_fk FOREIGN KEY (id_news)
REFERENCES main_list_news (id_news)
on delete cascade

/
-- End of DDL script for Foreign Key(s)
