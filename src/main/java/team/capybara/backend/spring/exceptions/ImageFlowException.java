package team.capybara.backend.spring.exceptions;

public class ImageFlowException extends RuntimeException {
    public ImageFlowException() {
        super();
    }

    public ImageFlowException(String message) {
        super(message);
    }

    public ImageFlowException(Throwable cause) {
        super(cause);
    }

    public ImageFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}
