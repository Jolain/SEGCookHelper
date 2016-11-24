package com.compilers.segcookhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResearchActivity extends Activity {
    private Button search;
    private Button clear;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        search.findViewById(R.id.searchButton);
        clear.findViewById(R.id.clearButton);
        edit.findViewById(R.id.searchQuery);

    }

    public void onClickSearchRecipe(View view) {
        Intent intent = new Intent(getApplicationContext(), ContainerActivity.class); //Application Context and Activity
        startActivityForResult(intent, 0);
    }

    public void onClickReset(View view){
        edit.setText(" ");

    }
}
