package in.mplay.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Timer;
import java.util.TimerTask;

import in.mplay.app.http.ApiClient;
import in.mplay.app.http.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Janny Samra on 11/3/2018.
 */

public class Online{
    private Context context;
    private Timer timer = new Timer();

   public Online(Context context){
       this.context=context;
       timer.scheduleAtFixedRate(new TimerTask() {
           @Override
           public void run() {
               ActiveOnline();
           }
       },0,60000);

   }





    @SuppressLint("HardwareIds")
    private void ActiveOnline() {
       String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call< String > result = service.getOnline("true", FirebaseInstanceId.getInstance().getToken(),deviceId);
        result.enqueue(new Callback< String >() {
            @Override
            public void onResponse(@NonNull Call < String > call, @NonNull Response< String > response) {
            }

            @Override
            public void onFailure(@NonNull Call < String > call, @NonNull Throwable t) {}

        });
    }

    }
