package bensalcie.likesyou.org.covi_19tracker.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bensalcie.likesyou.org.covi_19tracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditsFragment extends Fragment {
private View v;

    public CreditsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_credits, container, false);
        TextView noteView = (TextView)v. findViewById(R.id.noteview);
        TextView sourseView = (TextView)v. findViewById(R.id.sourceView);
        //android:text="@string/data_source"

        sourseView.setText(R.string.data_source);
        String someContent="Connect to me via:\n1:https://twitter.com/ibensalcie \n2:http://github.com/bensalcie \n3:http://linkedin.com/in/bensalcie"+
                "\n\n\nOr Become my Patreon :\nhttps://patreon.com/bensalcie";
        noteView.setText(someContent);
        Linkify.addLinks(sourseView,Linkify.ALL);
        Linkify.addLinks(noteView, Linkify.ALL);

        return v;
    }

}
