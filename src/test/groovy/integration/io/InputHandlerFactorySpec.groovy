package integration.io

import io.FileHandler
import io.InputHandlerFactory
import io.StdinHandler
import spock.lang.Specification

class InputHandlerFactorySpec extends Specification {
    void "constructor initializes and validates correctly for good file path"() {
        given:
        String[] args = ["non-empty input"]

        when: 'calling constructor'
        InputHandlerFactory inputHandlerFactory = new InputHandlerFactory(args)

        then: 'inputHandlerFactory.args are initialized with provided input args'
        inputHandlerFactory.args == args
    }

    void "getInputHandler returns FileHandler when file path is provided"() {
        given:
        String[] args = ["src/test/resources/AutomotiveDriverInput.txt"]

        when:
        InputHandlerFactory inputHandlerFactory = new InputHandlerFactory(args)

        then:
        inputHandlerFactory.getInputHandler().class == FileHandler
    }

    void "getInputHandler returns stdin when no file path is provided"() {
        given:
        String[] args = null

        when:
        InputHandlerFactory inputHandlerFactory = new InputHandlerFactory(args)

        then:
        inputHandlerFactory.getInputHandler().class == StdinHandler
    }
}
