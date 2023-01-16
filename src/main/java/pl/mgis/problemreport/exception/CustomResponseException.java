package pl.mgis.problemreport.exception;

public class CustomResponseException extends RuntimeException {
    private int httpCode;

    public CustomResponseException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
}
