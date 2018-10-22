package com.seriabov.fintecharch.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.seriabov.fintecharch.R;
import com.seriabov.fintecharch.data.entities.CoinInfo;
import com.seriabov.fintecharch.viewModel.MainActivityViewModel;
import com.seriabov.fintecharch.viewModel.MainViewState;

import java.util.List;

import timber.log.Timber;

/*
 * TODO:
 * 1) Подключить ViewModel и LiveData из Android Architecture components
 * 2) Разделить классы по пакетам
 * 3) Внедрить в проект архитектуру MVVM, вынести бизнес-логику во вьюмодель.
 * В идеале вьюмодель не должна содержать в себе андроид-компонентов (таких как Context)
 * 4) Сделать так, чтобы при повороте экрана данные не перезапрашивались заново,
 * а использовались полученные ранее
 * 5) Don't repeat yourself - если найдете в коде одинаковые куски кода, выносите в утилитные классы
 */

public class MainActivity extends AppCompatActivity {

    private CoinsAdapter adapter;
    private View errorView;
    private View contentView;
    private View loadingView;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        findViewById(R.id.fab).setOnClickListener(view -> onRefreshDataButton());

        errorView = findViewById(R.id.error_layout);
        contentView = findViewById(R.id.main_recycler_view);
        loadingView = findViewById(R.id.loading_layout);
        initRecyclerView();

        viewModel.coinInfoLiveData.observe(this, this::applyNewViewState);
        viewModel.navigateToDetailsLiveData.observe(this, coinInfoEvent -> {
            CoinInfo coinInfo;
            if (coinInfoEvent != null && (coinInfo = coinInfoEvent.getContentIfNotHandled()) != null)
                DetailsActivity.start(MainActivity.this, coinInfo);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            onRefreshDataButton();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void applyNewViewState(MainViewState mainViewState) {
        if (mainViewState != null) {
            if (mainViewState.throwable != null)
                showError(mainViewState.throwable);
            else if (mainViewState.coinsInfo != null)
                setData(mainViewState.coinsInfo);
            else if (mainViewState.isLoading)
                showLoading();
        }
    }

    private void onRefreshDataButton() {
        viewModel.onRefreshDataButton();
    }

    private void onAdapterItemChosen(CoinInfo coinInfo) {
        viewModel.onAdapterItemChosen(coinInfo);
    }

    private void setData(List<CoinInfo> data) {
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        adapter.setData(data);
    }

    private void showError(Throwable error) {
        Timber.d(error);
        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CoinsAdapter(this::onAdapterItemChosen);
        recyclerView.setAdapter(adapter);
    }
}
