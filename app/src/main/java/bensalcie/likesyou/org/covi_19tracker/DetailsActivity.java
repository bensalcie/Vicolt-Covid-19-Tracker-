package bensalcie.likesyou.org.covi_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;

import bensalcie.likesyou.org.covi_19tracker.fragments.CountriesFragment;

public class DetailsActivity extends AppCompatActivity {
    private int positionCountry;

    private TextView tvCases,tvTests,tvRecovered,tvCritical,tvActive,tvTodaysCases,tvTotalDeaths,tvTodaysDeaths,tvCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent=getIntent();
        positionCountry= intent.getIntExtra("position",0);
        getSupportActionBar().setTitle(""+CountriesFragment.countryModelList.get(positionCountry).getCountry());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCases=findViewById(R.id.tvCases);
        tvRecovered=findViewById(R.id.tvRecovered);
        tvCritical=findViewById(R.id.tvCritical);
        tvActive=findViewById(R.id.tvActive);
        tvTodaysCases=findViewById(R.id.tvTodayCases);
        tvTodaysDeaths=findViewById(R.id.tvTodayDeaths);
        tvTotalDeaths=findViewById(R.id.tvTotalDeaths);
        tvCountry=findViewById(R.id.tvAffectedCountries);
        tvTests=findViewById(R.id.tvTests);
        DecimalFormat formatter=new DecimalFormat("#,###,###");

    tvCountry.setText(CountriesFragment.countryModelList.get(positionCountry).getCountry());
    tvCases.setText(formatter.format(Integer.parseInt(CountriesFragment.countryModelList.get(positionCountry).getCases())));
    tvRecovered.setText(formatter.format(Integer.parseInt(CountriesFragment.countryModelList.get(positionCountry).getRecovered())));
    tvCritical.setText(formatter.format(Integer.parseInt(CountriesFragment.countryModelList.get(positionCountry).getCritical())));
    tvActive.setText(formatter.format(Integer.parseInt(CountriesFragment.countryModelList.get(positionCountry).getActive())));
    tvTodaysCases.setText(formatter.format(Integer.parseInt(CountriesFragment.countryModelList.get(positionCountry).getTodayCases())));
    tvTodaysDeaths.setText(formatter.format(Integer.parseInt(CountriesFragment.countryModelList.get(positionCountry).getTodayDeaths())));
    tvTotalDeaths.setText(formatter.format(Integer.parseInt(CountriesFragment.countryModelList.get(positionCountry).getDeaths())));
    tvTests.setText(formatter.format(Integer.parseInt(CountriesFragment.countryModelList.get(positionCountry).getTests())));
//loadAd();
    }
   /* private void loadAd() {
        MobileAds.initialize(DetailsActivity.this, "ca-app-pub-2540463123963261/7895710549");
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
    }*/
}
