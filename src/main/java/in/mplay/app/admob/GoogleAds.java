package in.mplay.app.admob;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.mplay.app.http.ApiClient;
import in.mplay.app.http.ApiInterface;
import in.mplay.app.utils.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoogleAds {
    private Context context;
    private Session session;


public GoogleAds(Context context){
    this.context= context;
    session = new Session(context);
    MobileAds.initialize(context, "ca-app-pub-1737152255173320~1913690939");
}


public void loadAds(){
    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
    Call < String > result = service.getAdmob("true");
    result.enqueue(new Callback< String >() {
        @Override
        public void onResponse(@NonNull Call < String > call, @NonNull Response< String > response) {

            if(response.body().equals("null")){
                session.setAds("null","null");
            }else{
                try {
                    JSONArray jsonarray = new JSONArray(response.body());
                    JSONObject jsonObject = jsonarray.getJSONObject(0);
                    session.setAds(jsonObject.getString("banner"),jsonObject.getString("interstitial"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call< String > call, @NonNull Throwable t) {}

    });
}


public void showAds(){
    if (!session.getInterstitialAds().equals("null")){
    final InterstitialAd mInterstitialAd = new InterstitialAd(context);
    mInterstitialAd.setAdUnitId(session.getInterstitialAds());
    AdRequest adRequestInter = new AdRequest.Builder().addTestDevice("B431EE858B5F1986E4D89CA31250F732").build();
    mInterstitialAd.setAdListener(new AdListener() {
        @Override
        public void onAdLoaded() {
            mInterstitialAd.show();
        }
    });
    mInterstitialAd.loadAd(adRequestInter);
}
}

    public void showBannerAds(View adContainer){
        if (!session.getBannerAds().equals("null")){
            AdView mAdView = new AdView(context);
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(session.getBannerAds());
            ((RelativeLayout)adContainer).addView(mAdView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("B431EE858B5F1986E4D89CA31250F732").build();
            mAdView.loadAd(adRequest);

        }
    }

}
