# BookingAPI
Booking REST API for a hotel.
This REST API was made using Spring Boot Framework and Project Lombok, Spring Boot JPA and H2 Database for data storage.


## Swagger

[http://localhost:8080/swagger-ui.html](swagger)

### End-Points

* #### Booking Operations

End-point                          | Http Method | Description
-----------------------------------|-------------|-------------
/reservation/                         | POST        | Save a reservation
/reservation/                         | GET         | Get all reservations
/reservation/{id}                     | GET         | Get a reservation by Id
/reservation/{id}                     | POST        | Update a specific reservation
/reservation/{id}                     | DELETE      | Delete a specific reservation

* #### Health Operations

End-point                          | Http Method | Description
  -----------------------------------|-------------|-------------
  /status/health                    | GET         | Get Application Health
