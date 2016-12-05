package com.compilers.segcookhelper.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.compilers.segcookhelper.R;
import com.compilers.segcookhelper.cookhelper.Category;
import com.compilers.segcookhelper.cookhelper.CookHelper;
import com.compilers.segcookhelper.cookhelper.Ingredient;
import com.compilers.segcookhelper.cookhelper.Pertinence;
import com.compilers.segcookhelper.cookhelper.Recipe;

import java.util.LinkedList;

public class ResearchActivity extends Activity {
    private CookHelper app;

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

        app = CookHelper.getInstance(getApplicationContext());

        String[] categoryNameArray = app.getCategoryNameArray();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNameArray);

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

        String query = edit.getText().toString();

        String[] splitArray = query.split("\\s+");
        String[] ingredientsString = new String[(splitArray.length+1)/2];
        String[] operators = new String[(splitArray.length-1)/2];
        for(int i = 0;i<splitArray.length;i++){

            if((i%2)==0){
                ingredientsString[i/2] = splitArray[i];
                Log.i("info",splitArray[i]);
            }
            else{
                operators[(i-1)/2] = splitArray[i];
                Log.i("info",splitArray[i]);
            }
        }
        Ingredient[] ingredients = new Ingredient[ingredientsString.length];
        for(int i = 0;i<ingredientsString.length;i++){
            ingredients[i] = new Ingredient(ingredientsString[i]);
        }

         Recipe[] recipes = app.recipeQuery(ingredients); // Liste de recette qui contient au moins 1 ingrédient

          Pertinence pertinence = Pertinence.getPertinence();
          pertinence.updateRecipe(recipes);
          pertinence.updatePertinence(app.getCategory(dropdown.getSelectedItem().toString()), ingredients, operators);
          pertinence.sortRecipe();

        //this.testSearch(ingredients,operators);

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

            LinkedList<Ingredient> list1 = new LinkedList<>();
            list1.add(oignon);

            LinkedList<Ingredient> list2 = new LinkedList<>();
            list2.add(oignon);
            list2.add(tomate);

            LinkedList<Ingredient> list3 = new LinkedList<>();
            list3.add(oignon);
            list3.add(tomate);
            list3.add(bacon);

            LinkedList<Ingredient> list4 = new LinkedList<>();
            list4.add(oignon);
            list4.add(tomate);
            list4.add(bacon);
            list4.add(poulet);

            LinkedList<Ingredient> list5 = new LinkedList<>();
            list5.add(oignon);
            list5.add(tomate);
            list5.add(bacon);
            list5.add(poulet);
            list5.add(pomme);

        String R1_description = "1.\tIn a 2 gallon container, mix together the brown sugar, curing mixture, and water. Submerge the pork belly in the mixture so that it is covered completely. If the meat floats, you can weigh it down with a dinner plate or similar object. Refrigerate covered for six days.\n" +
                "2.\tLight charcoal in an outdoor smoker. Soak wood chips in a bowl of water. When the temperature of the smoker is between 140 and 150 degrees coals are ready. Smoke the pork belly for 6 hours, throwing a handful of wood chips on the coals about once an hour. Store in the refrigerator. Slice and fry as you would with store-bought bacon.\n";

        String R2_description = "1.\tIn a small bowl, mix the eggs and water.\n" +
                "2.\tMix the bread crumbs and garlic salt in a medium bowl. In a medium bowl, blend the flour and cornstarch.\n" +
                "3.\tIn a large heavy saucepan, heat the oil to 365 degrees F (185 degrees C).\n" +
                "4.\tOne at a time, coat each mozzarella stick in the flour mixture, then the egg mixture, then in the bread crumbs and finally into the oil. Fry until golden brown, about 30 seconds. Remove from heat and drain on paper towels.\n";

        String R3_description = "1. Place chopped onions, bell peppers, and ground beef in large skillet that has been sprayed with Pam. Cook until ground beef is well done.\n" +
                "2. Cook elbow noodles in water for 9 minutes.\n" +
                "3. Add both cans of tomato soup to the cooked and drained ground beef. Simmer until the noodles are cooked.\n" +
                "4. Drain noodles and add to mix. Stir well.\n";

        String R4_description = "1)On medium heat melt the butter and sautee the onion and bell peppers.\n" +
                "2)Add the hamburger meat and cook until meat is well done.\n" +
                "3)Add the tomato sauce, salt, pepper and garlic powder.\n" +
                "4)Salt, pepper and garlic powder can be adjusted to your own tastes.\n" +
                "5)Cook noodles as directed.\n" +
                "6)Mix the sauce and noodles if you like, I keep them separated";

        String R5_description = "1. Heat 1 tablespoon salted butter in a cast-iron or nonstick skillet over medium-low heat.\n" +
                "2. Press the sandwich slightly and place it in the skillet. Cook until golden on the bottom, 3 to 5 minutes.\n" +
                "3. Flip, adding more butter to the pan if needed, and cook until the other side is golden and the cheese melts, 3 to 5 more minutes.\n";

        Recipe R1 = new Recipe("bacon breakfast","15min",quebecois,list1,"bacon_breakfast",R1_description);

        Recipe R2 = new Recipe("cheese stick","15min",quebecois,list2,"macaroni",R2_description);

        Recipe R3 = new Recipe("macaroni","15min",quebecois,list3,"macaroni",R3_description);

        Recipe R4 = new Recipe("spaghetti","15min",quebecois,list4,"ic_logo_00",R4_description);

        Recipe R5 = new Recipe("grilled cheese","15min",quebecois,list5,"macaroni",R5_description);

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
