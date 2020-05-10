package bensalcie.likesyou.org.covi_19tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bensalcie.likesyou.org.covi_19tracker.CountryModel;
import bensalcie.likesyou.org.covi_19tracker.R;
import bensalcie.likesyou.org.covi_19tracker.fragments.CountriesFragment;

public class MyCustomAdapter extends ArrayAdapter<CountryModel> {

    private Context context;
    private List<CountryModel> countryModelList;
    private List<CountryModel> countryModelListFiltered;


    public MyCustomAdapter(@NonNull Context context, List<CountryModel> countryModelList) {

        super(context, R.layout.list_custom_item,countryModelList);
        this.context=context;
        this.countryModelList=countryModelList;
        this.countryModelListFiltered=countryModelList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom_item,null,true);
        TextView tvCountry=view.findViewById(R.id.tvCountry);
        ImageView imageFlag=view.findViewById(R.id.imageFlah);
        tvCountry.setText(countryModelListFiltered.get(position).getCountry());
        Picasso.get().load(countryModelListFiltered.get(position).getFlag()).into(imageFlag);


        return view;
    }

    @Override
    public int getCount() {
        return countryModelListFiltered.size();
    }

    @Nullable
    @Override
    public CountryModel getItem(int position) {
        return countryModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if (constraint==null || constraint.length()==0){
                    filterResults.count=countryModelList.size();
                    filterResults.values=countryModelList;

                }else {
                    List<CountryModel> resultsModel=new ArrayList<>();
                    resultsModel.clear();

                    String searchStr=constraint.toString().toLowerCase();
                    for (CountryModel itemsModel: countryModelList){
                        if (itemsModel.getCountry().toLowerCase().contains(searchStr))
                        {
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count=resultsModel.size();
                        filterResults.values=resultsModel;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countryModelListFiltered=(List<CountryModel>)results.values;
                CountriesFragment.countryModelList=(List<CountryModel>)results.values;
                notifyDataSetChanged();

            }
        };
        return filter;

    }
}
