delete from wm_list_r_holding_gr_company
/

delete from wm_list_r_gr_comp_comp
/

update wm_auth_user
set 
id_road=null,
is_road=0,
id_service=null,
is_service=0
/

delete from wm_list_holding
/

delete from wm_list_group_company
/

drop table  wm_list_group_company
/

alter table wm_auth_user
drop column id_road
/

alter table wm_auth_user
drop column is_road
/

alter table wm_auth_user
drop column is_service
/

alter table wm_auth_user
drop column id_service
/

alter table wm_auth_user
add column id_holding decimal(10,0) 
/

alter table wm_auth_user
add column is_holding decimal(1,0) not NULL default 0 
/


drop table wm_list_r_gr_comp_comp
/

drop table wm_list_r_holding_gr_company
/

drop table wm_list_holding
/

CREATE TABLE WM_LIST_HOLDING (
ID_HOLDING decimal(10,0) NOT NULL default 0,
FULL_NAME_HOLDING varchar(200) default NULL,
NAME_HOLDING varchar(30) default NULL,
PRIMARY KEY  (ID_HOLDING)
)
/

CREATE TABLE WM_LIST_R_HOLDING_COMPANY (
  ID_REL_HOLDING decimal(10,0) NOT NULL default '0',
  ID_HOLDING decimal(10,0) NOT NULL default '0',
  ID_COMPANY decimal(10,0) NOT NULL default '0',
  PRIMARY KEY  (ID_REL_HOLDING)
)
/









