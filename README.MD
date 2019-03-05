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

### Frontend
För att köra med live-reload.

    cd web/client
    npm install
    npm start
    
Läs mer på i [README-filen](web/client/README.md) för frontend
