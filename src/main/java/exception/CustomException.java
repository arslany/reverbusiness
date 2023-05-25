package exception;

public class CustomException extends RuntimeException {

    public enum ErrorCode {
        UNIQUE_CONSTRAINT_VIOLATION(1001, "The value already exists in the database"),
        INVALID_INPUT(1002, "The input is not valid"),
        RESOURCE_NOT_FOUND(1003, "The requested resource is not found"),
        INTERNAL_SERVER_ERROR(1004, "An unexpected error occurred on the server"),
        RECORD_NOT_FOUND(1005, "The requested record not found"),
        USER_NOT_FOUND(1005, "The requested user %s not found"),
        INVALID_USER_NAME_OR_PASSWORD(1006, "Invalid user name or password"),
        USER_ALREADY_LOGGED_IN(1007,"User already logged in"),
        AUTHENTICATION_REQUIRED(1008,"Authentication required")
        ;

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

        // Add a parameter to the getDescription method
        public String getDescription(String param) {
            return String.format(description, param);
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

    public CustomException(ErrorCode errorCode, String message, boolean isParameter) {
        super(errorCode.getDescription(message));
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