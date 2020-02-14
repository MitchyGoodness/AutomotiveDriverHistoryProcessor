package automotivedriverhistoryprocessor

import exception.InvalidAutomotiveDriverLogInput

import java.time.LocalTime

class AutomotiveDriverHistory {
    List<AutomotiveDriver> drivers

    AutomotiveDriverHistory() {
        drivers = []
    }

    void addDriver(String name) {
        drivers.add(new AutomotiveDriver(name))
    }

    void addTrip(String name, LocalTime startDate, LocalTime endDate, Double distance) {
        AutomotiveDriver driver = drivers.find { it.name == name }

        if (driver)
            driver.addTrip(new Trip(startDate, endDate, distance))
        else
            throw new InvalidAutomotiveDriverLogInput("$name not found in list of automotive drivers.")
    }
}