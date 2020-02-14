package exception

class InvalidMilitaryTimeStamp extends InvalidAutomotiveDriverLogInput {
    String message

    InvalidMilitaryTimeStamp(String message) { this.message = message }

    InvalidMilitaryTimeStamp() {}
}