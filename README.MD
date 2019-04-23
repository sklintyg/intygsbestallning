# Intygsbeställning

## Komma igång med lokal installation
Den här sektionen beskriver hur man bygger applikationen för att kunna köras helt fristående.

Vi använder Gradle för att bygga applikationerna.

Börja med att skapa en lokal klon av källkodsrepositoryt:

    $ git clone https://github.com/sklintyg/intygsbestallning.git

På grund av att vi är beroende av sklintyg/infra för lite auth-relaterad kod måste man dessutom göra:

    $ cd infra
    $ checkout feature/spring5
    $ ./gradlew build install

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

I nuläget kör applikationen med profilen **dev-security**, denna ger full access till allt.
För att aktivera filter och SAML-auth etc finns just nu testprofilen **ib-security-test**

### H2
Du hittar databasen lokalt på `http://localhost:8080/h2-console

### Frontend
För att köra med live-reload.

    cd web/client
    npm install
    npm start
    
Läs mer på i [README-filen](web/client/README.md) för frontend

### Köra integrationstester
Vi har integrationstester skrivna med [REST-Assured](https://github.com/jayway/rest-assured)

De körs inte automatiskt vid bygge av applikationen utan man behöver köra dem med kommandot

    $ ./gradlew restassured
    
Man kan exekvera enskilda tester genom exempelvis:

    $ ./gradlew restassured --tests *BestallningControllerIT.testGetBestallningar
    
För att debugga själva testet, lägg på --debug-jvm dvs:

    $ ./gradlew restassured --debug-jvm
    ... loggar ...
    Listening for transport dt_socket at address: 5005

Anslut nu remote debugging i IDEA.

För att logga från testet till stdout:

    $ ./gradlew restassured -info