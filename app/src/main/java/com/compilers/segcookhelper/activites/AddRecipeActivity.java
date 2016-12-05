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

    public void onClickCreate(View view) {
        if (dropdown.getSelectedItem().toString().matches("") || recipeNameField.getText().toString().matches("") ||
                cookTimeField.getText().toString().matches("") || ingredientField.getText().toString().matches("") ||
                descriptionField.getText().toString().matches("")) {
            ActivityUtil.openNeutralAlertDialog("Please fill up all the fields", this, true, "OK");
            // TODO:delete the recipe from the database and return to research screen;
        } else if (!ActivityUtil.isWithinDescriptionLimits(descriptionField.getText().toString())) {
            ActivityUtil.openNeutralAlertDialog("Description must be within " + ActivityUtil.MAX_DESCRIPTION_LIMIT +
                    " and " + ActivityUtil.MIN_DESCRIPTION_LIMIT, this, true, "OK");
        } else {
            String name = recipeNameField.getText().toString();
            String cookTime = cookTimeField.getText().toString();
            String category = dropdown.getSelectedItem().toString();
            String[] ingredientsNameArray = ingredientField.getText().toString().split(", ");
            // TODO: For now it uses the default "ic_logo_00" picture, should use another image
            String img = "ic_logo_00";
            String desc = descriptionField.getText().toString();

            app.addRecipe(app.createRecipe(name, cookTime, category, ingredientsNameArray, img, desc));

            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    public void onClickHelp(View view) {
        ActivityUtil.openNeutralAlertDialog("Veuillez entrer les données correspondant à chaque case.", this, true, "OK");
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
