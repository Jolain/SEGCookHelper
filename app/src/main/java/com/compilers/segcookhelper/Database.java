package com.compilers.segcookhelper;

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

    // Private Constructor

    private Database(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    // Singleton Implementation

    public static synchronized Database getInstance(Context context) {
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

    private LinkedList<Recipe> linkedRecipe = new LinkedList<Recipe>();
    private LinkedList<Ingredient> linkedIngredient = new LinkedList<Ingredient>();
    private LinkedList<Category> linkedCategory = new LinkedList<Category>();

    public void addRecipe(Recipe recipe){
        if(!linkedRecipe.contains(recipe)) {
            linkedRecipe.add(recipe);
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

    public void removeRecipe(Recipe recipe){
        linkedRecipe.remove(recipe);
    }

    public void addCategory(Category category){
        if(linkedCategory.contains(category)) {
            linkedCategory.add(category);
        }
    }

    public void addIngredient(Ingredient ingredient){
        if(!linkedIngredient.contains(ingredient)) {
            linkedIngredient.add(ingredient);
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

    public void removeIngredient(Ingredient ingredient){
        linkedIngredient.remove(ingredient);
    }

}
