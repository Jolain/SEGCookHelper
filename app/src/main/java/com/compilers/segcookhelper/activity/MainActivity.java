package com.compilers.segcookhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.compilers.segcookhelper.R;
import com.compilers.segcookhelper.cookhelper.CookHelper;

public class MainActivity extends Activity {

    public Button search;
    public Button add;
    public Button help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (Button) findViewById(R.id.search1);
        help = (Button) findViewById(R.id.help);
        add = (Button) findViewById(R.id.add);

        //TODO initialise app
        CookHelper app = CookHelper.getInstance(getApplicationContext());
    }

    public void onClickSearch(View view) {
        Intent intent = new Intent(getApplicationContext(), ResearchActivity.class); //Application Context and Activity
        startActivityForResult(intent, 0);
    }

    public void onClickHelp(View view) {
        Intent intent = new Intent(getApplicationContext(), HelpActivity.class); //Application Context and Activity
        startActivityForResult(intent, 0);
    }

    public void onClickAddRecipe(View view) {
        Intent intent = new Intent(getApplicationContext(), AddRecipeActivity.class); //Application Context and Activity
        startActivityForResult(intent, 0);
    }
}
