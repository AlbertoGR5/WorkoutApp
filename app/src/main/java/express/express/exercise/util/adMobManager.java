package express.express.exercise.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.workout.exercise.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class adMobManager {

    InterstitialAd mInterstitialAd;
    public static int countBackpress = 0;
    int counter;
    public static AdView adView;

    public void LoadBannerAdd(Activity activity, View view) {

        if (view != null) {
            adView = view.findViewById(R.id.adView);
        } else {
            adView = activity.findViewById(R.id.adView);
        }
        if (activity.getResources().getString(R.string.ads_visibility).equals("yes")) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

    }

    public void LoadInterstitialAd(final Activity activity) {

        if (activity.getResources().getString(R.string.ads_visibility).equals("yes")) {
            mInterstitialAd = new InterstitialAd(activity);
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            mInterstitialAd.setAdUnitId(activity.getString(R.string.interstitial_id));
            mInterstitialAd.loadAd(adRequestBuilder.build());

            mInterstitialAd.setAdListener((new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    activity.finish();
//                    if (AdManager.INSTANCE != null) {
//                        loadAd();
//                    }
                }
            }));
        }
    }

    public void showInterstitialAd(Activity activity) {
        if (activity.getResources().getString(R.string.ads_visibility).equals("yes")) {
            this.mInterstitialAd.show();
        }

    }

    public void onbackPress(Activity activity) {
//        int counter=utilhelper.getAdCounter(activity);

        countBackpress++;
        counter = Integer.parseInt(activity.getResources().getString(R.string.ads_counter));

        if (counter == countBackpress) {
            showInterstitialAd(activity);
            countBackpress = 0;
        }
        Log.e("Counter", String.valueOf(counter));
    }
}
