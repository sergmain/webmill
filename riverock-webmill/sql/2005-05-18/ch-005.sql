ALTER TABLE WM_FORUM_CATEGORY ADD CONSTRAINT FORUM_CATEGORY_ID_WFC_PK
  PRIMARY KEY (
  FORUM_CATEGORY_ID
)
 USING INDEX 
 TABLESPACE MILLENNIUM_IDX
/