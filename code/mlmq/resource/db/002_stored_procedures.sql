CREATE OR REPLACE FUNCTION createQueue(client_id integer)
RETURNS integer AS $$

DECLARE
    newid BIGINT;
BEGIN

   INSERT INTO queue(client_id) VALUES ($1)  RETURNING id INTO newid;
    return newid;
END
$$
LANGUAGE plpgsql;

----

CREATE OR REPLACE FUNCTION createClient(client_name varchar)
RETURNS integer AS $$

DECLARE
    newid BIGINT;
BEGIN

   INSERT INTO client(name) VALUES ($1)  RETURNING id INTO newid;
    return newid;
END
$$
LANGUAGE plpgsql;


----

CREATE OR REPLACE FUNCTION  peekMessage(from_queue_id integer, from_client_id integer, shouldOrderByPriority boolean)
RETURNS TABLE (id integer, queue_id integer, client_sender_id integer, content BYTEA, prio smallint, sent_at time without time zone )
AS $$

DECLARE
    query_string varchar;
BEGIN

	query_string = 'SELECT m.id, m.queue_id, m.client_sender_id, m.content, m.prio, m.sent_at FROM message m WHERE 1=1 ';

	IF (NOT $1 IS NULL) THEN
		-- queue id
		query_string = query_string || ' AND m.queue_id = '  || $1;
	END IF;

	IF (NOT $2 IS NULL) THEN
		-- client id
		query_string = query_string || ' AND m.client_sender_id = '  || $2;
	END IF;

	if(shouldOrderByPriority) THEN
		query_string = query_string || ' ORDER BY m.prio LIMIT 1';
	ELSE
		query_string = query_string || ' ORDER BY m.sent_at LIMIT 1';
	END IF;

	return QUERY EXECUTE query_string;
END
$$
LANGUAGE plpgsql;