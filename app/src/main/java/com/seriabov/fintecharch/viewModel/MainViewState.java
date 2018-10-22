package com.seriabov.fintecharch.viewModel;

import com.seriabov.fintecharch.data.entities.CoinInfo;

import java.util.List;

public class MainViewState {
    public final List<CoinInfo> coinsInfo;
    public final Throwable throwable;
    public final boolean isLoading;

    MainViewState(List<CoinInfo> coinsInfo, Throwable throwable, boolean isLoading) {
        this.coinsInfo = coinsInfo;
        this.throwable = throwable;
        this.isLoading = isLoading;
    }
}
