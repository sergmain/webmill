
-- Start of DDL Script for Constraint TEST.ID_CTX_LANG_CATALOG_SCC_FK
-- Generated 2-фев-2005 22:42:41 from TEST@mill

-- Drop the old instance of ID_CTX_LANG_CATALOG_SCC_FK
ALTER TABLE site_ctx_catalog
DROP CONSTRAINT id_ctx_lang_catalog_scc_fk
/

ALTER TABLE site_ctx_catalog
ADD CONSTRAINT id_ctx_lang_catalog_scc_fk FOREIGN KEY (id_site_ctx_lang_catalog)
REFERENCES site_ctx_lang_catalog (id_site_ctx_lang_catalog)
/
-- End of DDL script for Foreign Key(s)
