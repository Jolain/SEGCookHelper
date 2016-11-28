package com.compilers.segcookhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weierstrass on 2016-11-23.
 */

public class ContainerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_container);
        ListView listView = (ListView) findViewById(R.id.list_recipe);
        //TODO Retrieve objects created by the search query (not yet implemented)
        // nouvelle section pour tester le containter
        Pertinence pertinence = Pertinence.getPertinence();
        Recipe[] results = pertinence.getRecipes();

        /*
        Recipe r1,r2;
        Recipe[] results = {
                r1 = new Recipe("Macaroni","30 min", null, null, R.drawable.macaroni,"ok" ),
                r2 = new Recipe("NoRecipe","60 min", null, null, R.drawable.ic_logo_00, "ok")
        };
        */

        RecipeAdapter ad = new RecipeAdapter(this, results);
        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textView = (TextView) view.findViewById(R.id.RecipeName);
                String text = textView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), RecipeViewActivity.class); //Application Context and Activity
                // Need some way to give recipe to the intent
                intent.putExtra("RecipeName",text);
                startActivityForResult(intent,0);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK) {
            finish();
        }
    }





}
