package com.compilers.segcookhelper.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

    final int RESULT_LOAD_IMAGE = 1;
    final int CAPTURE_IMAGE = 2;
    private CookHelper app;
    private EditText cookTimeField;
    private EditText ingredientField;
    private EditText descriptionField;
    private String message;
    private Spinner dropdown;
    private ImageView image;
    private Button save;
    private Button help;
    private Bitmap bitmap;
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
        message = bundle.getString("RecipeName"); // récupère le nom spécifique de la recette de l'activité précédente


        app = CookHelper.getInstance(getApplicationContext());
        originalRecipe = app.getRecipe(message); // cherche dans l'application une recette selon le nom spécifié de la recette
        String ingredientInString = originalRecipe.getIngredientsString();

        String[] categoryNameArray = app.getCategoryNameArray();
        bitmap = originalRecipe.getImg();
        cookTimeField.setText(originalRecipe.getCookTime());
        descriptionField.setText(originalRecipe.getDescription());
        ingredientField.setText(ingredientInString);
        if(originalRecipe.getImg()!=null){ // si l'image bitmap de la recette obtenu dans le database n'est pas null, afficher l'image de cette activity selon le bitmap, sinon afficher l'image de l'activité selon le nom du fichier dans drawable
            image.setImageBitmap(bitmap);

        }else {
            image.setImageResource(getApplicationContext().getResources().getIdentifier(originalRecipe.getImageFromDatabase(), "drawable", getPackageName()));
            bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNameArray);

        dropdown.setAdapter(adapter);
        dropdown.setSelection(getIndex(dropdown, originalRecipe.getCategoryName()));

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // attribuer une action si tu cliques sur une des catégories du dialogue
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getIndex(Spinner spinner, String myString) { // méthode permettant de trouver l'index dans un spinner selon la valeur de cet index
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
                descriptionField.getText().toString().matches("")) {//check for empty fields
            ActivityUtil.openNeutralAlertDialog("Please fill up all the fields", this, true, "OK");

        } else if (!ActivityUtil.isWithinDescriptionLimits(descriptionField.getText().toString())) {//check if description is within limits
            ActivityUtil.openNeutralAlertDialog("Description must be within " + ActivityUtil.MAX_DESCRIPTION_LIMIT +
                    " and " + ActivityUtil.MIN_DESCRIPTION_LIMIT, this, true, "OK");
        } else {
            String name = originalRecipe.getName();

            String cat = dropdown.getSelectedItem().toString();
            String desc = descriptionField.getText().toString();


            String time = cookTimeField.getText().toString();

            String ingredientString = ingredientField.getText().toString();
            ingredientString = ingredientString.replaceAll(" ", "");
            String[] ingredientsNameArray = ingredientString.split(",");

            app.editRecipe(originalRecipe, app.createRecipe(name, time, cat, ingredientsNameArray, bitmap, desc));// supprime la vieille recette du database, et ajoute une nouvelle recette

            Intent returnIntent = new Intent();
            returnIntent.putExtra("RecipeName", name);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    public void onClickHelp(View view) {
        ActivityUtil.openNeutralAlertDialog("Veuillez rentrez les données correspondant à chaque case.\n" +
                "Cliquez sur l'image pour modifier l'image de cette recette.\n" +
                "Cliquez sur le nom de la catégorie pour sélectionnez une nouvelle catégorie.\n" +
                "Les ingrédients doivent être séparer par des virgules.", this, true, "OK");
    }

    public void onImageClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // permet a l'utilisateur de choisir l'image de la gallery photo où de la camera
        builder.setMessage("Choisissez où vous vouliez prendre votre image");
        builder.setCancelable(true);



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
        if (resultCode != Activity.RESULT_CANCELED) {// l'image de l'activité devient soit l'image pris dans la gallerie, ou l'image pris à partir de l'appareil photo
            if (requestCode == CAPTURE_IMAGE) {

                bitmap = BitmapFactory.decodeFile(imgPath);

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
                bitmap = BitmapFactory.decodeFile(picturePath);

                image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
    }
}
