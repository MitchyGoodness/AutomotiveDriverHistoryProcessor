package automotivedriverhistoryprocessor

import io.InputHandlerFactory

class Main {
    static void main(String... args) {
        AutomotiveDriverHistoryProcessor processor = new AutomotiveDriverHistoryProcessor()
        AutomotiveDriverHistory history = new AutomotiveDriverHistory()
        List<String> textInputs = new InputHandlerFactory(args).getInputHandler().getTextInputs()

        textInputs.each { String textInput -> processor.processInput(history, textInput) }
        history.drivers.sort().each { AutomotiveDriver driverLog -> println driverLog }
    }
}
