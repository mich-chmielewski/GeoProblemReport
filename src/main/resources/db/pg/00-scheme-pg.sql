--liquibase formatted sql
--changeset mich:0

-- Table: report_type

-- DROP TABLE report_type;

CREATE TABLE report_type
(
    id bigserial,
    active boolean DEFAULT false,
    created timestamp without time zone,
    updated timestamp without time zone,
    description character varying(255),
    email character varying(255),
    CONSTRAINT report_type_pk PRIMARY KEY (id)
);

-- Table: users

-- DROP TABLE users;

CREATE TABLE users
(
    id bigserial,
    created timestamp without time zone,
    updated timestamp without time zone,
    email character varying(255),
    enabled boolean DEFAULT false,
    password character varying(255),
    user_role character varying(255),
    username character varying(255) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT username_unique UNIQUE (username)
);

-- Table: settings

-- DROP TABLE settings;

CREATE TABLE settings
(
    id bigserial,
    created timestamp without time zone,
    updated timestamp without time zone,
    display_name text,
    key character varying(255) NOT NULL,
    value text,
    CONSTRAINT settings_pk PRIMARY KEY (id)
);

-- Table: report_status

-- DROP TABLE report_status;

CREATE TABLE report_status
(
    id bigserial,
    created timestamp without time zone,
    updated timestamp without time zone,
    color character varying(255),
    description character varying(255),
    stage character varying(255),
    status_code integer NOT NULL,
    CONSTRAINT report_status_pk PRIMARY KEY (id)
);

-- Table: reports

-- DROP TABLE reports;

CREATE TABLE reports
(
    id bigserial,
    created timestamp without time zone,
    updated timestamp without time zone,
    email character varying(255),
    lat double precision NOT NULL,
    lon double precision NOT NULL,
    message text,
    photo_path character varying(255),
    uuid character varying(255),
    report_status_id bigint NOT NULL,
    report_type_id bigint NOT NULL,
    CONSTRAINT reports_pk PRIMARY KEY (id),
    CONSTRAINT reports_report_type_fk FOREIGN KEY (report_type_id)
        REFERENCES report_type (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT reports_report_status_fk FOREIGN KEY (report_status_id)
        REFERENCES report_status (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- Table: comments

-- DROP TABLE comments;

CREATE TABLE comments
(
    id bigserial,
    created timestamp without time zone,
    updated timestamp without time zone,
    content text,
    report_id bigint NOT NULL,
    send_by_email boolean DEFAULT false,
    CONSTRAINT comments_pk PRIMARY KEY (id),
    CONSTRAINT comments_report_fk FOREIGN KEY (report_id)
        REFERENCES reports (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);