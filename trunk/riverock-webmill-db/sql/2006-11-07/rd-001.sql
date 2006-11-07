create table wm_portal_ids
(
sequence_name varchar(50),
sequence_next_value decimal(10,0)
)
/


delete from wm_portal_ids
/


insert into wm_portal_ids
(select 'wm_portal_css' name, max(ID_SITE_CONTENT_CSS) from wm_portal_css)
/

insert into wm_portal_ids
(select 'wm_portal_xslt' name, max(ID_SITE_XSLT) from wm_portal_xslt)
/

insert into wm_portal_ids
(select 'wm_portal_site_language' name, max(ID_SITE_SUPPORT_LANGUAGE) from wm_portal_site_language)
/

alter table wm_portal_xslt
add column VERSION decimal(5,0) default 0
/

alter table wm_portal_xslt
add column XSLT_BLOB blob
/

