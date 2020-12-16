# microProCiv
Emergency management platform that allows citizens to get latest news and weather info supported with sensor data, report an emergency event and receive official emergency alerts.

## Services
###### Authentication service
- Register
- Login

###### Information service
- Get weather info (with sensor data)
- Add weather info (auth - admin)
- Get latest news
- Add latest news (auth - admin)

###### Alerting service
- Send report (auth - user)
- List reports
- Get alerts
- Add alert (auth - admin)

###### Sensor service
- List sensors
- Get sensor data
- Register new sensor (auth - admin)

###### Health service
- Aggregates health info of the whole system
- Checks health of other services periodically