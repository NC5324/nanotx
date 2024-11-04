CREATE TABLE IF NOT EXISTS t_user (
	id numeric(38) NOT NULL,
    login character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    phone_number character varying(255),
    created timestamp without time zone,
    edited timestamp without time zone,
    creator numeric(38),
    editor numeric(38),
    version numeric(38) NOT NULL,
    deleted boolean DEFAULT false,
	CONSTRAINT pk_t_user PRIMARY KEY (id),
  	CONSTRAINT fk_t_user_creator FOREIGN KEY (creator) REFERENCES t_user (id),
  	CONSTRAINT fk_t_user_editor FOREIGN KEY (editor) REFERENCES t_user (id)
);

CREATE SEQUENCE IF NOT EXISTS seq_user_id START WITH 5000 INCREMENT BY 1;