package com.purplefront.brightly.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.Modules.SharedDataModel;
import com.purplefront.brightly.R;

import java.util.ArrayList;

public class SharePage extends BaseActivity {

    Button share_inApp;
    Button share_aLink;

    String set_description = "";
    String set_name = "";
    String userId;
    String set_id = "";
    String share_link;
    ChannelListModel chl_list_obj;
    SetsListModel setsListModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Share Set");
        chl_list_obj=getIntent().getParcelableExtra("model_obj");
        setsListModel=getIntent().getParcelableExtra("setsListModel");
        set_description = setsListModel.getDescription();
        set_name = setsListModel.getSet_name();
        set_id = setsListModel.getSet_id();
        share_link = setsListModel.getShare_link();
        userId = getIntent().getStringExtra("userId");

        share_inApp = (Button) findViewById(R.id.share_inApp);
        share_aLink = (Button) findViewById(R.id.share_aLink);

        share_inApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SharePage.this, ShareWithContacts.class);
                intent.putExtra("userId", userId);
                intent.putExtra("model_obj", chl_list_obj);
                intent.putExtra("setsListModel", setsListModel);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });

        share_aLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Brightly Set Share link");
                share.putExtra(Intent.EXTRA_TEXT, share_link);

                startActivity(Intent.createChooser(share, "Share link!"));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }
}
