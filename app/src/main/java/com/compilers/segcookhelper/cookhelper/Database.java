package com.compilers.segcookhelper.cookhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

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

    private static Database instance; // Can possibly cause a memory leak by storing a context value in a static object
    private final Context context;
    private LinkedList<Recipe> linkedRecipe; // Container for all the recipe objects
    private LinkedList<Ingredient> linkedIngredient; // Container for all the ingredient objects
    private LinkedList<Category> linkedCategory; // Container for all the category objects

    private String dbPath = "";
    private Bitmap bit;

    // Private Constructor

    private Database(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
        this.context = context;
        dbPath = context.getApplicationInfo().dataDir + "/databases/";
    }

    // Singleton Implementation

    // The synchronized keyword makes sure that only one thread accesses the database
    // at any time to prevent IOException & SQLException.
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

    // Method called when app is initialised, code could technically be moved to onCreate()
    // Loads the database from disk (or copy it from assets folder if not present) and load
    // objects into memory for easier access.
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
        Cursor recipeCursor = db.rawQuery(query + DatabaseContract.R_table.TABLE_NAME,null);




        // Parse data for all cursors and create objects.

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
                String imgName = recipeCursor.getString(4);
                // Fetches the drawable ID by searching by filename

                String time = recipeCursor.getString(5);
                String[] ingredient = stringToArray(recipeCursor.getString(6));
                byte[] img = recipeCursor.getBlob(7); // this will be null if the image isn't from the user



                if (img == null) {
                    bit = null;
                }else {
                    bit = DbBitmapUtility.getImage(img);
                }
                // construct an array of linkedlist
                LinkedList<Ingredient> ingObj = new LinkedList<>();
                for (String anIngredient : ingredient) {
                    ingObj.add(getIngredient(anIngredient));
                }
                Recipe recet = new Recipe(name,time,category, ingObj,bit,description);
                recet.setImageFromDatabase(imgName);// give a string name which is the name of images in drawable
                linkedRecipe.add(recet);
            }while(recipeCursor.moveToNext());
        }
        recipeCursor.close();
        db.close();

        Log.v("INGREDIENT ARRAY:", linkedIngredient.toString());
        Log.v("CATEGORY ARRAY:  ", linkedCategory.toString());
        Log.v("RECIPE ARRAY:    ", linkedRecipe.toString());

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If database changes, drop old tables and create empty new ones
        // This method isn't used but is required to override
        // db.execSQL(DatabaseContract.I_table.DELETE_TABLE);
        // db.execSQL(DatabaseContract.C_table.DELETE_TABLE);
        // db.execSQL(DatabaseContract.R_table.DELETE_TABLE);
        //db.execSQL(DatabaseContract.IM_table.DELETE_TABLE);
        onCreate(db);
    }

    // This method is used to fetch an SQL query based on the ingredients inputted by the user.
    // It returns an Recipe[] array containing all the recipes stored containing at least 1 ingredient
    // matched by the query.
    Recipe[] recipeQuery(Ingredient[] ingredients) {
        String query;
        if(ingredients[0] == null) {
            return getRecipeArray(); // If query is null, return all recipe
        } else {
            query = "SELECT * FROM " + DatabaseContract.R_table.TABLE_NAME + " WHERE "; // Construct base SQLite query

            /*
            Note: this implementation is bad because it's vulnerable to SQL injections if you edit
            a recipe ingredients value to contain SQL code. Although in this context, this is a local
            database which contains no sensitive information and is not a problem.
            */

            for (int i = 0; i < ingredients.length; i++) { // Construct remaining query using user input
                if (ingredients[i] != null && ingredients[i].getName() != null) {
                    query = query + DatabaseContract.R_table.COL_INGREDIENT + " LIKE '%" + ingredients[i].getName() + "%'";
                }
                if (i + 1 < ingredients.length) {
                    query = query + " OR ";
                }
            }
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor recipeCursor = db.rawQuery(query, null); // Pass query to SQLite database
        Recipe[] output = new Recipe[recipeCursor.getCount()]; // Create empty array to hold recipes
        if(recipeCursor.moveToFirst()) { // Check if data is present
            int n = 0;
            do {
                String name = recipeCursor.getString(1);
                output[n] = getRecipe(name); // Fetch matching recipes from existing list
                n++;
            } while(recipeCursor.moveToNext());
        }
        recipeCursor.close();
        db.close();
        return output;
    }

    // This method takes a created Recipe object from user input and adds it to the database.
    // This method SHOULD check the ingredients values for SQL injection.
    void addRecipe(Recipe recipe) {
        if(!linkedRecipe.contains(recipe)) {
            System.out.println("Database: added recipe " + recipe.getName());

            // SQLite Implementation
            SQLiteDatabase db = getWritableDatabase();
            ContentValues entry = new ContentValues();

            // Convert ingredients into a string
            String[] ingredientNames = recipe.getIngredientsString().split(", ");
            String convertedString = arrayToString(ingredientNames);
            // Convert BitmapImage to raw bytes for storage
            byte[] byt = DbBitmapUtility.getBytes(recipe.getImg());
            // Insert values in the entry
            entry.put(DatabaseContract.R_table.COL_NAME, recipe.getName());
            entry.put(DatabaseContract.R_table.COL_CATEGORY, recipe.getCategoryName());
            entry.put(DatabaseContract.R_table.COL_DESC, recipe.getDescription());
            //entry.put(DatabaseContract.R_table.COL_IMG, byt);
            entry.put(DatabaseContract.R_table.COL_TIME, recipe.getCookTime());
            entry.put(DatabaseContract.R_table.COL_INGREDIENT, convertedString);
            entry.put(DatabaseContract.R_table.COL_BLOB,byt);

            // Insert entry into database
            try {
                db.insert(DatabaseContract.R_table.TABLE_NAME, null, entry);
                linkedRecipe.add(recipe); // Add the created recipe to the private list
            }
            catch (Exception e) {
                System.out.println("ERROR: Recipe was not added to SQLite database");
            }
            db.close(); // Required to not get sync errors
        }
    }

    // This method deletes the oldRecipe object from the list, creates a new one using the edited values
    // and add this new recipe to the SQLite database.
    // This method SHOULD also check the ingredients values for SQL injection.
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
        //entry.put(DatabaseContract.R_table.COL_IMG, byt);
        entry.put(DatabaseContract.R_table.COL_TIME, editedRecipe.getCookTime());
        entry.put(DatabaseContract.R_table.COL_INGREDIENT, convertedString);
        entry.put(DatabaseContract.R_table.COL_BLOB,byt);
        try {
            db.update(DatabaseContract.R_table.TABLE_NAME, entry, DatabaseContract.R_table.COL_NAME + " = ?", new String[] {oldRecipe.getName()});
            linkedRecipe.remove(oldRecipe);
            linkedRecipe.add(editedRecipe);
        }
        catch (Exception e) {
            System.out.println("ERROR: Recipe was not edited");
        }

        db.close();
    }

    // Deletes a given recipe from the database and the linked objects list.
    void deleteRecipe (Recipe r) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete(DatabaseContract.R_table.TABLE_NAME, DatabaseContract.R_table.COL_NAME + " = ?", new String[] {r.getName()} );
            linkedRecipe.remove(r);
        }
        catch (Exception e) {
            System.out.println("ERROR: Recipe was not deleted");
        }
    }

    // Adds a given ingredient to the database. This method is only called when the user
    // creates or edit a recipe and an ingredient named does not exist in the database.
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

    // Returns a Recipe object contained in the private list matching the name given, if it exists.
    // Returns null otherwise.
    Recipe getRecipe(String name) {

        Iterator<Recipe> i = linkedRecipe.iterator();
        try {
            Recipe node;
            while (i.hasNext()) {
                node = i.next();
                if (node.getName().equals(name)) { // If name matches
                    return node; // Return recipe
                }
            }
        }catch(Exception e){
            System.out.println("No such recipe");
        }
        return null;
    }

    // Returns a Recipe object contained in the private list matching the name given, if it exists.
    // Returns null otherwise.
    Category getCategory(String name) {

        Iterator<Category> i = linkedCategory.iterator();

        Category node;
        while(i.hasNext()){
            node = i.next();
            if(node.getName().equals(name)){ // If name matches
                return node; // Return category
            }
        }
        return null;
    }

    Recipe[] getRecipeArray() {
        return linkedRecipe.toArray(new Recipe[linkedRecipe.size()]);
    }

    Category[] getCategoryArray() {
        return linkedCategory.toArray(new Category[linkedCategory.size()]);
    }


    boolean containsIngredient(Ingredient ingredient) {
        return linkedIngredient.contains(ingredient);
    }
    // Returns an Ingredient object contained in the private list matching the name given, if it exists.
    // Returns null otherwise.
    Ingredient getIngredient(String name) {
        Iterator<Ingredient> i = linkedIngredient.iterator();

        Ingredient node;
        while(i.hasNext()){
            node = i.next();
            if(node.getName().equals(name)){
                return node;
            }
        }
        return null;
    }

    Ingredient[] getIngredientArray() {
        return linkedIngredient.toArray(new Ingredient[linkedIngredient.size()]);
    }

    // This method should never be used, breaks app when used.
    /*
    void removeIngredient(Ingredient ingredient) {
        linkedIngredient.remove(ingredient);
        System.out.println("Database: removed ingredient " + ingredient.getName());
    }
    */

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





}
