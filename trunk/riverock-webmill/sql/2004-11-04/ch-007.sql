insert into SITE_CONTENT_CSS_DATA
(ID_SITE_CONTENT_CSS_DATA, ID_SITE_CONTENT_CSS, CSS_DATA)

select SEQ_SITE_CONTENT_CSS_DATA.nextval, z.ID_SITE_CONTENT_CSS, x.CSS_DATA
from SITE_CONTENT_CSS_DATA x, SITE_CONTENT_CSS z 
where x.id_site_content_css=z.old_id_site_context_css and 
z.id_site_content_css in
(
   select a.id_site_content_css from SITE_CONTENT_CSS a
   where a.id_site_content_css!=a.old_id_site_context_css
)
/

commit
/





