package bensalcie.likesyou.org.covi_19tracker.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bensalcie.likesyou.org.covi_19tracker.AffectedCountries;
import bensalcie.likesyou.org.covi_19tracker.CountryModel;
import bensalcie.likesyou.org.covi_19tracker.DetailsActivity;
import bensalcie.likesyou.org.covi_19tracker.R;
import bensalcie.likesyou.org.covi_19tracker.adapters.MyCustomAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountriesFragment extends Fragment {
    private SearchView searchView;
    private ListView listView;
    private SimpleArcLoader simpleArcLoader;
    public static List<CountryModel> countryModelList=new ArrayList<>();
    CountryModel countryModel;
    MyCustomAdapter myCustomAdapter;
    private View v;
    private Context ctx;

    public CountriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_countries, container, false);

        searchView=v.findViewById(R.id.searchView);
        listView=v.findViewById(R.id.listView);
        simpleArcLoader=v.findViewById(R.id.loader);



        fetchData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ctx, DetailsActivity.class).putExtra("position",position));

            }
        });



        return v;
    }


    /*private void loadInterstitial() {
        MobileAds.initialize(ctx, "ca-app-pub-2540463123963261~4186538002");
        final InterstitialAd interstitialAd = new InterstitialAd(ctx);
        interstitialAd.setAdUnitId("ca-app-pub-2540463123963261/1819712199");
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);
        interstitialAd.setAdListener(new AdListener(){
            public void onAdLoaded(){
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });

    }*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx=context;


    }


    private void fetchData() {
        String url="https://disease.sh/v2/countries/";
        simpleArcLoader.start();
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Response: "+response);
                countryModelList.clear();

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {

                        //flag,country,cases,todayCases,deaths,todayDeaths,recovered,active,critical,tests
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String countryName=jsonObject.getString("country");
                        String cases=jsonObject.getString("cases");
                        String todayCases=jsonObject.getString("todayCases");
                        String deaths=jsonObject.getString("deaths");
                        String todayDeaths=jsonObject.getString("todayDeaths");
                        String recovered=jsonObject.getString("recovered");
                        String active=jsonObject.getString("active");
                        String critical=jsonObject.getString("critical");
                        String tests=jsonObject.getString("tests");


                        JSONObject object=jsonObject.getJSONObject("countryInfo");
                        //lat,long
                        String flag=object.getString("flag");
                        countryModel=new CountryModel(flag,countryName,cases,todayCases,deaths,todayDeaths,recovered,active,critical,tests);
                        countryModelList.add(countryModel);
                    }

                    myCustomAdapter=new MyCustomAdapter(ctx,countryModelList);
                    listView.setAdapter(myCustomAdapter);
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {



                            myCustomAdapter.getFilter().filter(newText);
                            myCustomAdapter.notifyDataSetChanged();


                            return false;
                        }
                    });

                    //loadInterstitial();

                } catch (JSONException e) {
                    e.printStackTrace();
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                Toast.makeText(ctx, "Something unusual happened", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(ctx);
        requestQueue.add(request);
    }



}
