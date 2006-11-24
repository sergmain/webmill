delete from test.wm_portal_portlet_name 
where ID_SITE_CTX_TYPE not in 
(select b.ID_SITE_CTX_TYPE from test.wm_portal_catalog b  )
/

commit
/
