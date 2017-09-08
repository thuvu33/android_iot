package com.example.androidhive;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class InboxDetailActivity extends ActionBarActivity {
    private static final String TAG_MESSAGES = "messages";
    private static final String TAG_ID = "id";
    private static final String TAG_FROM = "from";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_SUBJECT = "subject";
    private static final String TAG_DATE = "date";

    private TextView view_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_detail);

        view_detail = (TextView) findViewById(R.id.textViewDetail);
        String get_subject = getIntent().getStringExtra(TAG_SUBJECT);
        view_detail.setText(get_subject);
    }
}
