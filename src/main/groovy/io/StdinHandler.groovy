package io

class StdinHandler implements InputHandler {
    BufferedReader stdin

    StdinHandler() {
        this.stdin = System.in.newReader()
    }

    List<String> getTextInputs() {
        this.stdin.readLines() as List<String>
    }
}