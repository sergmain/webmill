
-- Start of DDL Script for Constraint TEST.ID_SITE_TEMPLATE_STM_FK
-- Generated 3-фев-2005 13:31:04 from TEST@mill

-- Drop the old instance of ID_SITE_TEMPLATE_STM_FK
ALTER TABLE site_template_member
DROP CONSTRAINT id_site_template_stm_fk
/

ALTER TABLE site_template_member
ADD CONSTRAINT id_site_template_stm_fk FOREIGN KEY (id_site_template)
REFERENCES site_template (id_site_template) ON DELETE CASCADE
/
-- End of DDL script for Foreign Key(s)
