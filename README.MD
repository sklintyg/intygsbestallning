# Intygsbeställning

## Komma igång med lokal installation
Den här sektionen beskriver hur man bygger applikationen för att kunna köras helt fristående.

Vi använder Gradle för att bygga applikationerna.

Börja med att skapa en lokal klon av källkodsrepositoryt:

    $ git clone https://github.com/sklintyg/intygsbestallning.git

Efter att man har klonat repository navigera till den klonade katalogen intygsbestallning och kör följande kommando:

    $ cd intygsbestallning
    $ ./gradlew clean build

Det här kommandot kommer att bygga samtliga moduler i systemet. 

När applikationen har byggt klart, kan man gå till `/` och köra kommandot

    $ ./gradlew bootRun

för att starta applikationen lokalt.

För att starta applikationen i debugläge används:

    $ ./gradlew bootRun --debug-jvm
    
Applikationen kommer då att starta upp med debugPort = **5005**. Det är denna port du ska använda när du sätter upp din 
debug-konfiguration i din utvecklingsmiljö.

För att starta applicationen och även bygga frontend-klienten med alla statiska filer, kör kommandot

    $ ./gradlew bootRun -P client

för att starta applikationen lokalt.

### H2
Du hittar databasen lokalt på `http://localhost:8080/h2-console

### Frontend
För att köra med live-reload.

    cd web/client
    npm install
    npm start
    
Läs mer på i [README-filen](web/client/README.md) för frontend
