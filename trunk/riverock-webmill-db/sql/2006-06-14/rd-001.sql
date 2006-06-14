alter table wm_portal_template
add column is_default_dynamic decimal(1,0) not null default 0
/

update wm_portal_template a
set is_default_dynamic=1
where exists
(select b.id_site_template_member from wm_portal_template_member b 
where a.id_site_support_language=b.id_site_support_language and 
a.id_site_template=b.id_site_template
)
/

