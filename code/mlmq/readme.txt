==========================================
                ReadMe
==========================================


Some usefull start parameters for the Main class
================================================


Drops a database
dbscript -url jdbc:postgresql://localhost:5432 -db mlmq -user postgres -password postgres -dropDatabase

Creates an empty database
dbscript -url jdbc:postgresql://localhost:5432 -db mlmq -user postgres -password postgres -createDatabase -createTables

Start a Broker
broker -config resource/unittestconfig.example.properties

Start a client
client -config resource/clientconfig.example.properties


