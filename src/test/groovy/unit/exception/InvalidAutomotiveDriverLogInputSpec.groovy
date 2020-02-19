package unit.exception

import exception.InvalidAutomotiveDriverLogInput
import spock.lang.Specification


class InvalidAutomotiveDriverLogInputSpec extends Specification {
    void "default constructor initializes correctly"() {
        given: 'calling constructor'
        Exception e = new InvalidAutomotiveDriverLogInput()


        expect: 'exception is initialized correctly'
        e.message == null
    }

    void "constructor initializes correctly"() {
        given: 'calling constructor'
        Exception e = new InvalidAutomotiveDriverLogInput("Message")


        expect: 'exception is initialized correctly'
        e.message == "Message"
    }
}