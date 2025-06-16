# HotelAPI

## Setup

### DataBase

#### Create

Run command in <b>Terminal</b>:

```bash
java -cp h2-*.jar org.h2.tools.Shell
```

Fill like:

```bash
Welcome to H2 Shell 2.3.232 (2024-08-11)
Exit with Ctrl+C
[Enter]   jdbc:h2:~/hotel/src/main/resources/db/hoteldb
URL       
[Enter]   org.h2.Driver
Driver    
[Enter]   sa
User      
Password  Password  ><
Type the same password again to confirm database creation.
Password  Password  ><
Connected
```

where `h2-*.jar` need to download from this [site](https://www.h2database.com/html/download.html)

#### Run migration

You can run project and will automatic migration with command in <b>Terminal</b>:

```bash
mvn spring-boot:run
```

Or run command for update manually:

```bash
mvn liquibase:update
```

#### Add new migration

Need to add a new changeset in `db.changelog-master.yaml` and write a new `changeSet.id` by format like: <b>YYYYMMDDHHMMSS</b> or <b>20250613195849</b>


## Swagger

To view detailed information, when the project is running, follow the link: `http://localhost:8092/swagger-ui/index.html`


## Testing

The project has tests, they can be run with this command:

```bash
mvn test
```
