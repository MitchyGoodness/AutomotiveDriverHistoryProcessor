package automotivedriverhistoryprocessor

import exception.InvalidAutomotiveDriverLogInput
import exception.InvalidMilitaryTimeStamp

import java.time.LocalTime
import java.util.regex.Pattern

class AutomotiveDriverHistoryProcessor {
    final static Pattern INTEGER_OR_DOUBLE = ~/^\d+|\d+\.\d+$/
    final static Pattern MILITARY_TIME_FORMAT = ~/^[0-2][0-9]:[0-6][0-9]$/

    List<String> textInputs
    AutomotiveDriverHistory history

    AutomotiveDriverHistoryProcessor(List<String> textInputs) {
        this.textInputs = textInputs
        this.history = new AutomotiveDriverHistory()
    }

    AutomotiveDriverHistory getHistory() {
        processInputs(textInputs)
        this.history
    }

    void processInputs(List<String> textInputs) {
        textInputs.each { String textInput -> processInput(textInput) }
    }

    void processInput(String input) {
        List<String> args = input.split()

        String command = args[0]

        switch (command) {
            case "Driver":
                addDriver(args)
                break

            case "Trip":
                addTrip(args)
                break

            default:
                throw new InvalidAutomotiveDriverLogInput("$command is not a valid command from input: $input")
        }
    }

    void addDriver(List<String> args) {
        if (args.size() == 2)
            history.addDriver(args[1])
        else
            throw new InvalidAutomotiveDriverLogInput($/Command "Driver" expects 1 parameter got: ${args.join(" ")}./$)

    }

    void addTrip(List<String> args) {
        if (args.size() == 5)
            history.addTrip(
                    args[0],
                    parseTime("start time", args[0]),
                    parseTime("end time", args[1]),
                    parseDistance(args[2])
            )
        else
            throw new InvalidAutomotiveDriverLogInput($/Command "Trip" expects 4 parameter got: ${args.join(" ")}./$)
    }


    LocalTime parseTime(String timeType, String timeStamp) {
        validateTimeInput(timeType, timeStamp)

        List timeParts = timeStamp.split(":")
        LocalTime.of((timeParts[0] as Integer), (timeParts[1] as Integer))
    }

    Double parseDistance(String distance) {
        validateDistanceInput(distance)

        distance as Double
    }

    private void validateDistanceInput(String distance) throws InvalidAutomotiveDriverLogInput {
        if (!distance)
            throw new InvalidAutomotiveDriverLogInput($/Missing input "distance" for trip log./$)

        if (!(distance ==~ INTEGER_OR_DOUBLE))
            throw new InvalidAutomotiveDriverLogInput($/Expected double or integer format for trip distance, got "$distance"./$)
    }

    private void validateTimeInput(String timeType, String timeStamp) {
        if (!timeStamp)
            throw new InvalidAutomotiveDriverLogInput($/Missing input "$timeType"/$)

        if (!(timeStamp ==~ MILITARY_TIME_FORMAT))
            throw new InvalidMilitaryTimeStamp($/Expected $timeType in kk:mm (military time) format, got "$timeStamp"./$)
    }
}