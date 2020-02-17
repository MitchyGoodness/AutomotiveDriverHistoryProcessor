package unit

import automotivedriverhistoryprocessor.AutomotiveDriver
import automotivedriverhistoryprocessor.Trip
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime

class AutomotiveDriverSpec extends Specification {
    void setupSpec() {

    }

    @Unroll
    void "addTrip() adds trip to list of trips if its speed is within acceptable tolerances"() {
        given:
        AutomotiveDriver driver = new AutomotiveDriver("Carl")
        Trip trip = Mock(Trip)

        trip.isSpeedWithinAcceptedTolerances() >> withinAcceptedTolerances

        when:
        driver.addTrip(trip)

        then:
        driver.trips.size() == numberOfTrips

        where:
        withinAcceptedTolerances | numberOfTrips
        true                     | 1
        false                    | 0
    }

    void "getTotalDistance returns sum of the distance for all associated trips"() {
        given:
        AutomotiveDriver driver = new AutomotiveDriver("Carl")
        LocalTime time = LocalTime.of(0, 0)
        driver.trips = [new Trip(time, time, 1), new Trip(time, time, 2)]

        expect:
        driver.getTotalDistance() == 3
    }

    void "getAverageSpeed returns average speed of all associated trips"() {
        given:
        AutomotiveDriver driver = new AutomotiveDriver("Carl")
        driver.trips = [Mock(Trip), Mock(Trip), Mock(Trip)]
        driver.trips.eachWithIndex { trip, index -> trip.getAverageSpeed() >> index * 2 }

        expect:
        driver.getAverageSpeed() == 2
    }

    @Unroll
    void "Comparing #driverName who drove #driverDistance miles to #otherDriverName who drove #otherDriverDistance miles returns #expectedResult"() {
        given: 'Two drivers'
        LocalTime time = LocalTime.of(0, 0)

        AutomotiveDriver driver = new AutomotiveDriver(driverName)
        driver.trips.push(new Trip(time, time, driverDistance))

        AutomotiveDriver otherDriver = new AutomotiveDriver(otherDriverName)
        otherDriver.trips.push(new Trip(time, time, otherDriverDistance))

        expect:
        driver.compareTo(otherDriver) == expectedResult

        where:
        driverDistance | driverName | otherDriverDistance | otherDriverName | expectedResult
        100            | "Carl"     | 100                 | "Carl"          | 0
        100            | "Allen"    | 100                 | "Carl"          | -1
        100            | "Carl"     | 100                 | "Allen"         | 1
        50             | "Allen"    | 100                 | "Allen"         | 1

    }
}