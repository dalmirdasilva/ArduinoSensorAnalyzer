package serialclient;

public class SerialClientException extends Exception {

    public SerialClientException(String exceptionMessage) {
        super("SerialClientException: " + exceptionMessage);
    }
}
