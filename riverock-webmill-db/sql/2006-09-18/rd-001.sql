CREATE TABLE wm_portlet_search (
  ID_SITE NUMBER(10,0) NOT NULL,
  SEARCH_DATE DATE NOT NULL default SYSDATE,
  WORD varchar2(50) default NULL
) 
/