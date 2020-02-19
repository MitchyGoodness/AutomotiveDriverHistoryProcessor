package unit.automotivedriverhistoryprocessor

import automotivedriverhistoryprocessor.AutomotiveDriverHistory
import automotivedriverhistoryprocessor.AutomotiveDriverHistoryProcessor
import exception.InvalidAutomotiveDriverLogInput
import exception.InvalidMilitaryTimeStamp
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime
import java.util.regex.Pattern

class AutomotiveDriverHistoryProcessorSpec extends Specification {
    void "Regular expression pattern INTEGER_OR_DOUBLE is not null and validates integers or doubles"() {
        expect:
        AutomotiveDriverHistoryProcessor?.INTEGER_OR_DOUBLE instanceof Pattern
        "1" ==~ AutomotiveDriverHistoryProcessor.INTEGER_OR_DOUBLE
        "1.0" ==~ AutomotiveDriverHistoryProcessor.INTEGER_OR_DOUBLE
        !("a" ==~ AutomotiveDriverHistoryProcessor.INTEGER_OR_DOUBLE)
    }

    void "Regular expression pattern MILITARY_TIME_FORMAT is not null and validates military style time"() {
        expect:
        AutomotiveDriverHistoryProcessor?.MILITARY_TIME_FORMAT instanceof Pattern
        "01:00" ==~ AutomotiveDriverHistoryProcessor.MILITARY_TIME_FORMAT
        !("1:00" ==~ AutomotiveDriverHistoryProcessor.MILITARY_TIME_FORMAT)
        !("26:00" ==~ AutomotiveDriverHistoryProcessor.MILITARY_TIME_FORMAT)
    }

    void "constructor initializes correctly"() {
        when: 'calling constructor'
        List<String> textInputs = []
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(textInputs)

        then: 'object is initialized correctly'
        processor.textInputs instanceof List<String>
        processor.textInputs == textInputs
        processor.history instanceof AutomotiveDriverHistory
    }

    void "getHistory returns AutomotiveDriverHistory"() {
        given:
        Boolean ranProcessInputs = false
        List<String> textInputs = []

        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(textInputs)

        when: 'stubbing processInputs()'
        processor.metaClass.processInputs = { -> ranProcessInputs = true } as Closure<Void>

        and: 'getHistory is called'
        def history = processor.getHistory()

        then: 'processInputs was called and AutomotiveDriverHistory was returned'
        history instanceof AutomotiveDriverHistory
        ranProcessInputs
    }

    void "processInputs processes the given list of strings"() {
        given: 'Two strings in textInputs'
        Integer processInputCallCounter = 0
        List<String> textInputs = ["1", "2"]

        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(textInputs)

        when: 'stubbing processInput(String textInput)'
        processor.metaClass.processInput = { String _ -> processInputCallCounter++ } as Closure<Void>

        and: 'processInputs is called'
        processor.processInputs()

        then: 'processInput was called twice'
        processInputCallCounter == 2
    }

    @Unroll
    void "processInput processes #input calling #command"() {
        given:
        Boolean addDriverCalled = false
        Boolean addTripCalled = false

        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        and: "stubbing processor commands"
        processor.metaClass.addDriver = { List<String> _ -> addDriverCalled = true } as Closure<Void>
        processor.metaClass.addTrip = { List<String> _ -> addTripCalled = true } as Closure<Void>

        when:
        processor.processInput(input)

        then:
        addDriverCalled == expectedAddDriverCalled
        addTripCalled == expectedAddTripCalled

        where:
        input                       | expectedAddDriverCalled | expectedAddTripCalled
        "Driver Galvin"             | true                    | false
        "Trip Galvin 00:00 00:00 0" | false                   | true
    }

    void "processInput for bad input throws exception"() {
        given:
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        when:
        processor.processInput("Bad input")

        then:
        Exception e = thrown(InvalidAutomotiveDriverLogInput)
        e.message == "Bad is not a valid command from input: Bad input"
    }

    void "addDriver calls history.addDriver and passes new driver name"() {
        given:
        String driverName = 'Ike'
        List<String> args = ['Driver', driverName]
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        and: "stubbed history.addDriver"
        String actualResult
        processor.history.metaClass.addDriver = { String name -> actualResult = name } as Closure<Void>

        when:
        processor.addDriver(args)

        then:
        actualResult
        actualResult == driverName
    }

    void "addDriver handles bad input by throwing exception"() {
        given:
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        when:
        processor.addDriver([])

        then:
        Exception e = thrown(InvalidAutomotiveDriverLogInput)
        e.message == $/Command "Driver" expects 1 parameter got: ./$
    }

    void "addTrip calls several parsing functions and passes result"() {
        given: 'Expected input'
        String driverName = 'Elijah'
        LocalTime startTime = LocalTime.of(0, 0)
        LocalTime endTime = LocalTime.of(1, 0)
        Double distance = 50.0

        List<String> args = ['Trip', driverName, "$startTime", "$endTime", "$distance"]
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        and: "stubbed parseTime(String timeType, String timeStamp)"
        Map parseTimeCallOutput = [:]
        processor.metaClass.parseTime = { String timeType, String parseTimeInput ->
            parseTimeCallOutput.put(timeType, parseTimeInput)
            return timeType == 'start time' ? startTime : endTime
        } as Closure<LocalTime>

        and: "stubbed parseDistance(String distance)"
        String parseDistanceOutput
        processor.metaClass.parseDistance = { String parseDistanceInput ->
            parseDistanceOutput = parseDistanceInput
            return distance
        } as Closure<Double>

        and: "stubbed history.addTrip()"
        List historyAddTripInput
        processor.history.metaClass.addTrip = { String arg1, LocalTime arg2, LocalTime arg3, Double arg4 ->
            historyAddTripInput = [arg1, arg2, arg3, arg4]
        } as Closure<Void>

        when: 'Adding a trip'
        processor.addTrip(args)

        then: 'parseTime is passed the start time and end time with matching identifier'
        parseTimeCallOutput['start time'] == "$startTime"
        parseTimeCallOutput['end time'] == "$endTime"

        and: 'parseDistance is passed the distance'
        parseDistanceOutput == "$distance"

        and: 'history.addTrip() is passed all args in the correct order'
        historyAddTripInput[0] == driverName
        historyAddTripInput[1] == startTime
        historyAddTripInput[2] == endTime
        historyAddTripInput[3] == distance
    }

    void "addTrip handles bad input by throwing exception"() {
        given:
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        when:
        processor.addTrip([])

        then:
        Exception e = thrown(InvalidAutomotiveDriverLogInput)
        e.message == $/Command "Trip" expects 4 parameter got: ./$
    }

    @Unroll
    void "parseTime parses valid input for #timeType correctly"() {
        given:
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)
        LocalTime actualTime = processor.parseTime(timeType, timeStamp)

        expect:
        actualTime.getHour() == hour
        actualTime.getMinute() == minute

        where:
        timeType     | timeStamp | hour | minute
        'start time' | "01:30"   | 1    | 30
        'end time'   | "05:24"   | 5    | 24
    }

    void "parseTime throws InvalidAutomotiveDriverLogInput exception for missing timestamp"() {
        given:
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        when:
        processor.parseTime('start time', null)

        then: 'Exception is thrown'
        Exception e = thrown(InvalidAutomotiveDriverLogInput)
        e.message == 'Missing input "start time"'
    }

    void "parseTime throws InvalidMilitaryTimeStamp exception for invalid timestamp"() {
        given:
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        when:
        processor.parseTime('end time', "35:11")

        then: 'Exception is thrown'
        Exception e = thrown(InvalidMilitaryTimeStamp)
        e.message == 'Expected end time in kk:mm (military time) format, got "35:11".'
    }

    void "parseDistance parses valid input correctly"() {
        given:
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        when:
        processor.parseDistance(null)

        then: 'Exception is thrown'
        Exception e = thrown(InvalidAutomotiveDriverLogInput)
        e.message == 'Missing input "distance" for trip log.'
    }

    void "parseDistance throws exception for invalid input"() {
        given:
        String badDistanceInput = "a50.6"
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)

        when:
        processor.parseDistance(badDistanceInput)

        then: 'Exception is thrown'
        Exception e = thrown(InvalidAutomotiveDriverLogInput)
        e.message == $/Expected double or integer format for trip distance, got "$badDistanceInput"./$
    }
}