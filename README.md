# Analyzer (DE)

Ein Schulprojekt mit dem Zweck, Strommessungsdaten (SDAT und ESL) auszuwerten.

## Ausführen
Java JDK 11 muss installiert sein, um dieses Projekt kompilieren zu können.
```shell script
java -version
```
sollte also JDK 11 zurückgeben. (Unter Windows muss oft noch die PATH-Systemvariable angepasst werden)

Zudem wird Maven gebraucht. Das Programm kann dann mit 
```shell script
mvn javafx:run
```
ausgeführt werden. Alternativ kann IntelliJ Idea verwendet werden. Dort muss eine neue Maven-Konfiguration hinzugefügt
werden und unter "Parameters" die "Command Line" auf `javafx:run` gesetzt werden.

Zudem müssen Datenfiles im SDAT- und ESL-Format vorhanden sein, um das Programm verwenden zu können.