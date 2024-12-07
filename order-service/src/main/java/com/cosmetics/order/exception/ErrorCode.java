package com.cosmetics.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ORDER_NOT_EXISTED(1005, "Order not existed", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    PAYMENT_FAILED(1008, "Payment transaction failed", HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_METHOD(1009, "Invalid payment method", HttpStatus.BAD_REQUEST)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
