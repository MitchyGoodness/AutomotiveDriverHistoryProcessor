package automotivedriverhistoryprocessor

import io.InputHandlerFactory

class Main {
    static void main(String... args) {
        List<String> textInputs = new InputHandlerFactory(args).getInputHandler().getTextInputs()
        AutomotiveDriverHistory history = new AutomotiveDriverHistoryProcessor(textInputs).getHistory()

        println history
    }
}