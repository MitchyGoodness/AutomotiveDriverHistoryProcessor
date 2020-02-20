# Automotive Driver History Processor

**Version 2.0.0**

Parses driver history logs, then returns a reformatted version sorted by total distance driven.

---

## Requirements and setup

As this is a command-line interface, the user is expected to have basic understanding setting up the environment and 
using such interfaces. Using your preferred package manager, install the following languages and versions:
* Java 1.8

As a suggested package manager, SDKMAN is simple to install and use for unix based systems.

Run the gradlew script from the project directory

For unix-based OS's 
```
$./gradlew
```

For windows users
```
$gradlew.bat
```

This should download gradle and run the default tasks, which are:
* clean
* build
* test - runs the automated test suite found in src/test
* fatJar - this will package the project as a java jar in the build/libs folder

---

## Usage

CLI Usage:
``` 
$java -jar AutomotiveDriverHistoryProcessor-2.0.jar {FILE|-}
```

File or Input Usage:
```
Driver DRIVER_NAME
Trip DRIVER_NAME START_TIME END_TIME DISTANCE
```

---

## Design Process

### Reading through the requirements I gathered a basic list of the criteria:

* Expected to take a minimal amount of time
* Anything not covered was up to the developer's discretion and will not be evaluated
* Must have a readme without identifying information included
* No deadline
* Bundle/Package the final product and submit to the DropBox link provided by either Tim Hill or Chris Evans
* The program can process input in one of two ways, by receiving arguments from the command line or through stdin
* Each line of the input has a valid command which would be "Driver DRIVER_NAME" or 
    "Trip DRIVER_NAME START_TIME END_TIME DISTANCE"
    * The driver command will add a driver by name
    * The trip command will add a trip to a specific driver by name
        * The start time and end time will be in military time format, in sequential order and not exceed midnight into 
            the next day
        * Minimum allowed speed 5 MPH
        * Maximum allowed speed 100 MPH
* Generate a report, it's assumed printing to stdout is acceptable in this case
    * List drivers in descending order of total miles recorded
    * If no distance recorded report ```DRIVER_NAME 0 miles``` otherwise report 
        ```DRIVER_NAME TOTAL_DISTANCE miles @ AVERAGE_SPEED mph```
    * Round total distance and average speed to the nearest integer
* Implementation should show knowledge of automated testing and domain modeling
* Record design process in the readme
* Be consistent and intentional

### Reorganizing the criteria based on topic:
* Optional, Amorphous, or Ambiguous Requirements
    * Expected to take a minimal amount of time
    * Anything not covered was up to the developer's discretion and will not be evaluated
    * No deadline
    * Implementation should show knowledge of automated testing and domain modeling
    * Record design process in the readme
    * Be consistent and intentional
* Submission Requirements
    * Must have a readme without identifying information included
    * Bundle/Package the final product and submit to the DropBox link provided by either Tim Hill or Chris Evans
* Application Requirements
    * The program can process input in one of two ways, by receiving arguments from the command line or through stdin
    * Each line of the input has a valid command which would be "Driver DRIVER_NAME" or 
        "Trip DRIVER_NAME START_TIME END_TIME DISTANCE"
        * The driver command will add a driver by name
        * The trip command will add a trip to a specific driver by name
            * The start time and end time will be in military time format, in sequential order and not exceed midnight 
                into the next day
            * Minimum allowed speed 5 MPH
            * Maximum allowed speed 100 MPH
    * Generate a report, it's assumed printing to stdout is acceptable in this case
        * List drivers in descending order of total miles recorded
        * If no distance recorded report ```DRIVER_NAME 0 miles``` otherwise report 
        ```DRIVER_NAME TOTAL_DISTANCE miles @ AVERAGE_SPEED mph```
        * Round total distance and average speed to the nearest integer

### Development
Keeping the application requirements in mind, I separated everything based on two main concerns; Input/Output, or 
Automotive data. I/O handling can be grouped into three main categories:
* File handling
* Standard input handling
* And a way to generate the report

Using the factory pattern I could get an object implementing an input handler interface and using the interface's 
methods get the data for whichever form of input the user provides. Using a simple command I can write the output to 
stdout using an intelligent toString method.

Automotive Data can be grouped into four main Classes:
* Processor 
* History or Log
* Driver
* Trip

The driver and trip class were more or less spelled out in the requirements but a container for the collection of 
drivers is needed. The container would need an appropriate name, such as a history or a log. The history would track a 
list of drivers calling the appropriate functions on the individual drivers as needed. Each driver class would keep 
track of its respective list of trips. 

As the driver received data it would validate the input. The same with the trips.

Data model cardinality:
1 Processor to 1 History
1 History to many Drivers
1 Driver to many Trips

While developing this, I asked a peer to review my design and he pointed out the validation should probably be separated
from the History, Driver, and Trip classes and moved into the Processor. He also suggested I use a more java project 
package structure. Taking my peer's input seriously, I moved the validation to the Processor class and liked the overall
organization of the java project structure.

Once all of the criteria was met and the application worked from beginning to the end, I started work on the automated 
tests. This was unfortunate, since it would have been better to use TDD (test driven development) or BDD (behavior 
driven development) to check my design and maintain simplicity as I developed. Admittedly, this was just a poor habit I 
will need to correct at a later date.

Realizing I did not want to include testing with the final production version of the project, I decided to wrap the 
project into a gradle project and keep the tests separate from the main application. Once the project was setup for 
Gradle, I experimented with creating a deployable package like a java jar. I was pleasantly surprised to realize, 
the build could be a java jar. This meant the production bundle would be easy to run in almost any environment since 
Java is such a well know language.  

--- 

## Contributors

- REDACTED