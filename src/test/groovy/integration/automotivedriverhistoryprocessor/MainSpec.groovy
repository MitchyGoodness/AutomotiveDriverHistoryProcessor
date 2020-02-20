package integration.automotivedriverhistoryprocessor

import automotivedriverhistoryprocessor.Main
import spock.lang.Specification
import spock.lang.Unroll

class MainSpec extends Specification {
    @Unroll
    void "Take a file of inputs and print list of sorted drivers and their statistics"() {
        given: "File"
        String filePath = "src/test/resources/${fileName}.txt"

        and: "Replace stdout buffer with one accessible within the test"
        ByteArrayOutputStream buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        when: "processing file and getting driver history"
        Main.main([filePath] as String[])

        and: "actual output is formatted in the unix line return format"
        String actualOutput = buffer.toString().replaceAll(/(\r\n|\r)/, "\n").replaceAll(/\n$/, "")

        then: "Printed output matches expectation"
        actualOutput == expectedOutput

        where:
        fileName                | expectedOutput
        "AutomotiveDriverInput" | "mitch: 90 miles @ 30 mph\nmark: 49 miles @ 75 mph\ntina: 0 miles"
        "empty"                 | ""
    }
}