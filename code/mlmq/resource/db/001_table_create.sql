

CREATE TABLE broker (
  id INTEGER PRIMARY KEY,
  host VARCHAR,
  port SMALLINT
);


CREATE TABLE client (
  id INTEGER PRIMARY KEY,
  sessiont_token VARCHAR,
  connected BOOLEAN
);


CREATE TABLE queue (
  id INTEGER PRIMARY KEY,
  client_id INTEGER REFERENCES client (id),
  broker_id INTEGER NOT NULL REFERENCES broker (id)
);


CREATE TABLE message (
  id INTEGER PRIMARY KEY,
  queue_id INTEGER NOT NULL REFERENCES queue (id),
  client_sender_id INTEGER NOT NULL REFERENCES client (id),
  content VARCHAR,
  prio SMALLINT
);


CREATE TABLE request_response (
  id INTEGER PRIMARY KEY,
  message_request_id INTEGER NOT NULL REFERENCES client (id),
  message_response_id INTEGER REFERENCES client (id)
);

