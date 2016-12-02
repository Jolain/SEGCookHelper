package com.compilers.segcookhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EditRecipeActivity extends Activity {

    private EditText cooktime;
    private EditText ingredient;

    private String message;
    private Spinner dropdown;
    private ImageView image;
    private Button save;
    private Button help;
    private EditText description;
    private String imgPath;
    int RESULT_LOAD_IMAGE = 1;
    int CAPTURE_IMAGE = 2;
    private Recipe originalRecipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        // Containers
        cooktime = (EditText)findViewById(R.id.cooktimeEdit);
        ingredient = (EditText)findViewById(R.id.ingredientsEdit);
        description = (EditText)findViewById(R.id.descriptionEdit);
        image = (ImageView) findViewById(R.id.imageRecipe);
        save = (Button)findViewById(R.id.SaveEdit);
        help = (Button)findViewById(R.id.HelpEdit);
        dropdown = (Spinner)findViewById(R.id.categoryEdit);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("RecipeName");
        Database db = Database.getInstance(getApplicationContext());
        originalRecipe = db.getRecipe(message);

        cooktime.setText(originalRecipe.getCookTime());
        description.setText(originalRecipe.getDescription());
        String ingredientInString = "";
        Ingredient[] current = originalRecipe.getIngredientArray();
        for(int i =0; i < current.length;i++){
            ingredientInString+= current[i].getName()+ ", ";
        }



        ingredient.setText(ingredientInString);
        image.setImageResource(Integer.parseInt(originalRecipe.getImg()));

        Category[] categoryArray = db.getCategoryArray();

        String[] categoryNameArray = new String[categoryArray.length];

        for(int i=0;i<categoryNameArray.length;i++){
            categoryNameArray[i] = categoryArray[i].getName();
        }

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryNameArray);
        String[] items = new String[]{" ", "chinese", "breakfast", "italian", "dinner", "collation", "cookies", "drink"}; // this is only to help me

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(getIndex(dropdown,originalRecipe.getCategory().getName()));

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
        db.close();
    }
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    public LinkedList<Ingredient> stringIntoLinkedList(){
        String ingredients = ingredient.getText().toString();
        List<String> items = Arrays.asList(ingredients.split("\\s*,\\s*"));
        Ingredient[] resultIng = new Ingredient[items.size()];
        for(int i =0; i<items.size();i++){
            resultIng[i] = new Ingredient(items.get(i));
        }
        List<Ingredient> listIngredients = new ArrayList<Ingredient>(Arrays.asList(resultIng));
        LinkedList<Ingredient> linkedIngredients = new LinkedList<Ingredient>();
        for(int i = 0;i<listIngredients.size();i++){
            linkedIngredients.add(listIngredients.get(i));
        }
        return linkedIngredients;

    }

    public void onClickSave(View view) {
        if(dropdown.getSelectedItem().equals(null)&&message.equals(null)&&cooktime.getText().equals(null)&&ingredient.getText().equals(null)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please fill up all the fields if you want your recipe to be save");
            builder.setCancelable(true);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // TODO:delete the recipe from the database and return to research screen;

                    finish();
                }
            });

        }else{

        Database dbHelper = Database.getInstance(getApplicationContext());
        String name = originalRecipe.getName();
        Category cat = dbHelper.getCategory(dropdown.getSelectedItem().toString());
        String desc = description.getText().toString();

        String img = Integer.toString(image.getId());
        String time = cooktime.getText().toString();
        LinkedList<Ingredient> ingredients = stringIntoLinkedList();

        dbHelper.editRecipe(originalRecipe,new Recipe(name, time, cat, ingredients, img, desc));

        Intent returnintent = new Intent();
        returnintent.putExtra("RecipeName", name);
        setResult(RESULT_OK, returnintent);
        finish();
    }
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
