package com.seriabov.fintecharch.util;

import com.seriabov.fintecharch.data.Api;

public class Injection {
    private static Api apiService;

    public static Api provideApiService() {
        if (apiService == null) {
            synchronized (Api.class) {
                if (apiService == null) {
                    apiService = Api.Factory.create();
                }
            }
        }
        return apiService;
    }
}
