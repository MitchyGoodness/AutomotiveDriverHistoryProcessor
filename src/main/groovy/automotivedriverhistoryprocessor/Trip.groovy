package automotivedriverhistoryprocessor

import java.time.Duration
import java.time.LocalTime

class Trip {
    final static Integer MINIMUM_SPEED_TOLERANCE = 5
    final static Integer MAXIMUM_SPEED_TOLERANCE = 100

    LocalTime startTime
    LocalTime endTime
    Double distance

    Trip(LocalTime startTime, LocalTime endTime, Double distance) {
        this.startTime = startTime
        this.endTime = endTime
        this.distance = distance
    }

    private def getDuration(String formatAs = 'unconverted') {
        Duration duration = Duration.between(startTime, endTime)

        switch (formatAs) {
            case 'minutes':
                return duration.toMinutes()
                break

            case 'hours as double':
                return duration.toMinutes() / 60
                break

            case 'hours as integer':
                return duration.toHours() as Integer
                break

            default:
                return duration
        }
    }

    Double getAverageSpeed() {
        Double duration = getDuration('hours as double')
        duration ? distance / duration : 0
    }

    boolean isSpeedWithinAcceptedTolerances() {
        isSpeedAboveMinTolerance() && isSpeedBelowMaxTolerance()
    }

    boolean isSpeedAboveMinTolerance() {
        averageSpeed >= MINIMUM_SPEED_TOLERANCE
    }

    boolean isSpeedBelowMaxTolerance() {
        averageSpeed <= MAXIMUM_SPEED_TOLERANCE
    }

    @Override
    String toString() {
        "${startTime.from(endTime)} $distance"
    }
}