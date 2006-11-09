create table wm_portal_ids
(
sequence_name varchar(50),
sequence_next_value decimal(10,0)
)
/


delete from wm_portal_ids
/


insert into wm_portal_ids
(select 'wm_portal_css' name, max(ID_SITE_CONTENT_CSS)+1 max_value from wm_portal_css)
/

insert into wm_portal_ids
(select 'wm_portal_xslt' name, max(ID_SITE_XSLT)+1 max_value from wm_portal_xslt)
/

insert into wm_portal_ids
(select 'wm_portal_site_language' name, max(ID_SITE_SUPPORT_LANGUAGE)+1 max_value from wm_portal_site_language)
/

insert into wm_portal_ids
(select 'wm_portal_site' name, max(ID_SITE)+1 max_value from wm_portal_list_site)
/

insert into wm_portal_ids
(select 'wm_portal_virtual_host' name, max(ID_SITE_VIRTUAL_HOST)+1 max_value from wm_portal_virtual_host)
/

insert into wm_portal_ids
(select 'wm_portal_portlet_name' name, max(ID_SITE_CTX_TYPE)+1 max_value from wm_portal_portlet_name)
/

insert into wm_portal_ids
(select 'wm_auth_user' name, max(ID_AUTH_USER)+1 max_value from wm_auth_user)
/

insert into wm_portal_ids
(select 'wm_list_user' name, max(ID_USER)+1 max_value from wm_list_user)
/

insert into wm_portal_ids
(select 'wm_list_r_holding_company' name, max(ID_REL_HOLDING)+1 max_value from wm_list_r_holding_company)
/

insert into wm_portal_ids
(select 'wm_list_company' name, max(ID_FIRM)+1 max_value from wm_list_company)
/

insert into wm_portal_ids
(select 'wm_list_holding' name, max(ID_HOLDING)+1 max_value from wm_list_holding)
/

insert into wm_portal_ids
(select 'wm_auth_relate_accgroup' name, max(ID_RELATE_ACCGROUP)+1 max_value from wm_auth_relate_accgroup)
/

insert into wm_portal_ids
(select 'wm_auth_access_group' name, max(ID_ACCESS_GROUP)+1 max_value from wm_auth_access_group)
/

alter table wm_portal_xslt
add column VERSION decimal(5,0) default 0
/

alter table wm_portal_xslt
add column XSLT_BLOB blob
/

