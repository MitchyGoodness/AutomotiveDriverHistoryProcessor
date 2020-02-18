package unit

import automotivedriverhistoryprocessor.AutomotiveDriver
import automotivedriverhistoryprocessor.AutomotiveDriverHistory
import automotivedriverhistoryprocessor.AutomotiveDriverHistoryProcessor
import automotivedriverhistoryprocessor.Trip
import exception.InvalidAutomotiveDriverLogInput
import spock.lang.Ignore
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

    @Ignore
    void "addTrip calls history.addTrip"() {
//        given:
//        String driverName = 'Elijah'
//        LocalTime startTime = '00:00'
//        LocalTime endTime = '01:00'
//        Double distance = 50.0
//        List<String> args = ['Trip', driverName, startTime, endTime, '50']
//        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor(null)
//
//        and: "stubbed parseTime(String timeType, String timeStamp)"
//        processor.metaClass.parseTime = { String timeType, List<String> parseTimeInput ->
//            actualResult = addTripInput
//        } as Closure<Void>
//
//        and: "stubbed parseDistance(String distance)"
//        processor.metaClass.parseDistance = { List<String> parseDistanceInput ->
//            actualResult = addTripInput
//        } as Closure<Void>
//
//        and: "stubbed history.addTrip()"
//        List<String> actualResult
//        processor.history.metaClass.addTrip = { List<String> addTripInput ->
//            actualResult = addTripInput
//        } as Closure<Void>
//
//        when:
//        processor.addDriver(args)
//
//        then:
//        actualResult
//        actualResult == driverName
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
}