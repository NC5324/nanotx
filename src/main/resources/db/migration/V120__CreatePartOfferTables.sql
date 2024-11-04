CREATE TABLE IF NOT EXISTS t_part_offer (
	id numeric(38) NOT NULL,
	root_id numeric(38) NOT NULL,
	request_id numeric(38) NOT NULL,
    previous_version_id numeric(38),
    next_version_id numeric(38),
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
	CONSTRAINT pk_t_part_offer PRIMARY KEY (id),
	CONSTRAINT fk_t_part_offer_request FOREIGN KEY (request_id) REFERENCES t_part_request (id),
  	CONSTRAINT fk_t_part_offer_creator FOREIGN KEY (creator) REFERENCES t_user (id),
  	CONSTRAINT fk_t_part_offer_editor FOREIGN KEY (editor) REFERENCES t_user (id)
);

CREATE SEQUENCE IF NOT EXISTS seq_part_offer_id START WITH 5000 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS t_part_offer_comment (
	id numeric(38) NOT NULL,
    parent_id numeric(38),
    content character varying(255),
    offer_id numeric(38) NOT NULL,
    documents_metadata jsonb,
    created timestamp without time zone,
    edited timestamp without time zone,
    creator numeric(38),
    editor numeric(38),
    version numeric(38) NOT NULL,
    deleted boolean DEFAULT false,
	CONSTRAINT pk_t_part_offer_comment PRIMARY KEY (id),
  	CONSTRAINT fk_t_part_offer_comment_creator FOREIGN KEY (creator) REFERENCES t_user (id),
  	CONSTRAINT fk_t_part_offer_comment_editor FOREIGN KEY (editor) REFERENCES t_user (id)
);

CREATE SEQUENCE IF NOT EXISTS seq_part_offer_comment_id START WITH 5000 INCREMENT BY 1;

