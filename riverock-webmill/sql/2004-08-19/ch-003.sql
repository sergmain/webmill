update SITE_CONTENT_CSS a
set ID_LANGUAGE=
(select min(id_language) 
from SITE_SUPPORT_LANGUAGE z
where a.id_site=z.id_site)
/
