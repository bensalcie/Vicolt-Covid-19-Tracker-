package bensalcie.likesyou.org.covi_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import bensalcie.likesyou.org.covi_19tracker.fragments.CountriesFragment;
import bensalcie.likesyou.org.covi_19tracker.fragments.HomeFragment;
import bensalcie.likesyou.org.covi_19tracker.fragments.YourCountryFragment;
import bensalcie.likesyou.org.covi_19tracker.preferences.MyPreferenceClass;


public class MainActivity extends AppCompatActivity {
    private LinearLayout menu_one,menu_two,menu_three;
    private TextView tvInsights,tvAnalysys,tvControls;
    private String co="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        menu_one=findViewById(R.id.menu_one);
        menu_two=findViewById(R.id.menu_two);
        menu_three=findViewById(R.id.menu_three);
        tvAnalysys=findViewById(R.id.tvAnalysis);
        tvInsights=findViewById(R.id.tvInsights);
        tvControls=findViewById(R.id.tvControls);
        menu_one.setBackgroundColor(getResources().getColor(R.color.colorHightlighted));
        tvAnalysys.setTextColor(getResources().getColor(R.color.colorWhite));
        HomeFragment hf=new HomeFragment();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,hf);
        ft.commit();
        menu_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_one.setBackgroundColor(getResources().getColor(R.color.colorHightlighted));
                tvAnalysys.setTextColor(getResources().getColor(R.color.colorWhite));
                settupInit(menu_two,menu_three);
                HomeFragment hf=new HomeFragment();
                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment,hf);
                ft.commit();



            }
        });
        menu_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_two.setBackgroundColor(getResources().getColor(R.color.colorHightlighted));
                tvControls.setTextColor(getResources().getColor(R.color.colorWhite));
                settupInit(menu_one,menu_three);
                YourCountryFragment hf=new YourCountryFragment();
                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment,hf);
                ft.commit();


            }
        });
        menu_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_three.setBackgroundColor(getResources().getColor(R.color.colorHightlighted));
                tvInsights.setTextColor(getResources().getColor(R.color.colorWhite));
                settupInit(menu_one,menu_two);
                CountriesFragment hf=new CountriesFragment();
                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment,hf);
                ft.commit();

               /* NotificationsFragment hf=new NotificationsFragment();
                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment,hf);
                ft.commit();*/


            }
        });



    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            assert tm != null;
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry;
                }
            }
        }
        catch (Exception e) { }
        return null;
    }
    private void settupInit(LinearLayout menu_o, LinearLayout menu_t) {
        menu_o.setBackgroundColor(0);
        menu_t.setBackgroundColor(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyPreferenceClass mpc=new MyPreferenceClass(MainActivity.this);
        co= mpc.getData("country");
       if (TextUtils.isEmpty(co)){
           Locale loc = new Locale("",getUserCountry(this)); loc.getDisplayCountry();
           String countryName=loc.getDisplayCountry();
           mpc.saveData("country",countryName);
       }
    }
}
