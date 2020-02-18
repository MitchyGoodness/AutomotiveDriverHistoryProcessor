package automotivedriverhistoryprocessor

import exception.InvalidAutomotiveDriverLogInput
import groovy.transform.ToString

import java.time.LocalTime

@ToString
class AutomotiveDriverHistory {
    List<AutomotiveDriver> drivers

    AutomotiveDriverHistory() {
        drivers = []
    }

    void addDriver(String name) {
        if(getDriver(name))
            throw new InvalidAutomotiveDriverLogInput("$name is already listed as a driver.")

        drivers.add(new AutomotiveDriver(name))
    }

    AutomotiveDriver getDriver(String name) {
        drivers.find { it.name == name }
    }

    void addTrip(String name, LocalTime startDate, LocalTime endDate, Double distance) {
        AutomotiveDriver driver = getDriver(name)

        if (!driver)
            throw new InvalidAutomotiveDriverLogInput("$name not found in list of automotive drivers.")

        driver.addTrip(new Trip(startDate, endDate, distance))
    }

    @Override
    String toString() {
        drivers.sort().join("\n")
    }
}