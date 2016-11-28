package com.compilers.segcookhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.w3c.dom.Node;

import java.io.File;
import java.util.Date;

public class EditRecipeActivity extends Activity {

    private EditText cooktime;
    private EditText ingredient;
    private StringBuilder ingredientInString;

    private Spinner dropdown;
    private ImageView image;
    private Button save;
    private Button help;
    private EditText description;
    private String imgPath;
    int RESULT_LOAD_IMAGE = 1;
    int CAPTURE_IMAGE = 2;
    private Recipe recipeDatabase;
    private Recipe newRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        cooktime = (EditText)findViewById(R.id.cooktimeEdit);
        ingredient = (EditText)findViewById(R.id.ingredientsEdit);
        description = (EditText)findViewById(R.id.descriptionEdit);

        image = (ImageView) findViewById(R.id.imageRecipe);
        save = (Button)findViewById(R.id.SaveEdit);
        help = (Button)findViewById(R.id.HelpEdit);
        dropdown = (Spinner)findViewById(R.id.categoryEdit);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("RecipeName");
        // research a recipe from the database with name == message;
        //recipeDatabase = recipeFoundInTheDatabase
        cooktime.setText(recipeDatabase.getCookTime());
        description.setText(recipeDatabase.getDescription());
        Ingredient[] current = recipeDatabase.getIngredientArray();
        for(int i =0; i < current.length;i++){
            ingredientInString.append(current[i].getName()+ ", ");
        }


        ingredient.setText(ingredientInString);

        image.setImageBitmap(recipeDatabase.getImg());


        String[] items = new String[]{" ", "chinese", "breakfast", "italian", "dinner", "collation", "cookies", "drink"}; // this is only to help me
        // Category[] category = database.category; ???
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

    public void onClickSave(View view){
        // update method for the recipe in the data base
        Intent returnintent = new Intent();
        setResult(RESULT_OK,returnintent);
        finish();
    }

    public void onClickHelp(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" To edit a recipe blablabla...");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select where you want to take your picture");
        builder.setCancelable(true);



        builder.setNeutralButton("Application", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(),DataBaseImages.class);
                startActivityForResult(intent,0);
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
                Uri imgUri = Uri.fromFile(file);
                imgPath = file.getAbsolutePath();
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(intent,CAPTURE_IMAGE);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
                dialog.dismiss();
            }
        });



        AlertDialog alert = builder.create();
        alert.show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAPTURE_IMAGE) {

                image.setImageBitmap(BitmapFactory.decodeFile(imgPath));

            }
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                image.setImageBitmap(BitmapFactory.decodeFile(picturePath));


            }


        }





    }

}
