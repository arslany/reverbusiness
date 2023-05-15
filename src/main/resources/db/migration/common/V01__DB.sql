create table Person (
  id bigserial primary key,
  name varchar(400) not null,
  age integer not null,
  dateOfBirth date,
  created timestamp,
  modified timestamp,
  alive boolean,
  maritalStatus varchar(200)
)
CREATE TABLE
  USER (
    id serial NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    firstname character varying(255) NOT NULL,
    surname character varying(255) NOT NULL,
    active boolean NOT NULL
  );

ALTER TABLE
  USER
ADD
  CONSTRAINT user_pkey PRIMARY KEY (id)