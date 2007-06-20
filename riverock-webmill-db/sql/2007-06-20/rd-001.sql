alter table WM_PORTAL_VIRTUAL_HOST
add column IS_DEFAULT_HOST decimal(1,0) default 0

/

update WM_PORTAL_VIRTUAL_HOST
set IS_DEFAULT_HOST=1

/