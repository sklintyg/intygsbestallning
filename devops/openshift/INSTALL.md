### 3.2 Migrate Database Schema

Prior to any release that includes changes to the database schema, the operations provider must execute schema updates using the Liquibase runner tool provided in this section. 

_Please note: a complete database backup is recommended prior to run the database migration tool_

Replace `<version>` below with the actual application version.

Fetch the actual version of the tool, the example below runs `wget` to retrieve the package (zip).

    > wget https://build-inera.nordicmedtest.se/nexus/repository/releases/se/inera/intyg/intygsbestallning/intygsbestallning-liquibase-runner/<version>/intygsbestallning-liquibase-runner-<version>.zip


Download the tool to a computer with Java installed and access to the database in question.

    > unzip intygsbestallning-liquibase-runner-<version>.zip
    > cd intygsbestallning-liquibase-runner-<version>
    > bin/intygsbestallning-liquibase-runner --url=jdbc:mysql://<database-host>/<database-name> --username=<database_username> --password=<database_password> update
