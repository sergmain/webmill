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
