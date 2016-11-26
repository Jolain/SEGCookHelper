package com.compilers.segcookhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class AddRecipeActivity extends Activity {
    private EditText recipename;
    private EditText cooktime;
    private EditText ingredient;
    private EditText category;
    private Spinner spinner;
    private ImageView image;
    private Button create;
    private Button help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        recipename = (EditText)findViewById(R.id.nameAdd);
        cooktime = (EditText)findViewById(R.id.cooktimeAdd);
        ingredient = (EditText)findViewById(R.id.ingredientsADD);
        category = (EditText)findViewById(R.id.ingredientsADD);
        spinner = (Spinner)findViewById(R.id.categoryADD);
        image = (ImageView) findViewById(R.id.imageRecipeAdd);
        create = (Button)findViewById(R.id.createAdd);
        help = (Button)findViewById(R.id.HelpAdd);
    }
    public void onClickCreate(View view){
        // create new recipe in the data base
        Intent returnintent = new Intent();
        setResult(RESULT_OK,returnintent);

        Database dbHelper = Database.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //TODO: Actually create the item & add it to the database

        finish();
    }

    public void onClickHelp(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("To Add a recipe blablabla...");
        builder.setCancelable(true);



        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onImageClick(View view){
        Intent intent = new Intent(getApplicationContext(),ImagesDatabase.class);
        startActivityForResult(intent,0);
    }

}
