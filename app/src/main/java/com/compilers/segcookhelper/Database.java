package com.compilers.segcookhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.IllegalFormatCodePointException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Jolain Poirier on 11/23/2016.
 */

public class Database extends SQLiteOpenHelper {

    private static Database instance;
    private LinkedList<Recipe> linkedRecipe = new LinkedList<Recipe>();
    private LinkedList<Ingredient> linkedIngredient = new LinkedList<Ingredient>();
    private LinkedList<Category> linkedCategory = new LinkedList<Category>();

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

            // Convert ingredients into storable SQLite array
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
        }
    }

    public boolean containsRecipe(Recipe recipe){
        return linkedRecipe.contains(recipe);
    }

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
        throw new IllegalArgumentException("Recipe with name: " + name +
                " is not included in the database");
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

    private String arrayToString(String[] array) {
        String outputString = "";
        for(int i = 0; i < array.length; i++) { // Append each element next to each other with __,__
            outputString = outputString + array[i];
            if(i < array.length - 1) { // Do not append comma to the last element
                outputString = outputString + "__,__";
            }
        }

        return outputString;
    }

    private String[] stringToArray(String s) {
        return s.split("__,__");
    }

}
