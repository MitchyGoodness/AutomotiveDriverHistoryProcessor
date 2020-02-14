package main.groovy

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

    Double getAverageSpeed() {
        LocalTime duration = startTime.from(endTime)
        Double hours = duration.getHour() + (duration.getMinute() / 60)

        distance / hours
    }

    boolean isSpeedWithinAcceptedTolerances() {
        isSpeedAboveMinTolerance() && isSpeedBelowMaxTolerance()
    }

    boolean isSpeedAboveMinTolerance() {
        averageSpeed > MINIMUM_SPEED_TOLERANCE
    }

    boolean isSpeedBelowMaxTolerance() {
        averageSpeed < MAXIMUM_SPEED_TOLERANCE
    }

    @Override
    String toString() {
        "${startTime.from(endTime)} $distance"
    }
}