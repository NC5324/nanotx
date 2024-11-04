CREATE TABLE IF NOT EXISTS t_master_data (
	id numeric(38) NOT NULL,
    component_name character varying(255),
    type character varying(255),
    category character varying(255),
    manufacturer character varying(255),
    part_number character varying(255),
    alternate_part_number character varying(255),
    min_temp numeric(38),
    max_temp numeric(38),
	CONSTRAINT pk_t_master_data PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS seq_master_data_id START WITH 5000 INCREMENT BY 1;