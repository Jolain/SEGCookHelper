package com.compilers.segcookhelper.activites;

import android.app.Activity;
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
import com.compilers.segcookhelper.cookhelper.CookHelper;

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
        clear = (Button) findViewById(R.id.clearButton);
        search = (Button) findViewById(R.id.searchButton);
        edit = (EditText) findViewById(R.id.searchQuery);
        help = (Button) findViewById(R.id.HelpResearch);
        dropdown = (Spinner) findViewById(R.id.catSpinner);

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

        if(splitArray[0].equals("not") || splitArray[0].equals("and") ||splitArray[0].equals("NOT") || splitArray[0].equals("AND") || splitArray[0].equals("And") ||splitArray[0].equals("Not")){
            ActivityUtil.openNeutralAlertDialog("Veuillez commencer votre recherche en entrant un ingrédient et  avec un opérateur AND ou NOT", this, true, "OK");

        }else {
            String[] ingredientsString = new String[(splitArray.length + 1) / 2];
            String[] operators = new String[(splitArray.length - 1) / 2];
            for (int i = 0; i < splitArray.length; i++) {

                if ((i % 2) == 0) {
                    ingredientsString[i / 2] = splitArray[i];
                    Log.i("info", splitArray[i]);
                } else {
                    operators[(i - 1) / 2] = splitArray[i];
                    Log.i("info", splitArray[i]);
                }
            }
            app.sortPertinence(app.createIngredientArray(ingredientsString),
                    app.getCategory(dropdown.getSelectedItem().toString()), operators);
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class); //Application Context and Activity
            startActivityForResult(intent, 0);
        }
    }

    public void onClickReset(View view) {
        edit.setText("");
        dropdown.setSelection(0);
    }

    public void onClickHelp(View view) {
        ActivityUtil.openNeutralAlertDialog("1. Tout d’abord les recherches sont faites selon les catégories (entrée, sauce etc..), le type de plat  (Éthiopien, Indien, Français etc..) et une liste des ingrédients.\n" +
                "2. Les recherches selon les ingrédients devront utiliser des expressions AND , OR, NOT.\n", this, true, "OK");
    }
}

