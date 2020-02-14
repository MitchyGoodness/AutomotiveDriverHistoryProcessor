package io

class FileHandler implements InputHandler {
    String filePath
    File file

    FileHandler(String filePath) {
        this.filePath = filePath
        this.file = new File(filePath)

        validate()
    }

    protected void validate() {
        if (!file.isFile())
            throw new IOException($/"$filePath" is not a file./$)
    }

    @Override
    List<String> getTextInputs() {
        file.readLines() as List<String>
    }
}