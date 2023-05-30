CREATE TABLE
  PERMISSIONS (
    id bigserial NOT NULL,
    CODE character varying(10) NOT NULL,
    NAME character varying(255) NOT NULL,
    DESCRIPTION character varying(255) NOT NULL,
    active boolean NOT NULL
  );

ALTER TABLE PERMISSIONS
ADD CONSTRAINT PR_CODE UNIQUE (CODE);

CREATE TABLE
  USER_PERMISSIONS (
    id bigserial NOT NULL,
    userid bigserial NOT NULL,
    CODE character varying(10) NOT NULL,
    NAME character varying(255) NOT NULL,
    DESCRIPTION character varying(255) NOT NULL,
    active boolean NOT NULL
  );

ALTER TABLE USER_PERMISSIONS
ADD CONSTRAINT PR_USERID_CODE UNIQUE (USERID,CODE);