package integration.io

import io.StdinHandler
import spock.lang.Specification
import spock.lang.Unroll


class StdinHandlerSpec extends Specification {
    @Unroll
    void "constructor initializes and validates correctly for good file path"() {
        when: 'calling constructor'
        StdinHandler stdinHandler = new StdinHandler()

        then: 'stdin is populated with a buffered reader'
        stdinHandler.stdin
        stdinHandler.stdin instanceof BufferedReader
    }

    void "getTextInputs returns list of strings matching the test string"() {
        given:
        String expectedText = "test"
        System.setIn(new ByteArrayInputStream(expectedText.getBytes()))

        when: 'calling constructor'
        StdinHandler stdinHandler = new StdinHandler()

        then: 'fields are set and validation passes'
        stdinHandler.getTextInputs() == [expectedText]
    }
}
