
-- Start of DDL Script for Constraint TEST.ID_SITE_SUPPORT_LANG_SCA_FK
-- Generated 2-фев-2005 22:52:57 from TEST@mill

ALTER TABLE site_ctx_article
ADD CONSTRAINT id_site_support_lang_sca_fk FOREIGN KEY (id_site_support_language)
REFERENCES site_support_language (id_site_support_language)
/
-- End of DDL script for Foreign Key(s)
