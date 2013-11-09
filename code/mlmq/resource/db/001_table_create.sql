CREATE SEQUENCE client_id START 1;
CREATE SEQUENCE queue_id START 1;
CREATE SEQUENCE message_id START 1;
CREATE SEQUENCE message_context START 1;


CREATE TABLE client (
  id INTEGER PRIMARY KEY DEFAULT nextval('client_id'),
  name VARCHAR(50) UNIQUE
);


CREATE TABLE queue (
  id INTEGER PRIMARY KEY DEFAULT nextval('queue_id'),
  name VARCHAR(50) UNIQUE,
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

CREATE INDEX idx_client_name
  ON client
  USING btree
  (name);
  
CREATE INDEX idx_message_queue_id
  ON message
  USING btree
  (queue_id);
  
CREATE INDEX idx_message_prio
  ON message
  USING btree
  (prio);
    
CREATE INDEX idx_message_sent_at
  ON message
  USING btree
  (sent_at);

CREATE INDEX idx_message_client_sender_id
  ON message
  USING btree
  (client_sender_id);

CREATE INDEX idx_message_context
  ON message
  USING btree
  (context);
    
  