package com.compilers.segcookhelper.activites;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import com.compilers.segcookhelper.cookhelper.DbBitmapUtility;

import java.io.File;
import java.util.Date;

public class AddRecipeActivity extends Activity {

    private final int RESULT_LOAD_IMAGE = 1;
    private final int CAPTURE_IMAGE = 2;
    private CookHelper app;
    private EditText recipeNameField;
    private EditText cookTimeField;
    private EditText ingredientField;
    private EditText descriptionField;
    private String imgPath;
    private Spinner dropdown;
    private ImageView image;
    private Button create;
    private Button help;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        recipeNameField = (EditText) findViewById(R.id.nameAdd);
        cookTimeField = (EditText) findViewById(R.id.cooktimeAdd);
        ingredientField = (EditText) findViewById(R.id.ingredientsADD);

        descriptionField = (EditText) findViewById(R.id.descriptionAdd);

        image = (ImageView) findViewById(R.id.imageRecipeAdd);
        create = (Button) findViewById(R.id.createAdd);
        help = (Button) findViewById(R.id.HelpAdd);
        dropdown = (Spinner) findViewById(R.id.categoryADD);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_logo_00); // attribut une image à bitmap
        app = CookHelper.getInstance(getApplicationContext());
        String[] categoryNameArray = app.getCategoryNameArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNameArray); // instancie un adapteur de categoryArray

        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  // crée une action si tu choisis un des items
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void onClickCreate(View view) {
        if (dropdown.getSelectedItem().toString().matches("") || recipeNameField.getText().toString().matches("") ||
                cookTimeField.getText().toString().matches("") || ingredientField.getText().toString().matches("") ||
                descriptionField.getText().toString().matches("")) { //check for empty fields
            ActivityUtil.openNeutralAlertDialog("S'il vous plaît, remplissez toutes les cases", this, true, "OK");

        } else if (!ActivityUtil.isWithinDescriptionLimits(descriptionField.getText().toString())) { //check if desrciption is within limits
            ActivityUtil.openNeutralAlertDialog("Description must be within " + ActivityUtil.MAX_DESCRIPTION_LIMIT +
                    " and " + ActivityUtil.MIN_DESCRIPTION_LIMIT, this, true, "OK");
        } else if(app.getRecipe(recipeNameField.getText().toString()) != null) {//check if a recipe with the same name already exists
            ActivityUtil.openNeutralAlertDialog("Recipe with name: " +
                    recipeNameField.getText().toString() + " already exists", this, true, "OK");
        } else{
            String name = recipeNameField.getText().toString();
            String cookTime = cookTimeField.getText().toString();
            String category = dropdown.getSelectedItem().toString();
            String ingredientString = ingredientField.getText().toString();

            //remove all spaces in the ingredient field
            ingredientString = ingredientString.replaceAll(" ", "");
            //split ingredients by commas
            String[] ingredientsNameArray = ingredientString.split(",");


            String desc = descriptionField.getText().toString();
            app.addRecipe(app.createRecipe(name, cookTime, category, ingredientsNameArray, bitmap, desc));

            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    public void onClickHelp(View view) { // affiche un dialogue lorsqu'on clique sur le bouton help
        ActivityUtil.openNeutralAlertDialog("Veuillez entrer les données correspondant à chaque case. Cliquez sur l'image" +
                " pour choisir une image pour votre recette. Cliquez sur Breakfast pour choisir une autre catégorie." +
                "lorsque vous ajoutez vos ingrédients, séparez chaque ingrédient par des virgules", this, true, "OK");
    }




    public void onImageClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this); // demande à l'utilisateur où il veut choisir son image
        builder.setMessage("Selectionnez où vous voulez prendre votre image.");
        builder.setCancelable(true);



        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { // ouvre la camera de ton apparail
                if(isCameraPermissionGranted()) {
                    File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
                    Uri imgUri = Uri.fromFile(file);
                    imgPath = file.getAbsolutePath();
                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(intent, CAPTURE_IMAGE);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {// ouvre la gallerie photos de ton appareil
                if(isStoragePermissionGranted()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAPTURE_IMAGE) {// remplace l'image de l'activité par la photo prise
                bitmap = BitmapFactory.decodeFile(imgPath);

                image.setImageBitmap(BitmapFactory.decodeFile(imgPath));
            }
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {//remplace l'image de l'activité par la photo sélectionnez dans la gallerie photo
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

// Implementation for SDK >= 23 devices, permissions need to be requested on run.

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("STORAGE:","Permission is granted");
                return true;
            } else {
                Log.v("STORAGE:","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("STORAGE:","Permission is granted");
            return true;
        }
    }

    public  boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.v("STORAGE:","Permission is granted");
                return true;
            } else {
                Log.v("STORAGE:","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("STORAGE:","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("","Permission: "+permissions[0]+ "was "+grantResults[0]);
        }
    }

}
