ALTER TABLE WM_FORUM ADD CONSTRAINT FORUM_ID_WF_PK
  PRIMARY KEY (
  FORUM_ID
)
 USING INDEX 
 TABLESPACE MILLENNIUM_IDX
/