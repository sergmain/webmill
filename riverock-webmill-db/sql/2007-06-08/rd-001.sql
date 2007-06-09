alter table WM_PORTLET_WEBCLIP
add column IS_INDEXED decimal(1,0) default 0
/

update  test.wm_portlet_webclip
set is_indexed=0
/
