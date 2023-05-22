package exception;

import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;

public class CustomExceptionHandler implements ExceptionHandler<CustomException> {
    @Override
    public void handle(CustomException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(e.getErrorCode().getCode());
        errorResponse.setMessage(e.getMessage());
        ctx.status(400); // Bad Request
        ctx.json(errorResponse);
    }
}