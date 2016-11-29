package com.compilers.segcookhelper;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecipeViewActivity extends Activity {
    private TextView recipename;
    private TextView category;
    private TextView ingredient;
    private TextView cookTime;
    private TextView description;
    Recipe tempRecipe;
    private Button editRecipe;
    private Button deleteRecipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        recipename = (TextView)findViewById(R.id.RecipeName);
        category = (TextView) findViewById(R.id.category);
        ingredient = (TextView)findViewById(R.id.ingredient);
        cookTime = (TextView) findViewById(R.id.cookTime);
        description = (TextView)findViewById(R.id.description);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("RecipeName");
        Recipe recipeView;

        // search in the database with message and recipe recipeView to return the recipe matching with message since the message
        // is the recipe name

        //TODO set category, Ingredients, Description
        //TODO attributes need names in front eg: [Ingredients: Onions Tomato etc...]

        Database db = Database.getInstance(null);
        recipeView = db.getRecipe(message);

        recipename.setText(message);
        category.setText("Category: " + recipeView.getCategory().getName().toString());
        ingredient.setText("Ingredient: "+recipeView.ingredientListToString().toString());
        cookTime.setText("CookTime: " + recipeView.getCookTime());
        description.setText("Description: " + recipeView.getDescription());
    }

    public void onClickEditRecipe(View view){
        Intent intent = new Intent(getApplicationContext(), EditRecipeActivity.class); //Application Context and Activity
        intent.putExtra("RecipeName",recipename.getText().toString());
        startActivityForResult(intent, 0);
    }

    public void onClickDeleteRecipe(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete this recipe?");
        builder.setCancelable(true);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // delete the recipe from the database and return to research screen;
                Intent intent = new Intent(getApplicationContext(), ResearchActivity.class); //Application Context and Activity
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String message = bundle.getString("RecipeName");

            //research again a recipe in the database with message as parameter and say tempRecipe = getDataBaseRecipe(message)
            recipename.setText(message);
            category.setText("Category: " + tempRecipe.getCategory().getName().toString());
            ingredient.setText("Ingredient: "+tempRecipe.ingredientListToString().toString());
            cookTime.setText("CookTime: " + tempRecipe.getCookTime());
            description.setText("Description: " + tempRecipe.getDescription());
        }
    }
}
