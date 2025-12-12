package com.ssafy.zipcheck.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final HttpStatus status;
    private final String message;
    private final T data;

    @Builder
    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // ============================
    // 성공 응답
    // ============================
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> ok() {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.CREATED)
                .message(message)
                .data(null)
                .build();
    }

    // ============================
    // 실패 응답
    // ============================
    public static <T> ApiResponse<T> badRequest(String msg) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(msg)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> notFound(String msg) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.NOT_FOUND)
                .message(msg)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> conflict(String msg) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.CONFLICT)
                .message(msg)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> internalError(String msg) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(msg)
                .data(null)
                .build();
    }
}
