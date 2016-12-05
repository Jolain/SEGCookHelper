package com.compilers.segcookhelper.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.compilers.segcookhelper.R;
import com.compilers.segcookhelper.cookhelper.Pertinence;
import com.compilers.segcookhelper.cookhelper.Recipe;

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
        Recipe[] results = pertinence.getRecipeArray();

        for (int i = 0; i < results.length; i++) {
            if (results[i] == null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sorry, no results were found");
                builder.setCancelable(false);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:delete the recipe from the database and return to research screen;
                        Intent intent = new Intent(getApplicationContext(), ResearchActivity.class); //Application Context and Activity
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        RecipeAdapter ad = new RecipeAdapter(this, results);
        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textView = (TextView) view.findViewById(R.id.RecipeName);
                String text = textView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), RecipeViewActivity.class); //Application Context and Activity
                // Need some way to give recipe to the intent
                intent.putExtra("RecipeName", text);
                startActivityForResult(intent, 0);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
