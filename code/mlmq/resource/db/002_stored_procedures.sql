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

