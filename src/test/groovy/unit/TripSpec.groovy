package unit

import automotivedriverhistoryprocessor.Trip
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime

class TripSpec extends Specification {

    void "trip.getAverageSpeed returns accurate speed"() {
        given:
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = LocalTime.of(1, 30)
        Double distanceInMiles = 15

        when: "Trip of 15 miles driven in 1 hour 30 minutes"
        Trip trip = new Trip(startTime, endTime, distanceInMiles)

        then: "Average speed is 10 mph"
        trip.getAverageSpeed() == 10
    }

    void "trip.getAverageSpeed returns 0 mph when no distance was traveled"() {
        given:
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = LocalTime.of(1, 0)
        Double distanceInMiles = 0

        when: "Trip of 0 miles driven in 1 hour"
        Trip trip = new Trip(startTime, endTime, distanceInMiles)

        then: "Average speed is 0 mph"
        trip.getAverageSpeed() == 0
    }

    void "trip.getAverageSpeed returns 0 mph when no time has passed"() {
        given:
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = LocalTime.of(0, 0)
        Double distanceInMiles = 10

        when: "Trip of 10 miles driven in 0 hours"
        Trip trip = new Trip(startTime, endTime, distanceInMiles)

        then: "Average speed is 0 mph"
        trip.getAverageSpeed() == 0
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

    void "Speed is at minimum tolerance"() {
        given:
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = LocalTime.of(1, 0)

        when: "Trip of 5 miles is driven in 1 hour"
        Trip trip = new Trip(startTime, endTime, 5)

        then: "Speed is above minimum tolerance"
        trip.isSpeedAboveMinTolerance()
    }
}