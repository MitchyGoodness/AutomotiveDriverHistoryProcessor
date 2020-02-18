package unit

import automotivedriverhistoryprocessor.AutomotiveDriver
import automotivedriverhistoryprocessor.AutomotiveDriverHistory
import automotivedriverhistoryprocessor.Trip
import exception.InvalidAutomotiveDriverLogInput
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime

class AutomotiveDriverHistorySpec extends Specification {
    void "constructor initializes correctly"() {
        when: 'calling constructor'
        AutomotiveDriverHistory history = new AutomotiveDriverHistory()

        then: 'object is initialized correctly'
        history.drivers instanceof List<AutomotiveDriver>
        history.drivers.size() == 0
    }

    void "addDriver adds unique driver to the list"() {
        given: 'history and a given driver name'
        AutomotiveDriverHistory history = new AutomotiveDriverHistory()
        String driverName = 'Donald'

        when: 'adding a driver'
        history.addDriver(driverName)

        then: 'list of drivers increases by one and the new driver has the name given'
        history.drivers.size()
        history.drivers.first().name == driverName
    }

    void "addDriver throws exception for non-unique driver"() {
        given: 'history and a given driver name'
        AutomotiveDriverHistory history = new AutomotiveDriverHistory()
        String driverName = 'Kyle'
        history.drivers.push(new AutomotiveDriver(driverName))

        when: 'adding a driver with the same name'
        history.addDriver(driverName)

        then: 'exception is thrown'
        Exception e = thrown(InvalidAutomotiveDriverLogInput)
        e.message == "$driverName is already listed as a driver."
    }

    @Unroll
    void "getDriver returns a driver with the same name if found"() {
        given: 'history and with a driver'
        AutomotiveDriverHistory history = new AutomotiveDriverHistory()
        history.addDriver(startedWith)

        expect:
        history.getDriver(askedFor)?.name == whoWeFound

        where:
        startedWith | askedFor   | whoWeFound
        'Ashley'    | 'Brittney' | null
        'Joe'       | 'Joe'      | "Joe"
    }

    void "addTrip adds a trip for known driver"() {
        given:
        String driverName = 'Sarah'
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = startTime.plusMinutes(60)

        AutomotiveDriverHistory history = new AutomotiveDriverHistory()
        history.addDriver(driverName)

        when: 'adding a trip'
        history.addTrip(driverName, startTime, endTime, 50)

        then:
        history.getDriver(driverName).trips.size()
    }

    void "addTrip throws exception for unknown driver"() {
        given:
        String driverName = 'Sarah'
        String otherDriverName = 'Zoe'

        AutomotiveDriverHistory history = new AutomotiveDriverHistory()
        history.addDriver(driverName)

        when: 'adding a trip'
        history.addTrip(otherDriverName, null, null, null)

        then:
        Exception e = thrown InvalidAutomotiveDriverLogInput
        e.message == "$otherDriverName not found in list of automotive drivers."
    }

    void "toString prints strings returned from list of drivers"() {
        given:
        AutomotiveDriver driver = new AutomotiveDriver("Carl")
        driver.trips = [Mock(Trip), Mock(Trip), Mock(Trip)]
        driver.trips.eachWithIndex { trip, index -> trip.getAverageSpeed() >> index * 2 }

        expect:
        driver.getAverageSpeed() == 2
    }
}