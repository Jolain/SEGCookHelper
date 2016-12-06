package com.compilers.segcookhelper.cookhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Jolain Poirier on 11/23/2016.
 */

class Database extends SQLiteOpenHelper {

    private static Database instance;//TODO memory leak
    private final Context context;
    private LinkedList<Recipe> linkedRecipe;
    private LinkedList<Ingredient> linkedIngredient;
    private LinkedList<Category> linkedCategory;
    //private LinkedList<Bitmap> linkedImages;
    //private LinkedList<String> linkedImagesName;
    private String dbPath = "";

    // Private Constructor

    private Database(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
        this.context = context;
        dbPath = context.getApplicationInfo().dataDir + "/databases/";
    }

    // Singleton Implementation

    // The synchronized keyword makes sure that only one thread accesses the database
    // at any time.
    static synchronized Database getInstance(Context context) {
        if(instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    // Override methods

    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(DatabaseContract.I_table.CREATE_TABLE);
        //db.execSQL(DatabaseContract.C_table.CREATE_TABLE);
        //db.execSQL(DatabaseContract.R_table.CREATE_TABLE);
        //db.execSQL(DatabaseContract.IM_table.CREATE_TABLE);
    }

    void onLoad() {
        // Checks to see if database file already exists.
        // If it isn't copy it from the asset folder.
        File test = new File(dbPath + DatabaseContract.DATABASE_NAME);
        System.out.println("DB exists test: " + test.exists());
        if(!test.exists()) { // If database does not exist
            System.out.println("DB exists");
            this.getReadableDatabase(); // Creates the path if non-existent
            this.close();
            try {
                InputStream dataInput = context.getAssets().open(DatabaseContract.DATABASE_NAME);
                OutputStream dataOutput = new FileOutputStream(dbPath + DatabaseContract.DATABASE_NAME);
                byte[] buffer = new byte[1024];
                int dataLength;
                while((dataLength = dataInput.read(buffer)) > 0) { // While there's still data in the buffer
                    dataOutput.write(buffer, 0, dataLength);
                }
                dataOutput.flush();
                dataOutput.close();
                dataInput.close();
            }
            catch(Exception e) {
                Log.v("SQL DATABASE:", "Failed to copy database from assets");
            }
        }
        // When the database is first initialized, it reads the entire database and creates
        // all the necessary objects. Clears existing objects in an atomic way.
        SQLiteDatabase db = getReadableDatabase();

        linkedRecipe = new LinkedList<>();
        linkedCategory = new LinkedList<>();
        linkedIngredient = new LinkedList<>();
        //linkedImagesName = new LinkedList<>();
        //linkedImages = new LinkedList<>();

        // Fetch data cursors
        String query = "SELECT * FROM ";
        Cursor ingredientCursor = db.rawQuery(query + DatabaseContract.I_table.TABLE_NAME, null);
        Cursor categoryCursor = db.rawQuery(query + DatabaseContract.C_table.TABLE_NAME, null);
        Cursor recipeCursor = db.rawQuery(query + DatabaseContract.R_table.TABLE_NAME, null);
        //Cursor imageCursor = db.rawQuery(query + DatabaseContract.IM_table.TABLE_NAME,null);


        //TODO add image cursor actions
        /**if (imageCursor.moveToFirst()){
            do {
                byte[] imgByte = imageCursor.getBlob(1);
                String name = imageCursor.getString(0);
                linkedImagesName.add(name);
                linkedImages.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
            }while(imageCursor.moveToNext());
        }
        imageCursor.close();*/

        // Parse data
        if(ingredientCursor.moveToFirst()) { // Check if data is present
            do {
                String name = ingredientCursor.getString(1);
                linkedIngredient.add(new Ingredient(name)); // Create ingredient and add it to the list
            } while(ingredientCursor.moveToNext()); // Move on to the next ingredients
        }
        ingredientCursor.close();

        if(categoryCursor.moveToFirst()) { // Check if data is present
            do {
                String name = categoryCursor.getString(1);
                linkedCategory.add(new Category(name)); // Create the category and add it to the list
            } while(categoryCursor.moveToNext()); // Move on to the next category
        }
        categoryCursor.close();

        if(recipeCursor.moveToFirst()) { // Check if data is present
            do {
                String name = recipeCursor.getString(1);
                Category category = getCategory(recipeCursor.getString(2));
                String description = recipeCursor.getString(3);
                // Fetches the drawable ID by searching by filename
                byte[] img = recipeCursor.getBlob(4); // Implementation was wrong, looking for a fix
                String time = recipeCursor.getString(5);
                String[] ingredient = stringToArray(recipeCursor.getString(6));
                Bitmap bit = DbBitmapUtility.getImage(img);
                // Construct an array of the linked ingredients
                LinkedList<Ingredient> ingObj = new LinkedList<>();
                for (String anIngredient : ingredient) {
                    ingObj.add(getIngredient(anIngredient));
                }
                linkedRecipe.add(new Recipe(name, time, category, ingObj, bit, description));
            } while(recipeCursor.moveToNext());
        }
        recipeCursor.close();
        db.close();

        Log.v("INGREDIENT ARRAY:", linkedIngredient.toString());
        Log.v("CATEGORY ARRAY:  ", linkedCategory.toString());
        Log.v("RECIPE ARRAY:    ", linkedRecipe.toString());
        //Log.v("IMAGES ARRAY:    ", linkedImages.toString());
        //Log.v("IMAGES NAME:     ", linkedImagesName.toString());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If database changes, drop old tables and create new ones
        db.execSQL(DatabaseContract.I_table.DELETE_TABLE);
        db.execSQL(DatabaseContract.C_table.DELETE_TABLE);
        db.execSQL(DatabaseContract.R_table.DELETE_TABLE);
        //db.execSQL(DatabaseContract.IM_table.DELETE_TABLE);
        onCreate(db);
    }

    Recipe[] recipeQuery(Ingredient[] ingredients) {
        String query;
        if(ingredients[0] == null) {
            return getRecipeArray(); // If query is null, return all recipe
        } else {
            query = "SELECT * FROM " + DatabaseContract.R_table.TABLE_NAME + " WHERE ";
            for (int i = 0; i < ingredients.length; i++) {
                if (ingredients[i] != null && ingredients[i].getName() != null) {
                    query = query + DatabaseContract.R_table.COL_INGREDIENT + " LIKE '%" + ingredients[i].getName() + "%'";
                }
                if (i + 1 < ingredients.length) {
                    query = query + " OR ";
                }
            }
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor recipeCursor = db.rawQuery(query, null);
        Recipe[] output = new Recipe[recipeCursor.getCount()];
        if(recipeCursor.moveToFirst()) { // Check if data is present
            int n = 0;
            do {
                String name = recipeCursor.getString(1);
                output[n] = getRecipe(name);
                n++;
            } while(recipeCursor.moveToNext());
        }
        recipeCursor.close();
        db.close();
        return output;
    }


    void addRecipe(Recipe recipe) {
        if(!linkedRecipe.contains(recipe)) {
            linkedRecipe.add(recipe);
            System.out.println("Database: added recipe " + recipe.getName());

            // SQLite Implementation
            SQLiteDatabase db = getWritableDatabase();
            ContentValues entry = new ContentValues();

            // Convert ingredients into a string
            String[] ingredientNames = recipe.getIngredientsString().split(", ");
            String convertedString = arrayToString(ingredientNames);
            byte[] byt = DbBitmapUtility.getBytes(recipe.getImg());
            // Insert values in the entry
            entry.put(DatabaseContract.R_table.COL_NAME, recipe.getName());
            entry.put(DatabaseContract.R_table.COL_CATEGORY, recipe.getCategoryName());
            entry.put(DatabaseContract.R_table.COL_DESC, recipe.getDescription());
            entry.put(DatabaseContract.R_table.COL_IMG, byt);
            entry.put(DatabaseContract.R_table.COL_TIME, recipe.getCookTime());
            entry.put(DatabaseContract.R_table.COL_INGREDIENT, convertedString);

            // Insert entry intro database
            try {
                db.insert(DatabaseContract.R_table.TABLE_NAME, null, entry);
            }
            catch (Exception e) {
                System.out.println("ERROR: Recipe was not added to SQLite database");
            }
            db.close(); // Required to not get sync errors
        }
    }

    void editRecipe(Recipe oldRecipe, Recipe editedRecipe) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues entry = new ContentValues();
        String[] ingredientNames = editedRecipe.getIngredientsString().split(", ");
        String convertedString = arrayToString(ingredientNames);
        Bitmap bit = editedRecipe.getImg();
        byte[] byt = DbBitmapUtility.getBytes(bit);
        entry.put(DatabaseContract.R_table.COL_NAME, editedRecipe.getName());
        entry.put(DatabaseContract.R_table.COL_CATEGORY, editedRecipe.getCategoryName());
        entry.put(DatabaseContract.R_table.COL_DESC, editedRecipe.getDescription());
        entry.put(DatabaseContract.R_table.COL_IMG, byt);
        entry.put(DatabaseContract.R_table.COL_TIME, editedRecipe.getCookTime());
        entry.put(DatabaseContract.R_table.COL_INGREDIENT, convertedString);
        try {
            db.update(DatabaseContract.R_table.TABLE_NAME, entry, DatabaseContract.R_table.COL_NAME + " = ?", new String[] {oldRecipe.getName()});
        }
        catch (Exception e) {
            System.out.println("ERROR: Recipe was not edited");
        }
        linkedRecipe.remove(oldRecipe);
        linkedRecipe.add(editedRecipe);

        db.close();
    }

    public void deleteRecipe (Recipe r) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete(DatabaseContract.R_table.TABLE_NAME, DatabaseContract.R_table.COL_NAME + " = ?", new String[] {r.getName()} );
            //linkedRecipe.remove(r);
        }
        catch (Exception e) {
            System.out.println("ERROR: Recipe was not deleted");
        }
    }

    void addIngredient(Ingredient i) {
        if (!linkedIngredient.contains(i)) {
            linkedIngredient.add(i);
            System.out.println("Database: added ingredient " + i.getName());

            // SQLite Implementation
            SQLiteDatabase db = getWritableDatabase();
            ContentValues entry = new ContentValues();

            // Insert values in the entry
            entry.put(DatabaseContract.I_table.COL_NAME, i.getName());

            // Insert entry intro database
            try {
                db.insert(DatabaseContract.I_table.TABLE_NAME, null, entry);
            } catch (Exception e) {
                System.out.println("ERROR: Ingredient was not added to SQLite database");
            }
            db.close(); // Required to not get sync errors
        }
    }

    // Redundant method
    //public boolean containsRecipe(Recipe recipe){
    //    return linkedRecipe.contains(recipe);
    //}

    Recipe getRecipe(String name) {

        Iterator<Recipe> i = linkedRecipe.iterator();
        try {
            Recipe node;
            while (i.hasNext()) {
                node = i.next();
                if (node.getName().equals(name)) {
                    return node;
                }
            }
        }catch(Exception e){
            System.out.println("No such recipe");
        }
        throw new IllegalArgumentException("Recipe with name: " + name +
                " is not included in the database");
    }

    Category getCategory(String name) {

        Iterator<Category> i = linkedCategory.iterator();

        Category node;
        while(i.hasNext()){
            node = i.next();
            if(node.getName().equals(name)){
                return node;
            }
        }
        //throw new IllegalArgumentException("Recipe with name: " + name +
               // " is not included in the database");
        return null;
    }

    Recipe[] getRecipeArray() {
        return linkedRecipe.toArray(new Recipe[linkedRecipe.size()]);
    }

    void removeRecipe(Recipe recipe) {
        linkedRecipe.remove(recipe);
        System.out.println("Database: removed recipe " + recipe.getName());
    }

    public void addCategory(Category category){
        if(!linkedCategory.contains(category)) {
            linkedCategory.add(category);
            System.out.println("Database: added category " + category.getName());
        }
    }

    Category[] getCategoryArray() {
        return linkedCategory.toArray(new Category[linkedCategory.size()]);
    }


    boolean containsIngredient(Ingredient ingredient) {
        return linkedIngredient.contains(ingredient);
    }

    Ingredient getIngredient(String name) {
        Iterator<Ingredient> i = linkedIngredient.iterator();

        Ingredient node;
        while(i.hasNext()){
            node = i.next();
            if(node.getName().equals(name)){
                return node;
            }
        }
//        throw new IllegalArgumentException("Recipe with name: " + name +
               // " is not included in the database");
        return null;
    }

    Ingredient[] getIngredientArray() {
        return linkedIngredient.toArray(new Ingredient[linkedIngredient.size()]);
    }

    void removeIngredient(Ingredient ingredient) {
        linkedIngredient.remove(ingredient);
        System.out.println("Database: removed ingredient " + ingredient.getName());
    }

    // Array Conversion Methods

    // These methods are used to store the ingredient names related to a recipe
    // under a single string, all separated by "__,__". This permits SQL to store
    // an array.

    String arrayToString(String[] array) {
        String outputString = "";
        for(int i = 0; i < array.length; i++) { // Append each element next to each other with __,__
            outputString = outputString + array[i];
            if(i < array.length - 1) { // Do not append comma to the last element
                outputString = outputString + "__,__";
            }
        }

        return outputString;
    }

    String[] stringToArray(String s) {
        return s.split("__,__");
    }

    /**void addEntry( String name, byte[] image) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new  ContentValues();

        cv.put(DatabaseContract.IM_table.COL_NAME,    name);
        cv.put(DatabaseContract.IM_table.COL_IMAGE,   image);
        linkedImages.add(BitmapFactory.decodeByteArray(image, 0, image.length));
        linkedImagesName.add(name);
        database.insert(DatabaseContract.IM_table.TABLE_NAME, null, cv );
    }*/

    /**Bitmap getImage(String name){
        for(int i = 0;i<linkedImagesName.size();i++){
            if(linkedImagesName.get(i).equals(name)){
                return linkedImages.get(i);
            }
        }
        return null;
    }*/



}
