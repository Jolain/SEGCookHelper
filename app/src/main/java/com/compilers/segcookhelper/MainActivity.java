package com.compilers.segcookhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public Button search;
    public Button add;
    public Button help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search.findViewById(R.id.research);
        add.findViewById(R.id.add);
        help.findViewById(R.id.help);

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
