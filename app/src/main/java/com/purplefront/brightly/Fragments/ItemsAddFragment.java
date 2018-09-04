package com.purplefront.brightly.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.Activities.CreateCards;
import com.purplefront.brightly.R;

public class ItemsAddFragment extends Fragment {
    View rootView;
    String user_id,set_id,set_name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.items_add,container,false);
        ImageView add_cardImage;

        add_cardImage = (ImageView) rootView.findViewById(R.id.add_cardImage);
        Bundle bundle=getArguments();
        user_id = bundle.getString("userId");
        set_id = bundle.getString("set_id");
        set_name = bundle.getString("set_name");
        Glide.with(getContext())
                .load(R.drawable.add_image_black)
                .centerCrop()
                /*.transform(new CircleTransform(HomeActivity.this))
                .override(50, 50)*/
                .into(add_cardImage);

        add_cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), CreateCards.class);
                intent.putExtra("userId", user_id);
                intent.putExtra("set_id", set_id);
                intent.putExtra("set_name", set_name);

                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });


        return rootView;
    }
}
