package com.majiang.community.exception;

/**
 * 为什么要继承RuntimeException呢？
 * 如果不继承runtimeException，假如我们在questionservice类方法中直接把Exception throw出来的话，那么我们必须要在上一层进行捕获（try catch）,
 * 所以说这个异常我不需要在调用的时候有任何的影响，而仅仅在我们的controllerAdvice中去try catch就好了
 */
public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(ICustomizeErrorCode customizeErrorCode) {
        this.message = customizeErrorCode.getMessage();
    }

    public CustomizeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
