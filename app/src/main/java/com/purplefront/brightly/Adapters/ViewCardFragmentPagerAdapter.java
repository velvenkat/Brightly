package com.purplefront.brightly.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.purplefront.brightly.Fragments.Multimedia_CardFragment;
import com.purplefront.brightly.Fragments.ItemsAddFragment;
import com.purplefront.brightly.Modules.CardsListModel;

import java.util.List;

public class ViewCardFragmentPagerAdapter extends FragmentStatePagerAdapter
{
    // Declare Variables

    List<CardsListModel> cardsListModels;
    String set_id;
    String userId;
    String set_name;
    Context glob_con;

    public ViewCardFragmentPagerAdapter(Context context, FragmentManager fm, List<CardsListModel> cardsListModels, String set_id, String userId, String set_name) {
        super(fm);
        this.cardsListModels = cardsListModels;
        this.set_id = set_id;
        this.userId = userId;
        this.set_name = set_name;
        glob_con=context;

    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((Fragment) object).getView();

    }
    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment card_frag = null;
        if(position<cardsListModels.size()) {
            CardsListModel cardObj = cardsListModels.get(position);

            Bundle bundle = new Bundle();
            bundle.putParcelable("card_mdl_obj", cardObj);


                card_frag = new Multimedia_CardFragment();

            bundle.putString("set_id", set_id);
            bundle.putString("userId", userId);
            bundle.putString("set_name", set_name);
            card_frag.setArguments(bundle);


        }
       /* else{
            card_frag = new ItemsAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("set_id", set_id);
            bundle.putString("userId", userId);
            bundle.putString("set_name", set_name);
            card_frag.setArguments(bundle);
            return card_frag;
        }*/
        return card_frag;
    }

   /* @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
       ((ViewPager) container).removeView(((Fragment) object).getView());
        Toast.makeText(glob_con,"On Destroy",Toast.LENGTH_LONG).show();
    }*/

    @Override
    public int getCount() {
        return cardsListModels.size();
    }
}
