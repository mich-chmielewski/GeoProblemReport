--liquibase formatted sql
--changeset mich:0

-- Table: report_type

-- DROP TABLE report_type;

CREATE TABLE report_type
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
    active boolean DEFAULT false,
    created timestamp without time zone,
    updated timestamp without time zone,
    description character varying(255),
    email character varying(255)
);

-- Table: users

-- DROP TABLE users;

CREATE TABLE users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
    created timestamp without time zone,
    updated timestamp without time zone,
    email character varying(255),
    enabled boolean DEFAULT false,
    password character varying(255),
    user_role character varying(255),
    username character varying(255) NOT NULL
);

-- Table: settings

-- DROP TABLE settings;

CREATE TABLE settings
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
    created timestamp without time zone,
    updated timestamp without time zone,
    display_name text,
    key character varying(255) NOT NULL,
    value text
);

-- Table: report_status

-- DROP TABLE report_status;

CREATE TABLE report_status
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
    created timestamp without time zone,
    updated timestamp without time zone,
    color character varying(255),
    description character varying(255),
    stage character varying(255),
    status_code integer NOT NULL
);

-- Table: reports

-- DROP TABLE reports;

CREATE TABLE reports
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
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
    foreign key (report_type_id) references report_type(id),
    foreign key (report_status_id) references report_status(id)
);

-- Table: comments

-- DROP TABLE comments;

CREATE TABLE comments
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
    created timestamp without time zone,
    updated timestamp without time zone,
    content text,
    report_id bigint NOT NULL,
    send_by_email boolean DEFAULT false,
    foreign key (report_id) references reports(id)
);