import automotivedriverhistoryprocessor.Trip
import spock.lang.Specification

import java.time.LocalTime

class TripSpec extends Specification {

    void "Average speed is 10 mph"() {
        given: "Trip of 20 miles driven in 2 hours"
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = LocalTime.of(1, 0)
        Double distanceInMiles = 20

        when: "Recording the trip"
        Trip trip = new Trip(startTime, endTime, distanceInMiles)

        then: "Average speed is 10 mph"
        trip.getAverageSpeed() == 10
    }

    void "Speed is at minimum tolerance"() {
        given: "Trip of 5 miles is driven in 1 hour"
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = LocalTime.of(1, 0)

        when: "Recording the trip"
        Trip trip = new Trip(startTime, endTime, 5)

        then: "Speed is above minimum tolerance"
        trip.isSpeedAboveMinTolerance()
    }
}