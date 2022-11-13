# ldss-client-desktop

## Setup instructions (Windows)

### Prerequisites

1. Install [Java Development Kit 19](https://docs.oracle.com/en/java/javase/19/install/installation-jdk-microsoft-windows-platforms.html)
1. Install [Apache Maven 3.8.6](https://maven.apache.org/install.html)
1. Install [PostgreSQL](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads) (remember the password you specify for a root `postgres` user)
1. Download [JavaFX 19](https://gluonhq.com/products/javafx/)
1. Create environment variable pointing to `bin` folder of JavaFX library (assuming we are working in PowerShell):
    ```bash
    $env:PATH_TO_FX="D:\_LIBRARIES\javafx-sdk-19\lib"
    ```

    1. If you are working from eclipse here are [instructions](https://stackoverflow.com/questions/64560205/getting-module-javafx-controls-not-found-error-java-eclipse-ide) 
1. Create database in PostgreSQL server: either in [command line](https://www.guru99.com/postgresql-create-database.html) 
   or [pgAdmin](https://www.pgadmin.org/download/). Database name: `ldss`.
1. Install local dependencies:
    ```bash
    mvn install:install-file -Dfile="D:\hse\ldss-client-desktop\libs\fx_table_adapter.jar" -DgroupId="org.grios.tableadapter" -DartifactId="fx_table_adapter" -Dversion="1.0" -Dpackaging=jar
    ```
### Run instructions

1. Specify settings to connect to PostgreSQL server in `src/properties/config.properties`. Change
   only following lines (password as you configured earlier):
    ```bash
    db.name=ldss
    db.password=admin
    ```
1. Build project:
    ```bash
    mvn clean install
    ```
1. Run project:
    ```bash
    java --module-path $env:PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp .\target\AdaptableDSS-1.0-SNAPSHOT-jar-with-dependencies.jar adaptabledsss.HelloApplication
    ```
