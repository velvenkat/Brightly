package com.purplefront.brightly.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purplefront.brightly.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileType extends Fragment {


    public FileType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_type, container, false);
    }

}
