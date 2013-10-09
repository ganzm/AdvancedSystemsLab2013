CREATE SEQUENCE client_id START 1;
CREATE SEQUENCE queue_id START 1;
CREATE SEQUENCE message_id START 1;
CREATE SEQUENCE message_context START 1;


CREATE TABLE client (
  id INTEGER PRIMARY KEY DEFAULT nextval('client_id'),
  name VARCHAR UNIQUE
);


CREATE TABLE queue (
  id INTEGER PRIMARY KEY DEFAULT nextval('queue_id'),
  client_id INTEGER REFERENCES client (id)
);


CREATE TABLE message (
  id INTEGER PRIMARY KEY DEFAULT nextval('message_id'),
  queue_id INTEGER NOT NULL REFERENCES queue (id) ON DELETE CASCADE,
  client_sender_id INTEGER NOT NULL REFERENCES client (id),
  content BYTEA,
  prio SMALLINT,
  sent_at time without time zone NOT NULL DEFAULT NOW(),
  context INTEGER NULL 
);




CREATE INDEX idx_queue_client_id
  ON queue
  USING btree
  (client_id);
