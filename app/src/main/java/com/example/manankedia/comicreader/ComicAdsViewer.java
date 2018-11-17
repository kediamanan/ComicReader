package com.example.manankedia.comicreader;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adsnative.ads.ANAdListener;
import com.adsnative.ads.ANAdViewBinder;
import com.adsnative.ads.ANNativeAd;
import com.adsnative.ads.NativeAdUnit;
import com.bumptech.glide.Glide;

public class ComicAdsViewer extends AppCompatActivity {


    private ANNativeAd mNativeAd;

    final ANAdViewBinder anAdViewBinder = new
            ANAdViewBinder.Builder(R.layout.single_integration_ad)
            .bindTitle(R.id.single_integration_title)
            .bindSummary(R.id.single_integration_summary)
            .bindIconImage(R.id.single_integration_image1)
            .bindPromotedBy(R.id.single_integration_promoted_by)
            .build();
    private ConstraintLayout mContainer;
    private TextView mTextViewTitle;
    private TextView mTextViewDate;
    private ImageView mImageView;

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getIntExtra("comicsOpened",1)%5 == 0 && getIntent().getIntExtra("currentViewMode",0) == 0) {
            mNativeAd.loadAd();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getIntExtra("comicsOpened",1)%5 == 0 && getIntent().getIntExtra("currentViewMode",0) == 0) {
            mNativeAd.loadAd();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_comic_ads_viewer);
        String url = "";

        if(getIntent().hasExtra("comicUrl")){
            url = getIntent().getStringExtra("comicUrl");
        }

        mTextViewTitle = findViewById(R.id.title_fullscreen);
        mTextViewDate = findViewById(R.id.date_fullscreen);
        mImageView = findViewById(R.id.image);
        mTextViewTitle.setText(getIntent().getStringExtra("title"));
        mTextViewDate.setText(getIntent().getStringExtra("date"));
        mContainer = findViewById(R.id.single_integration_container);

        if(getIntent().getIntExtra("comicsOpened",1)%5 == 0 && getIntent().getIntExtra("currentViewMode",0) ==0) {

            mNativeAd = new ANNativeAd(this, "2Pwo1otj1C5T8y6Uuz9v-xbY1aB09x8rWKvsJ-HI");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    6.0f

            );
            mImageView.setLayoutParams(param);
            param = new LinearLayout.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    2.0f
            );
            mContainer.setLayoutParams(param);
            mNativeAd.registerViewBinder(anAdViewBinder);
            mNativeAd.setNativeAdListener(new ANAdListener() {
                @Override
                public void onAdLoaded(NativeAdUnit nativeAdUnit) {

                    View nativeAdView = mNativeAd.renderAdView(nativeAdUnit);

                    // the above method only creates the ad view, you need to add the view to your container as desired
                    mContainer.addView(nativeAdView);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                }

                @Override
                public void onAdFailed(String message) {
                }

                @Override
                public void onAdImpressionRecorded() {
                }

                @Override
                public boolean onAdClicked(NativeAdUnit nativeAdUnit) {
                    return false;
                }

            });

        }
        Glide.with(this).asBitmap().load(url).into((ImageView) mImageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getIntent().getStringExtra("alt"), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
