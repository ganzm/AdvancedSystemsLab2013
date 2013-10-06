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