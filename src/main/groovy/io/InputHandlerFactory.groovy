package io

class InputHandlerFactory {
    String[] args

    InputHandlerFactory(String... args) {
        this.args = args
    }

    InputHandler getInputHandler() {
        if (args) {
            return new FileHandler(args[0])

        } else {
            return new StdinHandler()
        }
    }
}
