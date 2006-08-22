CREATE TABLE WM_FORUM_CONCRETE
  (
  F_ORDER NUMBER (5, 0),
  F_ID NUMBER (7, 0) NOT NULL,
  F_NAME VARCHAR2 (100) NOT NULL,
  F_INFO VARCHAR2 (100) NOT NULL,
  F_U_ID NUMBER (7, 0),
  F_TOPICS NUMBER (7, 0) DEFAULT 0 NOT NULL,
  F_MESSAGES NUMBER (7, 0) DEFAULT 0 NOT NULL,
  F_U_ID2 NUMBER (7, 0),
  F_LASTTIME DATE,
  FORUM_CATEGORY_ID NUMBER (7, 0) NOT NULL,
  IS_DELETED NUMBER (1, 0) DEFAULT 0 NOT NULL
 )
     TABLESPACE MILLENNIUM
 PARALLEL
/