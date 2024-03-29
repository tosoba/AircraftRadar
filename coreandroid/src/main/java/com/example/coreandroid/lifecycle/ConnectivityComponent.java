package com.example.coreandroid.lifecycle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.coreandroid.R;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ConnectivityComponent implements LifecycleObserver {
    private final Activity activity;
    private boolean isDataLoaded;
    private final View parentView;
    private final ReloadsData reloadsData;

    public ConnectivityComponent(Activity activity, boolean isDataLoaded, View parentView, ReloadsData reloadsData) {
        this.activity = activity;
        this.isDataLoaded = isDataLoaded;
        this.parentView = parentView;
        this.reloadsData = reloadsData;
    }

    private Disposable internetDisposable;
    private boolean connectionInterrupted = false;
    private boolean lastConnectionStatus;

    public boolean getLastConnectionStatus() {
        return lastConnectionStatus;
    }

    private Snackbar snackbar;
    private boolean isSnackbarShowing = false;

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void observeInternetConnectivity() {
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    lastConnectionStatus = isConnectedToInternet;
                    handleConnectionStatus(isConnectedToInternet);
                });
    }

    private void handleConnectionStatus(boolean isConnectedToInternet) {
        if (!isConnectedToInternet) {
            connectionInterrupted = true;
            if (!isSnackbarShowing) {
                showNoConnectionDialog();
            }
        } else {
            if (connectionInterrupted) {
                connectionInterrupted = false;
                if (!isDataLoaded && reloadsData != null) {
                    reloadsData.reloadData();
                }
            }

            isSnackbarShowing = false;
            if (snackbar != null) snackbar.dismiss();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void clear() {
        if (snackbar != null)
            snackbar.dismiss();
        snackbar = null;
        safelyDispose(internetDisposable);
    }

    private void safelyDispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void showNoConnectionDialog() {
        snackbar = Snackbar
                .make(parentView, "No internet connection.", Snackbar.LENGTH_LONG)
                .setAction("Settings", view -> {
                    Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                    activity.startActivity(settingsIntent);
                })
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_SWIPE) {
                            showNoConnectionDialog();
                        }
                    }
                })
                .setActionTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

        TextView textView = snackbar.getView().findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        snackbar.getView().setBackgroundColor(Color.LTGRAY);
        snackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    public interface ReloadsData {
        void reloadData();
    }
}
