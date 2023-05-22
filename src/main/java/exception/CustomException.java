package exception;

public class CustomException extends RuntimeException {

    // Enum to store error codes and descriptions
    public enum ErrorCode {
        UNIQUE_CONSTRAINT_VIOLATION(1001, "The value already exists in the database"),
        INVALID_INPUT(1002, "The input is not valid"),
        RESOURCE_NOT_FOUND(1003, "The requested resource is not found"),
        INTERNAL_SERVER_ERROR(1004, "An unexpected error occurred on the server");

        private final int code;
        private final String description;

        private ErrorCode(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    // Instance variable to store the error code
    private final ErrorCode errorCode;

    // Constructor with error code and cause
    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDescription(), cause);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    // Constructor with error code and message only
    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // Constructor with error code only
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    // Getter for error code
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}