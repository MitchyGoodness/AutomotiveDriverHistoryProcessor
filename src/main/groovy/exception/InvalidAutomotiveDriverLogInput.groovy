package exception

class InvalidAutomotiveDriverLogInput extends RuntimeException {
    String message

    InvalidAutomotiveDriverLogInput(String message) { this.message = message }

    InvalidAutomotiveDriverLogInput() {}
}