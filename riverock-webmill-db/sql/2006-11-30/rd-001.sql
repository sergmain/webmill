insert into wm_portal_ids
(select 'wm_portlet_webclip' name, max(ID_WEBCLIP)+1 max_value from wm_portlet_webclip)
/

alter table wm_portlet_webclip
add VERSION decimal(5,0) default 0
/

alter table wm_portlet_webclip
add WEBCLIP_BLOB blob
/

drop table wm_portlet_webclip_data
/