package at.c02.tempus.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;

import at.c02.tempus.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusActivity;

/**
 * Created by Daniel Hartl on 04.05.2017.
 */
@RequiresPresenter(AuthPresenter.class)
public class AuthActivity extends NucleusActivity<AuthPresenter> {

    @BindView(R.id.txtAuthMessage)
    protected TextView txtAuthMessage;

    @BindView(R.id.btnAuthenticate)
    protected Button btnAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
    }

    public void setAuthMessage(String authMessage) {
        txtAuthMessage.setText(authMessage);
    }

    public void enableLoginButton(boolean enabled){
        btnAuthenticate.setEnabled(enabled);
    }

    @OnClick(R.id.btnAuthenticate)
    protected void onBtnAuthenticateClick() {
        getPresenter().authenticate();
    }

}
