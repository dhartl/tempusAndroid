package at.c02.tempus.app.ui;

import android.content.Context;

import rx.Subscription;

/**
 * Created by Daniel on 08.04.2017.
 */

public abstract class AbstractPresenter{

    private Context view;

    private Subscription subscription;

    public AbstractPresenter(Context view) {
        this.view = view;

    }

    //unsubscribe the observable
    public void rxUnSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
