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
Copy resource/brokerconfig.example.properties to resource/brokerconfig.properties and adjust to your needs. Then:
broker -config resource/brokerconfig.properties

Start a client
Copy resource/clientconfig.example.properties to resource/clientconfig.properties and adjust to your needs. Then:
client -config resource/clientconfig.properties


