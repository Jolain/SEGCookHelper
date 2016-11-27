package com.compilers.segcookhelper;

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
                // TODO Auto-generated method stub

            }

        });

    }

    public void onClickSearchRecipe(View view) {

        String query = edit.getText().toString();
        Database dbHelper = Database.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //TODO: Add search implementation

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


}
