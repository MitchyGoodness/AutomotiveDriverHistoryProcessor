package integration.io

import io.FileHandler
import spock.lang.Specification
import spock.lang.Unroll

class FileHandlerSpec extends Specification {
    @Unroll
    void "constructor initializes and validates correctly for good file path"() {
        given:
        String filePath = "src/test/resources/${fileName}.txt"

        when: 'calling constructor'
        FileHandler fileHandler = new FileHandler(filePath)

        then: 'fields are set and validation passes'
        fileHandler.filePath == filePath
        fileHandler.file == new File(filePath)

        where:
        fileName                | _
        "AutomotiveDriverInput" | _
        "empty"                 | _
    }

    void "constructor throws exception for bad file path"() {
        given:
        String filePath = "src/test/resources/"

        when: 'calling constructor'
        new FileHandler(filePath)

        then: 'Throws exception'
        Exception e = thrown(IOException)
        e.message == $/"$filePath" is not a file./$
    }

    void "getTextInputs returns a list of strings match file contents"() {
        given:
        String filePath = "src/test/resources/AutomotiveDriverInput.txt"
        FileHandler fileHandler = new FileHandler(filePath)

        when: 'calling constructor'
        List<String> inputs = fileHandler.getTextInputs()

        then: 'file contents matches'
        inputs[0] == "Driver mitch"
        inputs[1] == "Trip mitch 01:00 02:00 30"
        inputs[2] == "Trip mitch 01:00 02:00 30"
        inputs[3] == "Trip mitch 01:00 02:00 30"
        inputs[4] == "Driver mark"
        inputs[5] == "Trip mark 03:13 03:36 29"
        inputs[6] == "Trip mark 01:04 01:19 28"
        inputs[7] == "Trip mark 10:49 11:05 20"
        inputs[8] == "Driver tina"
    }
}
