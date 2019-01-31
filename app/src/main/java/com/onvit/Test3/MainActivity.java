package com.onvit.Test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;




import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    /* keyhash new
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNING_CERTIFICATES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signingInfo.getApkContentsSigners()) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String signatureBase64 = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                Log.d("signatureBase64", signatureBase64);
                return signatureBase64;
            } catch (NoSuchAlgorithmException e) {
                Log.w("msg", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }*/

    /* keyhash old
    private void getHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.onvit.Test3", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String signatureBase64 = new String(Base64.encode(md.digest(), Base64.DEFAULT));
                Log.d("success","key_hash : "+ signatureBase64 + " End");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }*/
}
