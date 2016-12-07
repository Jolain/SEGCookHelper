package com.compilers.segcookhelper.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.TypedArrayUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.compilers.segcookhelper.R;
import com.compilers.segcookhelper.cookhelper.CookHelper;
import com.compilers.segcookhelper.cookhelper.Recipe;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Weierstrass on 2016-11-23.
 */

public class ContainerActivity extends Activity {
    private CookHelper app;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_container);
        listView = (ListView) findViewById(R.id.list_recipe);

        app = CookHelper.getInstance(getApplicationContext());
        Recipe[] results = app.getSortedRecipes();
        if (results == null || results.length == 0) {// regarde s'il n'y a aucun résultat, si le cas échant, affiche un message à l'utilisateur disant qu'aucune recette n'a été trouvé

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

        RecipeAdapter ad = new RecipeAdapter(this, results); // crée un recipe adapteur pour afficher les recettes selon un layout voulu
        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { // démarre l'activité researchActivity si tu cliques sur une recette dans la liste

                TextView textView = (TextView) view.findViewById(R.id.RecipeName);
                String text = textView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), RecipeViewActivity.class); //Application Context and Activity

                intent.putExtra("RecipeName", text);
                startActivityForResult(intent, 0);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) { // update la listView si et seulement si une recette a été supprimé
            String recipeDelete = data.getStringExtra("RecipeName");
            Recipe[] results = app.removeElements(app.getSortedRecipes(),recipeDelete);
            ListView listView1 = (ListView) findViewById(R.id.list_recipe);


            RecipeAdapter ad = new RecipeAdapter(this, results);
            listView1.setAdapter(ad);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    TextView textView = (TextView) view.findViewById(R.id.RecipeName);
                    String text = textView.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), RecipeViewActivity.class); //Application Context and Activity

                    intent.putExtra("RecipeName",text);
                    startActivityForResult(intent,0);

                }
            });}

    }
}
