package com.compilers.segcookhelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Jolain Poirier on 11/23/2016.
 */

public class Database extends SQLiteOpenHelper {

    private static Database instance;
    private LinkedList<Recipe> linkedRecipe;
    private LinkedList<Ingredient> linkedIngredient;
    private LinkedList<Category> linkedCategory;

    // Private Constructor

    private Database(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
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
        db.execSQL(DatabaseContract.I_table.CREATE_TABLE);
        db.execSQL(DatabaseContract.C_table.CREATE_TABLE);
        db.execSQL(DatabaseContract.R_table.CREATE_TABLE);
    }

    public void onLoad(SQLiteDatabase db) {
        // When the database is first initialized, it reads the entire database and creates
        // all the necessary objects. Clears existing objects in an atomic way.

        linkedRecipe = new LinkedList<>();
        linkedCategory = new LinkedList<>();
        linkedIngredient = new LinkedList<>();

        // Fetch data cursors
        String query = "SELECT * FROM ";
        Cursor ingredientCursor = db.rawQuery(query + DatabaseContract.I_table.TABLE_NAME, null);
        Cursor categoryCursor = db.rawQuery(query + DatabaseContract.C_table.TABLE_NAME, null);
        Cursor recipeCursor = db.rawQuery(query + DatabaseContract.R_table.TABLE_NAME, null);

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
                int img = 0; // Implementation was wrong, looking for a fix
                String time = recipeCursor.getString(5);
                String[] ingredient = stringToArray(recipeCursor.getString(6));

                // Construct an array of the linked ingredients
                LinkedList<Ingredient> ingObj = new LinkedList<>();
                for(int i = 0; i < ingredient.length; i++) {
                    ingObj.add(getIngredient(ingredient[i]));
                }
                linkedRecipe.add(new Recipe(name, time, category, ingObj, img, description));
            } while(recipeCursor.moveToNext());
        }
        recipeCursor.close();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If database changes, drop old tables and create new ones
        db.execSQL(DatabaseContract.I_table.DELETE_TABLE);
        db.execSQL(DatabaseContract.C_table.DELETE_TABLE);
        db.execSQL(DatabaseContract.R_table.DELETE_TABLE);
        onCreate(db);
    }

    //TODO replace methods with database implementation
    //TEMPORARY METHODS AND ATTRIBUTES



    public void addRecipe(Recipe recipe){
        if(!linkedRecipe.contains(recipe)) {
            linkedRecipe.add(recipe);
            System.out.println("Database: added recipe " + recipe.getName());

            // SQLite Implementation
            SQLiteDatabase db = getWritableDatabase();
            ContentValues entry = new ContentValues();

            // Convert ingredients into a string
            String[] ingredientNames = recipe.ingredientListToString().split(" ");
            String convertedString = arrayToString(ingredientNames);

            // Insert values in the entry
            entry.put(DatabaseContract.R_table.COL_NAME, recipe.getName());
            entry.put(DatabaseContract.R_table.COL_CATEGORY, recipe.getCategoryName());
            entry.put(DatabaseContract.R_table.COL_DESC, recipe.getDescription());
            entry.put(DatabaseContract.R_table.COL_IMG, recipe.getImg());
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

    public void editRecipe(Recipe editedRecipe) {

    }

    // Redundant method
    //public boolean containsRecipe(Recipe recipe){
    //    return linkedRecipe.contains(recipe);
    //}

    public Recipe getRecipe(String name){

        Iterator<Recipe> i = linkedRecipe.iterator();

        Recipe node;
        while(i.hasNext()){
            node = i.next();
            if(node.getName().equals(name)){
                return node;
            }
        }
        throw new IllegalArgumentException("Recipe with name: " + name +
                " is not included in the database");
    }

    public Category getCategory(String name){

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

    public Recipe[] getRecipeArray(){
        return linkedRecipe.toArray(new Recipe[linkedRecipe.size()]);
    }

    public void removeRecipe(Recipe recipe){
        linkedRecipe.remove(recipe);
        System.out.println("Database: removed recipe " + recipe.getName());
    }

    public void addCategory(Category category){
        if(!linkedCategory.contains(category)) {
            linkedCategory.add(category);
            System.out.println("Database: added category " + category.getName());
        }
    }

    public Category[] getCategoryArray(){
        return linkedCategory.toArray(new Category[linkedCategory.size()]);
    }

    public void addIngredient(Ingredient ingredient){
        if(!linkedIngredient.contains(ingredient)) {
            linkedIngredient.add(ingredient);
            System.out.println("Database: added ingredient " + ingredient.getName());
        }
    }

    public boolean containsIngredient(Ingredient ingredient){
        return linkedIngredient.contains(ingredient);
    }

    public Ingredient getIngredient(String name){
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

    public Ingredient[] getIngredientArray(){
        return linkedIngredient.toArray(new Ingredient[linkedIngredient.size()]);
    }

    public void removeIngredient(Ingredient ingredient){
        linkedIngredient.remove(ingredient);
        System.out.println("Database: removed ingredient " + ingredient.getName());
    }

    // Array Conversion Methods

    // These methods are used to store the ingredient names related to a recipe
    // under a single string, all separated by "__,__". This permits SQL to store
    // an array.

    public String arrayToString(String[] array) {
        String outputString = "";
        for(int i = 0; i < array.length; i++) { // Append each element next to each other with __,__
            outputString = outputString + array[i];
            if(i < array.length - 1) { // Do not append comma to the last element
                outputString = outputString + "__,__";
            }
        }

        return outputString;
    }

    public String[] stringToArray(String s) {
        return s.split("__,__");
    }

}
