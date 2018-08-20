package com.purplefront.brightly.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.purplefront.brightly.Activities.BaseActivity;
import com.purplefront.brightly.R;

public class BaseFragment extends Fragment {


    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * @param fragment fragment transaction for all fragments
     */
    protected void fragmentTransaction(BaseFragment fragment) {
        fragmentTransaction(fragment, false);
    }

    /**
     * @param fragment fragment transaction for all fragments
     */
    protected void fragmentTransaction(BaseFragment fragment, boolean allowStateLoss) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
        fragmentTransaction.replace(R.id.frameContainer, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
//    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (allowStateLoss) {
            fragmentTransaction.commitAllowingStateLoss();
        } else
            fragmentTransaction.commit();
    }

    // Intent Methods
    public void simpleIntent(Activity activity, Class activityClass)
    {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
    }
    public void stackClearIntent(Activity activity, Class activityClass)
    {
        Intent intent = new Intent(activity, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void finishIntent(Activity activity, Class activityClass)
    {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
        activity.finish();

    }

    public void frwdAnimIntent(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
    }

    public void backAnimIntent(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    //Show Toast Methods
    public void showLongToast(Activity activity, String massage )
    {
        Toast.makeText( activity, massage, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(Activity activity, String massage )
    {
        Toast.makeText( activity, massage, Toast.LENGTH_LONG).show();
    }


    /**
     * show progress dialog while api calls
     */
    protected void showProgress() {
        try {
            ((BaseActivity) getActivity()).showProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dissmiss progresss dialog for api calls
     */
    protected void dismissProgress() {
        try {
            ((BaseActivity) getActivity()).dismissProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

