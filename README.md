# Cab Management Service
A backend service for Cab management portal that provides inter-city cab services.

This service is able to :
1. Register cabs.
2. Onboard various cities where cab services are provided.
3. Change current city (location) of any cab.
4. Change state of any cab. For this you will have to define a state machine for the cab ex:
a cab must have at least these two basic states; IDLE and ON_TRIP
5. Book cabs based on their availability at a certain location. In case more than one cab are
available , use the following strategy;
  - Find out which cab has remained idle the most and assign it.
  - In case of clash above, randomly assign any cab


Assumption : a cab once assigned a trip cannot cancel/reject it