package com.compilers.segcookhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResearchActivity extends Activity {
    private Button search;
    private Button clear;
    private Button help;
    private EditText edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        clear = (Button)findViewById(R.id.clearButton);
        search = (Button)findViewById(R.id.searchButton);
        edit = (EditText)findViewById(R.id.searchQuery);
        help = (Button)findViewById(R.id.HelpResearch);

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
