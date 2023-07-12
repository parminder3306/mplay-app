package in.mplay.app.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class Session {
	private SharedPreferences sharedPreferences;
	private Context context;

	public Session(Context context){
		this.context = context;
		sharedPreferences = context.getSharedPreferences("megaplay", Context.MODE_PRIVATE);
	}

	public void setCategory(String cat){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("selectcat", cat);
		editor.apply();
	}

	public void setAds(String banner,String interstitial){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("banner", banner);
		editor.putString("interstitial", interstitial);
		editor.apply();
	}

	public String getmyCat(){
		return sharedPreferences.getString("selectcat", null);
	}

	public String getBannerAds(){
		return sharedPreferences.getString("banner", null);
	}
	public String getInterstitialAds(){
		return sharedPreferences.getString("interstitial", null);
	}

	}
