package com.appsys.utils;

public class JSONParsingException extends ApiException {
    private String mResponse;

    public JSONParsingException(String s, String response) {
        super("Api response is not json: " + s);
        mResponse = response;
    }

    public String getResponse() {
        return mResponse;
    }
}
