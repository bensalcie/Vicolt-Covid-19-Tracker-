package bensalcie.likesyou.org.covi_19tracker.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.leo.simplearcloader.SimpleArcLoader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import bensalcie.likesyou.org.covi_19tracker.R;
import bensalcie.likesyou.org.covi_19tracker.preferences.MyPreferenceClass;
import me.itangqi.waveloadingview.WaveLoadingView;
import mehdi.sakout.fancybuttons.FancyButton;
import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourCountryFragment extends Fragment {
private View root;
private Context context;
private RelativeLayout myCountryHolder;
private GifImageView loadingView;


    public YourCountryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_your_country, container, false);
        myCountryHolder=root.findViewById(R.id.myCountryHolder);
        myCountryHolder.setVisibility(View.GONE);
        loadingView=root.findViewById(R.id.loadingView);

        TextView tvCountry =root.findViewById(R.id.tvCountry);
        MyPreferenceClass mpc=new MyPreferenceClass(context);

        String co= mpc.getData("country");
        if (!TextUtils.isEmpty(co)) {
            tvCountry.setText(co+" Stats");
            fetchData(co);
        }

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    private void fetchData(final String co) {
        loadingView.setVisibility(View.VISIBLE);
        String url="https://disease.sh/v2/countries/"+co;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Response: "+response);
                try {


                    JSONObject jsonObject=new JSONObject(response);
                    String cases=jsonObject.getString("cases");
                    String recovered=jsonObject.getString("recovered");
                    String critical=jsonObject.getString("critical");
                    String active=jsonObject.getString("active");
                    String todaycases=jsonObject.getString("todayCases");
                    String todayDeaths=jsonObject.getString("todayDeaths");
                    String deaths=jsonObject.getString("deaths");
                    String tests=jsonObject.getString("tests");
                    String testsperOneM=jsonObject.getString("testsPerOneMillion");

                    JSONObject object=jsonObject.getJSONObject("countryInfo");
                    //lat,long
                    String flag=object.getString("flag");

                    loadingView.setVisibility(View.GONE);
                    myCountryHolder.setVisibility(View.VISIBLE);

                    //Cases
                    ImageView ivFlag=root.findViewById(R.id.ivFlag);
                    FancyButton todayCases,todayD;

                     TextView tvTemp,tvHum,tvRecovered,tvCritical,tvActive,tvTests,tvTpm;
                     WaveLoadingView wlv=root.findViewById(R.id.waveLoadingView);
                   tvHum=root.findViewById(R.id.tvHum);
                    tvTemp=root.findViewById(R.id.tvTemp);
                   todayD=root.findViewById(R.id.todatDeaths);
                    todayCases=root.findViewById(R.id.todatCases);
                     tvTpm=root.findViewById(R.id.tvTpm);
                    tvRecovered=root.findViewById(R.id.tvRecovered);
                    tvCritical=root.findViewById(R.id.tvCritical);
                    tvActive=root.findViewById(R.id.tvActive);
                    tvTests=root.findViewById(R.id.tvTests);
                    DecimalFormat formatter=new DecimalFormat("#,###,###");

                    tvRecovered.setText(String.format("%s", formatter.format(Integer.parseInt(recovered))));
                    tvCritical.setText(String.format("%s", formatter.format(Integer.parseInt(critical))));
                    tvActive.setText(String.format("%s", formatter.format(Integer.parseInt(active))));
                    tvTests.setText(String.format("%s", formatter.format(Integer.parseInt(tests))));
                    tvTpm.setText(String.format("%s", formatter.format(Integer.parseInt(testsperOneM))));


                    Picasso.get().load(flag).into(ivFlag);
                    tvTemp.setText(" "+formatter.format(Integer.parseInt(cases)));
                    todayCases.setText(""+todaycases);
                    tvHum.setText(""+formatter.format(Integer.parseInt(deaths)));
                    todayD.setText(""+formatter.format(Integer.parseInt(todayDeaths)));

                    int rec=Integer.parseInt(recovered);
                    int cas=Integer.parseInt(cases);
                    double perc=(rec*100)/cas;

                    wlv.setProgressValue((int) perc);

                    wlv.startAnimation();
                    //loadAd();





                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "An error Occurred", Toast.LENGTH_SHORT).show();
            }

        });

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        //this.context=context;
    }

   /* private void loadAd() {
        MobileAds.initialize(context, "ca-app-pub-2540463123963261/6421819107");
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
    }*/

}
