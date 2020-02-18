package unit

import automotivedriverhistoryprocessor.Trip
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime

class TripSpec extends Specification {
    void "MINIMUM_SPEED_TOLERANCE is not null"() {
        expect:
        Trip.MINIMUM_SPEED_TOLERANCE
    }

    void "MAXIMUM_SPEED_TOLERANCE is not null"() {
        expect:
        Trip.MAXIMUM_SPEED_TOLERANCE
    }

    void "constructor initializes correctly"() {
        given:
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = LocalTime.of(1, 0)
        Integer distance = 5

        when: 'calling constructor'
        Trip trip = new Trip(startTime, endTime, distance)

        then: 'object is initialized correctly'
        trip.startTime == startTime
        trip.endTime == endTime
        trip.distance == distance
    }

    @Unroll
    void "trip.getAverageSpeed returns #averageSpeed mph when distance is #distance miles and duration is #duration minutes"() {
        given:
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = startTime.plusMinutes(duration)

        expect:
        new Trip(startTime, endTime, distance).getAverageSpeed() == averageSpeed

        where:
        duration | distance | averageSpeed
        90       | 15       | 10
        60       | 0        | 0
        0        | 10       | 0
    }

    @Unroll
    void "trip.isSpeedWithinAcceptedTolerances returns #withinTolerances for average speed #averageSpeed mph"() {
        given:
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = startTime.plusMinutes(duration)
        Trip trip = new Trip(startTime, endTime, distance as Integer)

        expect:
        trip.isSpeedAboveMinTolerance() == aboveMinTolerance
        trip.isSpeedBelowMaxTolerance() == belowMaxTolerance
        trip.isSpeedWithinAcceptedTolerances() == withinTolerances

        where:
        duration | distance | averageSpeed | aboveMinTolerance | belowMaxTolerance | withinTolerances
        90       | 15       | 10           | true              | true              | true
        60       | 5        | 5            | true              | true              | true
        60       | 4        | 4            | false             | true              | false
        60       | 100      | 100          | true              | true              | true
        60       | 101      | 101          | true              | false             | false
    }
}