insert into SITE_CONTENT_CSS
(ID_SITE_CONTENT_CSS, ID_SITE, IS_CURRENT, DATE_POST, 
TEXT_COMMENT, CSS_DATA,  
ID_LANGUAGE, OLD_ID_SITE_CONTEXT_CSS)

select SEQ_SITE_CONTENT_CSS.nextval, z.ID_SITE, z.IS_CURRENT, z.DATE_POST,
z.TEXT_COMMENT, z.CSS_DATA, x.ID_LANGUAGE, z.OLD_ID_SITE_CONTEXT_CSS
from SITE_CONTENT_CSS z, SITE_SUPPORT_LANGUAGE x
where z.id_site=x.id_site and
not exists (
select null from SITE_CONTENT_CSS aa 
where aa.id_site=x.id_site and aa.id_language=x.id_language

)


