update SITE_CONTENT_CSS a
set ID_SITE_SUPPORT_LANGUAGE = 
(select z.ID_SITE_SUPPORT_LANGUAGE 
from SITE_SUPPORT_LANGUAGE z
where z.id_site=a.id_site and z.id_language=a.id_language
)
/

commit
/



