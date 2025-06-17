# HotelAPI

HotelAPI - RESTful API for hotel management, developed using Spring Boot. The system provides functionality for creating, searching and managing information about hotels, including their addresses, contacts, amenities and check-in/check-out times.

## Technology Stack

- Java 21
- Maven
- Spring Boot
- Spring Data JPA
- H2 Database
- Liquibase
- Swagger/OpenAPI for API documentation
- JUnit 5 for testing

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── example/
│               └── hotel/
│                   ├── config/
│                   │   └── SwaggerConfig.java
│                   ├── controller/
│                   │   └── HotelController.java
│                   ├── dto/
│                   │   ├── HotelCreateDTO.java
│                   │   ├── HotelDetailsDTO.java
│                   │   └── HotelResponseDTO.java
│                   ├── exception/
│                   │   └── GlobalExceptionHandler.java
│                   ├── model/
│                   │   ├── Address.java
│                   │   ├── Amenity.java
│                   │   ├── ArrivalTime.java
│                   │   ├── Contact.java
│                   │   └── Hotel.java
│                   ├── repository/
│                   │   ├── AmenityRepository.java
│                   │   └── HotelRepository.java
│                   ├── service/
│                   │   └── HotelService.java
│                   └── HotelApiApplication.java
└── test/
    └── java/
        └── com/
            └── example/
                └── hotel/
                    ├── controller/
                    │   └── HotelControllerTest.java
                    ├── repository/
                    │   └── HotelRepositoryTest.java
                    ├── service/
                    │   └── HotelServiceTest.java
                    ├── util/
                    │   └── TestDataUtil.java
                    ├── validation/
                    │   └── ValidationTest.java
                    └── HotelApiApplicationTests.java
```

## API Endpoints

- `GET /property-view/hotels` - Get all hotels
- `POST /property-view/hotels` - Create new hotel
- `POST /property-view/hotels/{id}/amenities` - Add amenities to hotel
- `GET /property-view/search` - Search hotels with filters
- `GET /property-view/hotels/{id}` - Get hotel by ID
- `GET /property-view/histogram/{param}` - Get statistics by parameter

![Image](https://github.com/user-attachments/assets/4562a1eb-caa3-4bb6-8088-27b49fac49cd)

### Examples

#### Create new hotel request

```json
{
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
  "brand": "Hilton",
  "address": {
    "houseNumber": 9,
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375 17 309-80-00",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  }
}
```

#### Create new hotel response

```json
{
  "id": 1,
  "name": "DoubleTree by Hilton Minsk",
  "descripion": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
  "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
  "phone": "+375 17 309-80-00"
}
```



## Setup

### DataBase

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
