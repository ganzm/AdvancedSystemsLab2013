CREATE SEQUENCE broker_id START 1;
CREATE SEQUENCE client_id START 1;
CREATE SEQUENCE queue_id START 1;
CREATE SEQUENCE message_id START 1;
CREATE SEQUENCE request_response_id START 1;

CREATE TABLE broker (
  id INTEGER PRIMARY KEY DEFAULT nextval('broker_id'),
  host VARCHAR,
  port SMALLINT
);


CREATE TABLE client (
  id INTEGER PRIMARY KEY DEFAULT nextval('client_id'),
  name VARCHAR
);


CREATE TABLE queue (
  id INTEGER PRIMARY KEY DEFAULT nextval('queue_id'),
  client_id INTEGER REFERENCES client (id),
  broker_id INTEGER NULL REFERENCES broker (id)
);


CREATE TABLE message (
  id INTEGER PRIMARY KEY DEFAULT nextval('message_id'),
  queue_id INTEGER NOT NULL REFERENCES queue (id),
  client_sender_id INTEGER NOT NULL REFERENCES client (id),
  content VARCHAR,
  prio SMALLINT,
  sent_at time without time zone NOT NULL DEFAULT now()
);


CREATE TABLE request_response (
  id INTEGER PRIMARY KEY  DEFAULT nextval('request_response_id'),
  message_request_id INTEGER NOT NULL REFERENCES client (id),
  message_response_id INTEGER REFERENCES client (id)
);

