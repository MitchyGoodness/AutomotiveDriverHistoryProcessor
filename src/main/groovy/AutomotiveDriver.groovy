package main.groovy

import groovy.transform.ToString

@ToString
class AutomotiveDriver implements Comparable<AutomotiveDriver> {
    String name
    List<Trip> trips

    AutomotiveDriver(String name) {
        this.name = name
        this.trips = []
    }

    void addTrip(Trip trip) {
        if (trip.isSpeedWithinAcceptedTolerances())
            trips.push(trip)
    }

    Integer getTotalDistance() {
        trips.distance.sum() as Integer ?: 0
    }

    Integer getAverageSpeed() {
        List<Double> averages = trips.collect { it.getAverageSpeed() }

        averages.sum() ? (averages.sum() / averages.size()) as Integer : 0
    }

    @Override
    int compareTo(AutomotiveDriver otherDriver) {
        otherDriver.getTotalDistance() <=> this.getTotalDistance() ?: this.name <=> otherDriver.name
    }

    @Override
    String toString() {
        Integer totalDistance = getTotalDistance()

        if (totalDistance)
            "$name $totalDistance miles @ $averageSpeed mph"
        else
            "$name $totalDistance miles"
    }
}