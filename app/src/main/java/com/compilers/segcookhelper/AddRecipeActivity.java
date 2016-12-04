package com.compilers.segcookhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.compilers.segcookhelper.R.id.imageView;

public class AddRecipeActivity extends Activity {
    private EditText recipename;
    private EditText cooktime;
    private EditText ingredient;

    private Spinner dropdown;
    private ImageView image;
    private EditText description;
    private Button create;
    private Button help;
    private String imgPath;
    int RESULT_LOAD_IMAGE = 1;
    int CAPTURE_IMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Database db = Database.getInstance(getApplicationContext());
        recipename = (EditText)findViewById(R.id.nameAdd);
        cooktime = (EditText)findViewById(R.id.cooktimeAdd);
        ingredient = (EditText)findViewById(R.id.ingredientsADD);

        description = (EditText)findViewById(R.id.descriptionAdd);

        image = (ImageView) findViewById(R.id.imageRecipeAdd);
        create = (Button)findViewById(R.id.createAdd);
        help = (Button)findViewById(R.id.HelpAdd);
        dropdown = (Spinner)findViewById(R.id.categoryADD);
        Category[] categoryArray = db.getCategoryArray();

        String[] categoryNameArray = new String[categoryArray.length];
        // categoryNameArray[0] = " ";
        for(int i=0;i<categoryNameArray.length;i++){
            categoryNameArray[i] = categoryArray[i].getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryNameArray);


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
    public void onClickCreate(View view){
        if(dropdown.getSelectedItem().toString().matches("")||recipename.getText().toString().matches("")||cooktime.getText().toString().matches("")||ingredient.getText().toString().matches("")||description.getText().toString().matches("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please fill up all the fields");
            builder.setCancelable(true);

            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Database dbHelper = Database.getInstance(getApplicationContext());
            String name = recipename.getText().toString();
            Category cat = dbHelper.getCategory(dropdown.getSelectedItem().toString());
            String desc = description.getText().toString();
            // TODO: For now it uses the default "ic_logo_00" picture, should use another image
            String img = "ic_logo_00";
            String time = cooktime.getText().toString();

            String[] ingredientsName = ingredient.getText().toString().split(", ");
            LinkedList<Ingredient> ingredientList = new LinkedList<>();
            for(int i = 0; i < ingredientsName.length; i++) {
                Ingredient tmp = dbHelper.getIngredient(ingredientsName[i]);
                if(tmp == null) {
                    tmp = new Ingredient(ingredientsName[i]);
                    dbHelper.addIngredient(tmp);
                }
                ingredientList.add(tmp);
            }

            dbHelper.addRecipe(new Recipe(name, time, cat, ingredientList, img, desc));
            dbHelper.close();
            Intent returnintent = new Intent();
            setResult(RESULT_OK, returnintent);
            finish();
        }
    }



    public void onClickHelp(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("To add a recipe blablabla...");
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
