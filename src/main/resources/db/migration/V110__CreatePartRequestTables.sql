CREATE TABLE IF NOT EXISTS t_part_request (
	id numeric(38) NOT NULL,
    component_name character varying(255),
    category character varying(255),
    manufacturer character varying(255),
    part_number character varying(255),
    alternate_part_number character varying(255),
    quantity numeric(38),
    target_price numeric(38, 2),
    currency character varying(255),
    min_temp numeric(38),
    max_temp numeric(38),
    package_type character varying(255),
    state character varying(255),
    documents_metadata jsonb,
    dynamic_properties jsonb,
    created timestamp without time zone,
    edited timestamp without time zone,
    creator numeric(38),
    editor numeric(38),
    version numeric(38) NOT NULL,
    deleted boolean DEFAULT false,
	CONSTRAINT pk_t_part_request PRIMARY KEY (id),
  	CONSTRAINT fk_t_part_request_creator FOREIGN KEY (creator) REFERENCES t_user (id),
  	CONSTRAINT fk_t_part_request_editor FOREIGN KEY (editor) REFERENCES t_user (id)
);

CREATE SEQUENCE IF NOT EXISTS seq_part_request_id START WITH 5000 INCREMENT BY 1;