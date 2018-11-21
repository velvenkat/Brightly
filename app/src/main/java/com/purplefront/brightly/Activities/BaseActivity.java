package com.purplefront.brightly.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.purplefront.brightly.R;


public class BaseActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    alert_dlg_interface mListener;
    /**
     * @param context
     * @param activityClass  - target fragment
     * @param bundle
     * @param finish         - is this current activity can be finish
     * @param animationStyle - animation style : left to right...
     */

    /**
     * @param
     * @return check is fiels is empty  if empty request focus to that field
     */

    public void setDlgListener(alert_dlg_interface listener) {
        mListener = listener;
    }

    boolean isEmptyField(EditText editText) {
        boolean empty = TextUtils.isEmpty(getFieldText(editText));
        if (empty)
            editText.requestFocus();
        return empty;
    }

    public void showAlertDialog_ok(String message, String Title, String Pos_Title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(Title);

        // Setting Dialog Message
        //alertDialog.setMessage("You are about to delete the Set. All the information contained in the Sets will be lost. ");
        alertDialog.setMessage(message);
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.error);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(Pos_Title, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                mListener.postive_btn_clicked();
                //getDeleteSet();
                // Write your code here to invoke YES event
//                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button


        // Showing Alert Message
        alertDialog.show();
    }


    public void showAlertDialog(String message, String Title, String Pos_Title, String Neg_Title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(Title);

        // Setting Dialog Message
        //alertDialog.setMessage("You are about to delete the Set. All the information contained in the Sets will be lost. ");
        alertDialog.setMessage(message);
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.error);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(Pos_Title, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                mListener.postive_btn_clicked();
                //getDeleteSet();
                // Write your code here to invoke YES event
//                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(Neg_Title, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
//                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                mListener.negative_btn_clicked();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    String getFieldText(EditText editText) {
        return editText.getText().toString().trim();
    }

    // Intent Methods
    public void simpleIntent(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
    }

    public void stackClearIntent(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void finishIntent(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
        finish();

    }

    public void frwdAnimIntent(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
    }

    public void backAnimIntent(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }


    //Show Toast Methods
    public void showLongToast(Activity activity, String massage) {
        Toast.makeText(activity, massage, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(Activity activity, String massage) {
        Toast.makeText(activity, massage, Toast.LENGTH_LONG).show();
    }

    /**
     * show progress dialog for api calls
     */
    public void showProgress() {
        try {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * dismiss progress dialog after api calls
     */
    public void dismissProgress() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*Camelcase code*/

    public boolean isStringEmpty(String input) {
        Boolean valid = false;
        if (input == null) {
            valid = true;
        } else if (input.trim().length() == 0) {
            valid = true;
        }
        return valid;
    }

    public String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
   public String[] split_string(String charText) {
       String[] parts;
       parts = charText.split("\\s+");
        Log.d("Length-->", "" + parts.length);
        if (parts.length == 2)

        {
            String First = parts[0];
            String Last = parts[1];
        }
       return parts;
    }

    public String toCamelCase(String string) {
        String result = "";
        if (!isStringEmpty(string)) {
            String[] words = string.split("\\s");
            for (String s : words) {
                result = result + capitalize(s) + " ";
            }
        }
        return result;
    }

    public interface alert_dlg_interface {
        public void postive_btn_clicked();

        public void negative_btn_clicked();
    }
}
