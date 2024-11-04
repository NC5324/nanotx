CREATE TABLE IF NOT EXISTS t_document (
	id numeric(38) NOT NULL,
    mime_type character varying(255),
    file_name character varying(511),
    file_path character varying(255),
    file_size numeric(38),
    created timestamp without time zone,
    edited timestamp without time zone,
    creator numeric(38),
    editor numeric(38),
    version numeric(38) NOT NULL,
    deleted boolean DEFAULT false,
    content bytea,
	CONSTRAINT pk_t_document PRIMARY KEY (id),
  	CONSTRAINT fk_t_document_creator FOREIGN KEY (creator) REFERENCES t_user (id),
  	CONSTRAINT fk_t_document_editor FOREIGN KEY (editor) REFERENCES t_user (id)
);

CREATE SEQUENCE IF NOT EXISTS seq_document_id START WITH 5000 INCREMENT BY 1;