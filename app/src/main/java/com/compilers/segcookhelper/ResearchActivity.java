package com.compilers.segcookhelper;
import java.util.LinkedList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ResearchActivity extends Activity {
    private Button search;
    private Button clear;
    private Button help;
    private EditText edit;
    private Spinner dropdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        clear = (Button)findViewById(R.id.clearButton);
        search = (Button)findViewById(R.id.searchButton);
        edit = (EditText)findViewById(R.id.searchQuery);
        help = (Button)findViewById(R.id.HelpResearch);
        dropdown = (Spinner)findViewById(R.id.catSpinner);
        String[] items = new String[]{" ", "chinese", "breakfast", "italian", "dinner", "collation", "cookies", "drink"}; // this is only to help me
        // Category[] category = datebase.category; ???
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });

    }

    public void onClickSearchRecipe(View view) {
    /*
        String query = edit.getText().toString();
        Database dbHelper = Database.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //TODO: Add search implementation
     */
        //String query = edit.getText().toString();
        //this.testSearch();

        Intent intent = new Intent(getApplicationContext(), ContainerActivity.class); //Application Context and Activity
        startActivityForResult(intent, 0);
    }

    public void onClickReset(View view){
        edit.setText("");
        dropdown.setSelection(0);
    }



    public void onClickHelp(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("To research a recipe blablabla...");
        builder.setCancelable(true);



        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void testSearch(Ingredient[] ingredients,String[] operators) {
            Ingredient oignon = new Ingredient("oignon");
            Ingredient tomate = new Ingredient("tomate");
            Ingredient bacon = new Ingredient("bacon");
            Ingredient poulet = new Ingredient("poulet");
            Ingredient pomme = new Ingredient("pommme");

            Category quebecois = new Category("quebecois");
            Category chinois = new Category("chinois");
            Category indien = new Category("indien");

            LinkedList<Ingredient> list1 = new LinkedList<Ingredient>();
            list1.add(oignon);

            LinkedList<Ingredient> list2 = new LinkedList<Ingredient>();
            list2.add(oignon);
            list2.add(tomate);

            LinkedList<Ingredient> list3 = new LinkedList<Ingredient>();
            list3.add(oignon);
            list3.add(tomate);
            list3.add(bacon);

            LinkedList<Ingredient> list4 = new LinkedList<Ingredient>();
            list4.add(oignon);
            list4.add(tomate);
            list4.add(bacon);
            list4.add(poulet);

            LinkedList<Ingredient> list5 = new LinkedList<Ingredient>();
            list5.add(oignon);
            list5.add(tomate);
            list5.add(bacon);
            list5.add(poulet);
            list5.add(pomme);

            Recipe R1 = new Recipe("R1","15min",quebecois,list1,1,"ceci est une recette test");

            Recipe R2 = new Recipe("R2","15min",quebecois,list2,1,"ceci est une recette test");

            Recipe R3 = new Recipe("R3","15min",quebecois,list3,1,"ceci est une recette test");

            Recipe R4 = new Recipe("R4","15min",quebecois,list4,1,"ceci est une recette test");

            Recipe R5 = new Recipe("R5","15min",quebecois,list5,1,"ceci est une recette test");

            Recipe[] recipes = new Recipe[5];
            recipes[0] = R1;
            recipes[1] = R2;
            recipes[2] = R3;
            recipes[3] = R4;
            recipes[4] = R5;

            /*
            Ingredient[] ingredients = new Ingredient[5];
            ingredients[0] = oignon;
            ingredients[1] = tomate;
            ingredients[2] = bacon;
            ingredients[3] = poulet;
            ingredients[4] = pomme;

            String[] operateur = new String[4];
            operateur[0] = "and";
            operateur[1] = "or";
            operateur[2] = "and";
            operateur[3] = "not";
    `       */

            Pertinence pertinence = Pertinence.getPertinence();
            pertinence.updateRecipe(recipes);
            pertinence.updatePertinence(quebecois, ingredients, operators);
            pertinence.sortRecipe();
            /*
            Recipe[] recette = pertinence.getRecipes();
            for(int i = 0; i<5; i++){

                String nom = recette[i].getName();
                System.out.println("recette with name: " +nom + " pertinence: " +recette[i].recipePertinence);
                //System.out.println("pertinence: " +recette[i].recipePertinence);
            }
            */
        }

}
