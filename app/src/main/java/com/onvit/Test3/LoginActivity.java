package com.onvit.Test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

public class LoginActivity extends AppCompatActivity {
    SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String userId = getSharedPreferences(getPackageName(), MODE_PRIVATE).getString("userId", null);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        Button mainBtn = findViewById(R.id.mainBtn);
        mainBtn.setOnClickListener((view) -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            e.printStackTrace();
        }
    }

    public void requestMe() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("requestMeSessionClosed", "error : " + errorResult );
                Toast.makeText(getApplicationContext(), errorResult.getErrorMessage(), Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("requestMeFailure", "error : " + errorResult );
                Toast.makeText(getApplicationContext(), errorResult.getErrorMessage(), Toast.LENGTH_SHORT ).show();

                if (errorResult.getErrorCode() == ApiErrorCode.CLIENT_ERROR_CODE) {
                    Toast.makeText(getApplicationContext(), "연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onSuccess(MeV2Response result) {
                String userId = Long.toString(result.getId());
                String userEmail = result.getKakaoAccount().getEmail();
                Log.d("requestMeSuccess", userId + " , " + userEmail);

                SharedPreferences.Editor editor = getSharedPreferences(getPackageName(),MODE_PRIVATE).edit();
                editor.putString("userId", userId);
                editor.putString("userEmail", userEmail );
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
