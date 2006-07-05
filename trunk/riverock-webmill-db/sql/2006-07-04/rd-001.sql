alter table wm_portal_list_site 
modify DEF_COUNTRY varchar(6) default ''
/

ALTER TABLE  wm_portal_list_site
 ADD (
  PROPERTIES VARCHAR2 (2000)
 )
/