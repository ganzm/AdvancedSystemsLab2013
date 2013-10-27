==========================================
                ReadMe
==========================================


Some useful start parameters for the Main class
================================================


Drops a database
dbscript -url jdbc:postgresql://localhost:5432 -db mlmq -user postgres -password postgres -dropDatabase

Creates an empty database
dbscript -url jdbc:postgresql://localhost:5432 -db mlmq -user postgres -password postgres -createDatabase -createTables


Start a scenario (see class names of ch.ethz.mlmq.scenario.scenarios)
--------------------------------------------------------------------
Broker:
scenario -l resource/logging.properties -config config/2hLoadTest/brokerconfig.properties
Client:
scenario -l resource/logging.properties -config config/2hLoadTest/clientconfig.properties

Some useful vm arguments
================================================
Logs Garbage Collection Activities
-Xloggc:gc.log


