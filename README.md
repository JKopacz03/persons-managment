# Persons Managment
This project manages information about individuals categorized as employees, students, retirees, and potentially other types in the future. Each individual is characterized by a set of attributes including first name, last name, unique PESEL (Polish national identification number), height, weight, and email address.

For students, additional data such as university name, year of study, study program, and scholarship amount are stored. Employees have information on start date, current position, and salary, while retirees' data includes pension amount and years worked.

This project is great example of:
* Inserting big csv file do db with Spring Batch (ex. 100MB file in 2 second)
* Open/Close principle
* Usage Startegy and Factory design pattern.
* Optimisitc locking (and some pessimistic)
* Avoiding n+1
* Using QueryDSL

## Endpoint for Retrieving Individuals
One endpoint facilitates retrieving individuals from the database based on various search criteria:

* Type (employee, student, retiree)
* First name, last name
* Age
* PESEL
* Gender
* Height (range)
* Weight (range)
* Email address
Numeric and date criteria utilize inclusive ranges, while text searches employ a "contains ignore case" method. Pagination support is integrated for efficient data handling.

## Endpoint for Adding Individuals
One endpoint allows add different types of users.

## Endpoint for Editing Individuals
One endpoint allows edit different types of users.

## Endpoint for Managing Employee Positions
An endpoint manages employee positions, specifying periods during which an employee holds a position with a specified name and salary. Overlapping date periods for positions are prohibited to maintain data accuracy.

## Endpoint for CSV File Upload
An endpoint facilitates the bulk import of individuals from CSV files into the database. Key features include:

Support for large files (up to 3GB)
Non-blocking import execution with progress monitoring (status, creation date, start date, processed row count)
CSV file format: TYPE,first name,last name,PESEL,height,weight,email address,additional parameters specific to each individual
Single import execution at any time with potential for concurrent executions in future environments.

## Critical requirements for updating individuals:

* Resilience against race conditions
* Handling of missing updates
* Non-blocking operation
* Version incrementation by 1 if the current object version is provided; otherwise, an OptimisticLockingFailureException is thrown.
* Execution of exactly 2 queries (one for finding and one for updating).
* Import Considerations
## Key aspects of the import functionality:

* Transactional import (all or nothing)
* Minimum insertion rate of 50,000 records per second on H2 (file) database
* Memory-efficient operation to handle up to 20 million records with a maximum of 200MB application memory footprint
