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
Example for SimpleShutdownBroker:
Copy resource/brokerconfig.example.properties to resource/config.properties and adjust to your needs. Then:
SimpleShutdownBroker -config resource/config.properties

Example for SimpleSendClient:
Copy resource/clientconfig.example.properties to resource/config.properties and adjust to your needs. Then:
SimpleSendClient -config resource/config.properties


Some useful vm arguments
================================================
Logs Garbage Collection Activities
-Xloggc:gc.log


