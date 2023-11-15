# Linkr

## To run the frontend:

### `cd Frontend`

### `npm i`

NOTE: you need to run this command only when a new package has been added.

### `npm start`

## Setup Local Backend & Database:

You will need the following:

- Latest backend code
- Updated local MySQL Database
- Wildfly Server 24.0.1
- Wildfly Datasource

### Setup the local MySQL Database

Get the latest linkr Database script from the Google Drive.

- Under the Backend/src/main/resources/linkr.sql

Using MySQL Workbench or a method of your choice run the script and if successful you should see the linkrdatabase created locally along with the following tables:

- budget
- employee
- employee_work_package
- project
- project_employee
- rate_sheet
- timesheet
- timesheet_row
- user_credential
- work_package
- work_package_actual_cost
- work_package_estimate_cose

### Wildfly

Below are the settings for the datasource needed to connect with the linkrdatabase

- Name: linkrDatabase
- JNDI Name: java:jboss/datasources/linkrDatabase
- Driver Class: com.mysql.cj.jdbc.Driver (Name may be different depending on how things were named locally)
- Driver Name: mysql-connector-java-8.0.12.jar (Name may be different depending on how things were named locally)
- Connection URL: jdbc:mysql://localhost/linkrDatabase?serverTimezone=America/Vancouver
- User Name: team2
- Password: pass

### Confirm Local Setup - Work in Progress

Perform the following steps to test if you have sucessfully setup the backend and database locally:

- Run your Wildfly server
- Deploy the backend on to the Wildfly server
- ...(WIP)