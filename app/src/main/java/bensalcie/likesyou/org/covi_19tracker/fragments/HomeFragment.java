package bensalcie.likesyou.org.covi_19tracker.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import bensalcie.likesyou.org.covi_19tracker.AffectedCountries;
import bensalcie.likesyou.org.covi_19tracker.R;
import bensalcie.likesyou.org.covi_19tracker.models.Post;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {
    private static final int RC_SIGN_IN = 980;
    private View v;
    private TextView tvCases,tvRecovered,tvCritical,tvActive,tvTodaysCases,tvTotalDeaths,tvTodaysDeaths,tvAffectedCountries;
    private SimpleArcLoader simpleArcLoader;
    private PieChart pieChart;
    private Context ctx;
    private ImageButton btnHelp;
    private SliderLayout mDemoSlider;
private GoogleSignInOptions gso;
private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mPostsDatabase;
    private CardView sliderCard;
    private TextView tvInstructions;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_home, container, false);
        tvCases=v.findViewById(R.id.tvCases);
        btnHelp=v.findViewById(R.id.btnHelp);
        mDemoSlider = v.findViewById(R.id.slider);

        tvRecovered=v.findViewById(R.id.tvRecovered);
        tvCritical=v.findViewById(R.id.tvCritical);
        tvActive=v.findViewById(R.id.tvActive);
        tvTodaysCases=v.findViewById(R.id.tvTodayCases);
        tvTodaysDeaths=v.findViewById(R.id.tvTodayDeaths);
        tvTotalDeaths=v.findViewById(R.id.tvTotalDeaths);
        tvAffectedCountries=v.findViewById(R.id.tvAffectedCountries);
        pieChart=v.findViewById(R.id.piechart);
        simpleArcLoader=v.findViewById(R.id.SIM_loader);

        tvInstructions=v.findViewById(R.id.tvInstructions);
        sliderCard=v.findViewById(R.id.sliderCard);

        mPostsDatabase= FirebaseDatabase.getInstance().getReference().child("COVI-APP").child("Posts");



        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditsFragment hf=new CreditsFragment();
                FragmentTransaction ft=getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment,hf);
                ft.commit();
            }
        });
        sliderCard.setVisibility(View.GONE);

        fetchData();
        loadSlider();
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        return v;
    }

    private void loadSlider() {
        final ArrayList<String> listUrl = new ArrayList<>();
        final ArrayList<String> listName = new ArrayList<>();

        final List<Post> topPostList = new ArrayList<>();
        mPostsDatabase.limitToLast(5).orderByChild("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                topPostList.clear();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Post topPost = postSnapshot.getValue(Post.class);
                    topPostList.add(topPost);
                    // here you can access to name property like university.name
                }

                for (int i=0;i<topPostList.size();i++){
                    listUrl.add(topPostList.get(i).getPhoto());
                    listName.add(topPostList.get(i).getMessage());
                }

               loadViews(listUrl,listName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });







    }

    private void loadViews(ArrayList<String> listUrl, ArrayList<String> listName) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);


        listUrl.add("https://media1.s-nbcnews.com/j/newscms/2019_51/3151621/191217-hand-washing-stock-cs-906a_f4b26951f71188739ce7617acf4cb093.fit-760w.jpg");
        listName.add("Wash your hands frequently");
       if (listUrl.size()>0){
           for (int i = 0; i < listUrl.size(); i++) {
               TextSliderView sliderView= new TextSliderView(ctx);
               // if you want show image only / without description text use DefaultSliderView instead

               // initialize SliderLayout
               sliderView
                       .image(listUrl.get(i))
                       .description(listName.get(i))
                       .setRequestOption(requestOptions)
                       .setProgressBarVisible(true).
                       setOnSliderClickListener(this);

               //add your extra information
               sliderView.bundle(new Bundle());
               sliderView.getBundle().putString("extra", listName.get(i));
               mDemoSlider.addSlider(sliderView);

           }
           // set Slider Transition Animation
           // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);

           sliderCard.setVisibility(View.VISIBLE);
           mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

           mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
           mDemoSlider.setCustomAnimation(new DescriptionAnimation());
           mDemoSlider.setDuration(3000);
           mDemoSlider.addOnPageChangeListener(this);
           mDemoSlider.stopCyclingWhenTouch(false);
          FirebaseDatabase.getInstance().getReference().child("COVI-APP").child("HEADING").addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {

                   if (dataSnapshot.exists()){
                       String headig=dataSnapshot.getValue().toString();
                       tvInstructions.setText(headig);
                       tvInstructions.setVisibility(View.VISIBLE);
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });

       }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx=context;
    }

    private void fetchData() {
        String url="https://disease.sh/v2/all/";
        simpleArcLoader.start();
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Response: "+response);
                try {
                    DecimalFormat formatter=new DecimalFormat("#,###,###");
                    JSONObject jsonObject=new JSONObject(response);
                    String cases=jsonObject.getString("cases");
                    String recovered=jsonObject.getString("recovered");
                    String critical=jsonObject.getString("critical");
                    String active=jsonObject.getString("active");
                    String todaycases=jsonObject.getString("todayCases");
                    String todayDeaths=jsonObject.getString("todayDeaths");
                    String deaths=jsonObject.getString("deaths");
                    String affectedCountries=jsonObject.getString("affectedCountries");
                    tvCases.setText(formatter.format(Integer.parseInt(cases)));
                    tvRecovered.setText(formatter.format(Integer.parseInt(recovered)));
                    tvCritical.setText(formatter.format(Integer.parseInt(critical)));
                    tvActive.setText(formatter.format(Integer.parseInt(active)));
                    tvTodaysCases.setText(formatter.format(Integer.parseInt(todaycases)));
                    tvTodaysDeaths.setText(formatter.format(Integer.parseInt(todayDeaths)));
                    tvTotalDeaths.setText(formatter.format(Integer.parseInt(deaths)));
                    tvAffectedCountries.setText(formatter.format(Integer.parseInt(affectedCountries)));

                    pieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(cases), Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(recovered), Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(deaths), Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(active), Color.parseColor("#29B6F6")));

                    pieChart.startAnimation();
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                    //scrollView.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                    //scrollView.setVisibility(View.VISIBLE);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                //scrollView.setVisibility(View.VISIBLE);
                Toast.makeText(ctx, "Something unusual happened", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(ctx);
        requestQueue.add(request);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if (account==null){
            bringPopup(slider.getView());
        }else {
            moveToChats(account);

        }
    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onStop() {
        super.onStop();
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    private void bringPopup(View v) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.bottom_sheet, null);

        ImageButton btnClose=popupView.findViewById(R.id.btnClose);
        TextView tvPolicies=popupView.findViewById(R.id.tvPolicies);
        String content="By logging in you accept to terms and conditions available here: \nhttp://bensalcie.likesyou.org/cwc/privacy_policy.html\n";


        tvPolicies.setText(content);
        Linkify.addLinks(tvPolicies,Linkify.ALL);

        SignInButton signInButton = popupView.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

       // MedicalProfessional mp=new MedicalProfessional(getContext(),popupView,popupWindow);
       // mp.add_medical_professional();
        // dismiss the popup window when touched


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // create the popup window

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            moveToChats(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLE", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void moveToChats(GoogleSignInAccount account) {

        if (account != null) {
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            String personPhoto="default";
            if (account.getPhotoUrl()!=null){

                personPhoto = account.getPhotoUrl().toString();
            }

            startActivity(new Intent(getContext(), AffectedCountries.class)
                    .putExtra("email",personEmail)
                    .putExtra("id",personId)
                    .putExtra("photo",personPhoto)
                    .putExtra("name",personName));

        }
    }

}
