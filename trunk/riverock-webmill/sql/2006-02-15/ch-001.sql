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

alter table WM_I18N_MESSAGE
add column SHORT_NAME_LANGUAGE varchar2(15)
/

update wm_list_language
set short_name_language='ru'
where short_name_language='ru_RU'
/

update wm_list_language
set short_name_language='de'
where short_name_language='de_DE'
/

update wm_list_language
set short_name_language='en'
where short_name_language='en_GB'
/

update wm_list_language
set short_name_language='ja'
where short_name_language='ja_JP'
/

update wm_list_language
set short_name_language='es'
where short_name_language='es_ES'
/

update wm_list_language
set short_name_language='fr'
where short_name_language='fr_FR'
/

update wm_list_language
set short_name_language='it'
where short_name_language='it_IT'
/

update  WM_I18N_MESSAGE a
set SHORT_NAME_LANGUAGE  = 
( select b.SHORT_NAME_LANGUAGE  from wm_list_language b where a.id_language=b.id_language)
/

alter table wm_portal_site_language
drop column id_language
/

drop table wm_list_language
/









