package com.compilers.segcookhelper.activites;

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

import com.compilers.segcookhelper.R;
import com.compilers.segcookhelper.cookhelper.CookHelper;
import com.compilers.segcookhelper.cookhelper.Recipe;

import java.io.File;
import java.util.Date;

public class EditRecipeActivity extends Activity {

    int RESULT_LOAD_IMAGE = 1;
    int CAPTURE_IMAGE = 2;
    private CookHelper app;
    private EditText cookTimeField;
    private EditText ingredientField;
    private EditText descriptionField;
    private String message;
    private Spinner dropdown;
    private ImageView image;
    private Button save;
    private Button help;
    private String imgPath;
    private Recipe originalRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        // Containers
        cookTimeField = (EditText) findViewById(R.id.cooktimeEdit);
        ingredientField = (EditText) findViewById(R.id.ingredientsEdit);
        descriptionField = (EditText) findViewById(R.id.descriptionEdit);
        image = (ImageView) findViewById(R.id.imageRecipe);
        save = (Button) findViewById(R.id.SaveEdit);
        help = (Button) findViewById(R.id.HelpEdit);
        dropdown = (Spinner) findViewById(R.id.categoryEdit);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("RecipeName");


        app = CookHelper.getInstance(getApplicationContext());
        originalRecipe = app.getRecipe(message);
        String ingredientInString = originalRecipe.ingredientListToString();

        String[] categoryNameArray = app.getCategoryNameArray();

        cookTimeField.setText(originalRecipe.getCookTime());
        descriptionField.setText(originalRecipe.getDescription());
        ingredientField.setText(ingredientInString);
        image.setImageResource(getResources().getIdentifier(originalRecipe.getImg(), "drawable", getPackageName()));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryNameArray);

        dropdown.setAdapter(adapter);
        dropdown.setSelection(getIndex(dropdown, originalRecipe.getCategory().getName()));

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

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void onClickSave(View view) {
        if (dropdown.getSelectedItem().toString().matches("") || message.matches("") ||
                cookTimeField.getText().toString().matches("") || ingredientField.getText().toString().matches("") ||
                descriptionField.getText().toString().matches("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please fill up all the fields if you want your recipe to be save");
            builder.setCancelable(true);

            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // TODO:delete the recipe from the database and return to research screen;

                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            String name = originalRecipe.getName();

            String cat = dropdown.getSelectedItem().toString();
            String desc = descriptionField.getText().toString();
            // TODO: For now it uses the same image as the old recipe, should get the new one
            String img = originalRecipe.getImg();
            String time = cookTimeField.getText().toString();

            String[] ingredientsName = ingredientField.getText().toString().split(", ");

            app.editRecipe(originalRecipe, app.createRecipe(name, time, cat, ingredientsName, img, desc));

            Intent returnIntent = new Intent();
            returnIntent.putExtra("RecipeName", name);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    public void onClickHelp(View view) {
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

    public void onImageClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select where you want to take your picture");
        builder.setCancelable(true);

        builder.setNeutralButton("Application", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), DataBaseImages.class);
                startActivityForResult(intent, 0);
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
                startActivityForResult(intent, CAPTURE_IMAGE);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAPTURE_IMAGE) {
                image.setImageBitmap(BitmapFactory.decodeFile(imgPath));
            }
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
    }
}