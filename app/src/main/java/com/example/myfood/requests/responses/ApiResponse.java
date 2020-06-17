package com.example.myfood.requests.responses;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {

    public ApiResponse<T> create(Throwable error) {
        return new ApiErrorResponse<>(!error.getMessage().equals("") ? error.getMessage() : "unknown error \n check Network connection");
    }

    public ApiResponse<T> create(Response<T> response) {
        if (response.isSuccessful()) {
            T body = response.body();
            if (body == null || response.code() == 204) {//204 is empty response code
                return new ApiEmptyResponse();

            } else
                return new ApiSuccessResponse<>(body);
        }else{
            String errorMesg="";
            try {
                errorMesg=response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
                errorMesg=response.message();
            }
            return new ApiErrorResponse<>(errorMesg);
        }
    };


    public class ApiSuccessResponse<T> extends ApiResponse<T> {
        private T body;

        public ApiSuccessResponse(T body) {
            this.body = body;
        }

        public T getBody() {
            return body;
        }
    }

    public class ApiErrorResponse<T> extends ApiResponse<T> {
        private String errorMessage;

        public ApiErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public class ApiEmptyResponse<T> extends ApiResponse<T> {
    }
}
