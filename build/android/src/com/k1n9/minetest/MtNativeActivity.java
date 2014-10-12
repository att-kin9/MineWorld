package com.k1n9.minetest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.example.games.basegameutils.GameHelper;
import com.google.android.gms.games.Games;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import android.graphics.Color;

import android.app.NativeActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import android.app.NativeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;

import com.k1n9.minetest.R;

public class MtNativeActivity extends NativeActivity 
{
    private static MtNativeActivity appActivity_;
    private AdView adView_;
    private PopupWindow popUpWindow_;
    private LinearLayout layout_;
    private LinearLayout mainLayout_;
    boolean adsinited_ = false;
    private static final String BANNER_UNIT_ID = "";

	static private InterstitialAd mInterstitialAd;
    static private AdRequest mAdRequest;

    public static Handler UIHandler;
    GameHelper mHelper;

    private static boolean mLoginInSucceeded = false;
    private static final String FULLSCREENAD_UNITID = "";
    private static final int REQUEST_LEADERBOARD = 0;

    public static Object actInstance;
    public static Object rtnActivity()
    {
        return actInstance;
    }

    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable)
    {
        UIHandler.post(runnable);
    }

	@Override
	public void onCreate(Bundle savedInstanceState)
	 {
		super.onCreate(savedInstanceState);
		m_MessagReturnCode = -1;
		m_MessageReturnValue = "";

		// init banner ad
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        appActivity_ = this;

        adView_ = new AdView(this);
        adView_.setAdSize(AdSize.BANNER);
        adView_.setAdUnitId(BANNER_UNIT_ID);
        
		// init fullscreen ad
		actInstance = this;
        mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        GameHelper.GameHelperListener listener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInSucceeded()
            {
                mLoginInSucceeded = true;
            }

            @Override
            public void onSignInFailed()
            {
                mLoginInSucceeded = false;
            }
        };

        mHelper.setMaxAutoSignInAttempts(0);
        mHelper.setup(listener);
        // signIn();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(FULLSCREENAD_UNITID);
        mAdRequest = new AdRequest.Builder()
                    .build();
	}

	public static void showAdPopup()
	{
		appActivity_.showAdPopup_();
	}

	public void showAdPopup_()
	{
		if (adsinited_)
		{
			return;	
		}

		if (adView_ != null)
		{
			appActivity_.runOnUiThread(new Runnable() {
				@Override
				public void run()
				{
					adsinited_ = true;
					popUpWindow_ = new PopupWindow(appActivity_);

					popUpWindow_.setWidth(320);
					popUpWindow_.setHeight(50);
					popUpWindow_.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					popUpWindow_.setClippingEnabled(false);

					layout_ = new LinearLayout(appActivity_);
					mainLayout_ = new LinearLayout(appActivity_);
					layout_.setPadding(-5, -5, -5, -5);

					MarginLayoutParams params = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

					params.setMargins(0, 0, 0, 0);
					layout_.setOrientation(LinearLayout.VERTICAL);
					layout_.addView(adView_, params);
					popUpWindow_.setContentView(layout_);
					appActivity_.setContentView(mainLayout_, params);

					AdRequest adRequest = new AdRequest.Builder()
									.build();
					appActivity_.adView_.loadAd(adRequest);
					popUpWindow_.showAtLocation(mainLayout_, Gravity.TOP | Gravity.RIGHT, 0, 0);
					popUpWindow_.update();
				}
			});	
		}
	}

	private class AdmobListener extends AdListener {
		@Override
		public void onAdClosed() {
			super.onAdClosed();
		}

		@Override
		public void onAdFailedToLoad(int errorCode) {
			super.onAdFailedToLoad(errorCode);
		}

		@Override
		public void onAdLeftApplication() {
			super.onAdLeftApplication();
		}

		@Override
		public void onAdLoaded() {
			super.onAdLoaded();
		}

		@Override
		public void onAdOpened() {
			super.onAdOpened();
		}
	}

    @Override
    protected void onResume()
    {
        super.onResume();
        if (adView_ != null)
        {
            adView_.resume();    
        }
    }

    @Override
    protected void onPause()
    {
        if (adView_ != null)
        {
            adView_.pause();    
        }

        super.onPause();
    }

	@Override
    protected void onStart()
    {
        super.onStart();
        mHelper.onStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mHelper.onStop();
    }

	@Override
	public void onDestroy() 
	{
		if (adView_ != null)
		{
			adView_.destroy();	
		}

		super.onDestroy();
	}
	
	public void showFullScreenAd()
	{
		showAdPopup();
	}

	public void copyAssets() {
		Intent intent = new Intent(this, MinetestAssetCopy.class);
		startActivity(intent);
	}
	
	public void showDialog(String acceptButton, String hint, String current,
			int editType) {
		
		Intent intent = new Intent(this, MinetestTextEntry.class);
		Bundle params = new Bundle();
		params.putString("acceptButton", acceptButton);
		params.putString("hint", hint);
		params.putString("current", current);
		params.putInt("editType", editType);
		intent.putExtras(params);
		startActivityForResult(intent, 101);
		m_MessageReturnValue = "";
		m_MessagReturnCode   = -1;
	}
	
	public static native void putMessageBoxResult(String text);
	
	/* ugly code to workaround putMessageBoxResult not beeing found */
	public int getDialogState() {
		return m_MessagReturnCode;
	}
	
	public String getDialogValue() {
		m_MessagReturnCode = -1;
		return m_MessageReturnValue;
	}
	
	public float getDensity() {
		return getResources().getDisplayMetrics().density;
	}
	
	public int getDisplayWidth() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	public int getDisplayHeight() {
		return getResources().getDisplayMetrics().heightPixels;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == 101) {
			if (resultCode == RESULT_OK) {
				String text = data.getStringExtra("text"); 
				m_MessagReturnCode = 0;
				m_MessageReturnValue = text;
			}
			else {
				m_MessagReturnCode = 1;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	static {
		System.loadLibrary("openal");
		System.loadLibrary("ogg");
		System.loadLibrary("vorbis");
		System.loadLibrary("ssl");
		System.loadLibrary("crypto");
	}
	
	private int m_MessagReturnCode;
	private String m_MessageReturnValue;
}
