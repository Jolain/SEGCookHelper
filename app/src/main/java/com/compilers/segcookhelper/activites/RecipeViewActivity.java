package com.compilers.segcookhelper.activites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.compilers.segcookhelper.R;
import com.compilers.segcookhelper.cookhelper.CookHelper;
import com.compilers.segcookhelper.cookhelper.Recipe;

public class RecipeViewActivity extends Activity {
    private CookHelper app;

    private TextView recipeNameField;
    private TextView category;
    private TextView ingredient;
    private TextView cookTime;
    private TextView description;

    private Button editRecipe;
    private Button deleteRecipe;

    private Recipe tempRecipe;
    private Recipe recipeView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        recipeNameField = (TextView) findViewById(R.id.RecipeName);
        category = (TextView) findViewById(R.id.category);
        ingredient = (TextView) findViewById(R.id.ingredient);
        cookTime = (TextView) findViewById(R.id.cookTime);
        description = (TextView) findViewById(R.id.description);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("RecipeName");

        app = CookHelper.getInstance(getApplicationContext());

        // search in the database with message and recipe recipeView to return the recipe matching with message since the message
        // is the recipe name

        recipeView = app.getRecipe(message);

        recipeNameField.setText(message);
        category.setText("Category: " + recipeView.getCategoryName());
        ingredient.setText("Ingredient: " + recipeView.ingredientListToString());
        cookTime.setText("CookTime: " + recipeView.getCookTime());
        description.setText("Description: " + recipeView.getDescription());
    }

    public void onClickEditRecipe(View view) {
        Intent intent = new Intent(getApplicationContext(), EditRecipeActivity.class); //Application Context and Activity
        intent.putExtra("RecipeName", recipeNameField.getText().toString());
        startActivityForResult(intent, 0);
    }

    public void onClickDeleteRecipe(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete this recipe?");
        builder.setCancelable(true);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // TODO:delete the recipe from the database and return to research screen;
                app.removeRecipe(app.getRecipe(recipeNameField.getText().toString()));
                Intent intent = new Intent(); //Application Context and Activity
                intent.putExtra("RecipeName",recipeNameField.getText().toString());
                setResult(RESULT_OK, intent);
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

    @SuppressLint("SetTextI18n")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String message = bundle.getString("RecipeName");

            //research again a recipe in the database with message as parameter and say tempRecipe = getDataBaseRecipe(message)
            recipeNameField.setText(message);

            tempRecipe = app.getRecipe(message);
            category.setText("Category: " + tempRecipe.getCategoryName());
            ingredient.setText("Ingredient: " + tempRecipe.ingredientListToString());
            cookTime.setText("CookTime: " + tempRecipe.getCookTime());
            description.setText("Description: " + tempRecipe.getDescription());
        }
    }
}
