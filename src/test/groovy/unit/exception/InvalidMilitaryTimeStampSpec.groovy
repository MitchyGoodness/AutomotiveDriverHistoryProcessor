package unit.exception

import exception.InvalidMilitaryTimeStamp
import spock.lang.Specification

class InvalidMilitaryTimeStampSpec extends Specification {
    void "default constructor initializes correctly"() {
        given: 'calling constructor'
        Exception e = new InvalidMilitaryTimeStamp()

        expect: 'exception is initialized correctly'
        e.message == null
    }

    void "constructor initializes correctly"() {
        given: 'calling constructor'
        Exception e = new InvalidMilitaryTimeStamp("Message")

        expect: 'exception is initialized correctly'
        e.message == "Message"
    }
}