package com.compilers.segcookhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecipeViewActivity extends Activity {

    private TextView category;
    private TextView ingredient;
    private TextView cookTime;
    private TextView description;
    private Button editRecipe;
    private Button deleteRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        category = (TextView) findViewById(R.id.category);
        ingredient = (TextView)findViewById(R.id.ingredient);
        cookTime = (TextView) findViewById(R.id.cookTime);
        description = (TextView)findViewById(R.id.description);

    }

    public void onClickEditRecipe(View view){
        Intent intent = new Intent(getApplicationContext(), EditRecipeActivity.class); //Application Context and Activity
        startActivityForResult(intent, 0);
    }

    public void onClickDeleteRecipe(View view){
        // delete the recipe from the database and return to research screen;
        Intent intent = new Intent(getApplicationContext(), ResearchActivity.class);
        startActivityForResult(intent,0);

    }
}
