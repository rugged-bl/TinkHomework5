package com.seriabov.fintecharch.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.seriabov.fintecharch.data.entities.CoinInfo;
import com.seriabov.fintecharch.util.Injection;

import java.util.List;
import java.util.Objects;

import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {
    public final MutableLiveData<MainViewState> coinInfoLiveData;
    public final MutableLiveData<Event<CoinInfo>> navigateToDetailsLiveData;
    private final MainViewState loadingState;

    public MainActivityViewModel() {
        this.coinInfoLiveData = new MutableLiveData<>();
        this.navigateToDetailsLiveData = new MutableLiveData<>();
        this.loadingState = new MainViewState(null, null, true);

        onRefreshDataButton();
    }

    private void onNewCoinInfo(List<CoinInfo> coinsInfo) {
        if (coinInfoLiveData.getValue() == null || !Objects.equals(coinInfoLiveData.getValue().coinsInfo, coinsInfo))
            coinInfoLiveData.postValue(new MainViewState(coinsInfo, null, false));
    }

    private void onError(Throwable throwable) {
        coinInfoLiveData.postValue(new MainViewState(null, throwable, false));
    }

    private void onLoading() {
        coinInfoLiveData.postValue(loadingState);
    }

    public void onAdapterItemChosen(CoinInfo coinInfo) {
        navigateToDetailsLiveData.postValue(new Event<>(coinInfo));
    }

    public void onRefreshDataButton() { //за этот ход с AppDelegate я не уверен
        onLoading();
        Injection.provideApiService()
                .getCoinsList()
                .subscribeOn(Schedulers.io())
                .subscribe(this::onNewCoinInfo, this::onError);
    }
}