# BookingAPI
Booking REST API for a hotel scenario.

## Scenario
People are now free to travel everywhere but because of the pandemic, a lot of hotels went
bankrupt. Some former famous travel places are left with only one hotel.
You’ve been given the responsibility to develop a booking API for the very last hotel in Cancun.

## Requirements
- API will be maintained by the hotel’s IT department.
- As it’s the very last hotel, the quality of service must be 99.99 to 100% => no downtime
- For the purpose of the test, we assume the hotel has only one room available
- To give a chance to everyone to book the room, the stay can’t be longer than 3 days and
  can’t be reserved more than 30 days in advance.
- All reservations start at least the next day of booking,
- To simplify the use case, a “DAY’ in the hotel room starts from 00:00 to 23:59:59.
- Every end-user can check the room availability, place a reservation, cancel it or modify it.
- To simplify the API is insecure.

## Additional Considerations

For the sake of use case, the following considerations were taken into account:
 
- H2 database was chosen for storage
- Logical removal logic was used for DB entities

## Technologies used
- Java 8
- Spring Boot Framework
- Spring Boot Web
- Spring Boot JPA
- Spring Boot Actuator
- Project Lombok
- H2 Database
- JUnit 5
- Mockito
- Gradle
- Model Mapper

## Features Included
- REST API with fully functional operations, validations and requirements, see [Booking API Specification](#Booking API Specification) below
- Response Object design with custom status and body
- Status code design to centralize API Status code management
- Status codes specific per scenario approach
- Global Exception Handling
- Per-request Spring Boot Web filter to log request and responses
- Entity and DTO separation in the controllers and services
- Spring Actuator to enable production-ready status operation
- Basic unit and integration tests using JUnit 5 and Mockito

## High-Availability and No-Downtime

To achieve high-availability aka. no downtime, the suggested following approach can be used:

**2 servers:** 1 main instance and a fail-over in front of a load balancer and perform a kind of canary rollout for deployments.

The idea is to have the main and fail-over schema to protect from downtime and then, at the moment of releasing 
a new version, the new version is installed in the main instance, while the traffic is balanced towards the fail-over.
After confirming the deployment was successful, balance the traffic towards the main instance and update the fail-over instance.

## Extra Documentation
- [ER Database Diagram](https://github.com/pablohorst/booking-api/blob/main/Booking%20API%20ER%20Diagram.pdf)
- [Postman Collection to run examples](https://github.com/pablohorst/booking-api/blob/main/Booking%20API.postman_collection.json)
- [Swagger Spec HTML](https://github.com/pablohorst/booking-api/blob/main/swagger-html/index.html)

## Instructions

## Booking API Specification

### End-Points

#### Reservation Operations

| End-point               | Http Method | Description             |
|-------------------------|-------------|-------------------------|
| /v0/booking/guest/      | POST        | Create a guest          |
| /v0/booking/guests/     | GET         | Get all guests          |
| /v0/booking/guests/{id} | GET         | Get a guest by Id       |
| /v0/booking/guests/{id} | POST        | Update a specific guest |
| /v0/booking/guests/{id} | DELETE      | Delete a specific guest |


#### Reservation Operations

| End-point                     | Http Method | Description                   |
|-------------------------------|-------------|-------------------------------|
| /v0/booking/reservation/      | POST        | Create a reservation          |
| /v0/booking/reservations/     | GET         | Get all reservations          |
| /v0/booking/reservations/{id} | GET         | Get a reservation by Id       |
| /v0/booking/reservations/{id} | POST        | Update a specific reservation |
| /v0/booking/reservations/{id} | DELETE      | Delete a specific reservation |

#### Health Operations

| End-point                 | Http Method | Description            |
|---------------------------|-------------|------------------------|
| /v0/booking/status/health | GET         | Get Application Health |
