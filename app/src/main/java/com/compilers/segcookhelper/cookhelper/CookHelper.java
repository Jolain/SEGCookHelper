package com.compilers.segcookhelper.cookhelper;

import android.content.Context;

import java.util.LinkedList;

/**
 * Created by Mathieu on 04/12/2016.
 */

public class CookHelper {

    private static CookHelper instance;
    private Database db;

    private CookHelper(Context context) {
        // Initialise database singleton
        Database db = Database.getInstance(context);
        db.onLoad();
        db.close();
    }

    public static CookHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CookHelper(context);
        }
        return instance;
    }

    public Recipe[] recipeQuery(Ingredient[] ingredientArray) {
        Recipe[] recipeArray = db.recipeQuery(ingredientArray);
        db.close();
        return recipeArray;
    }

    public void addRecipe(Recipe recipe) {
        db.addRecipe(recipe);
        db.close();
    }

    public Recipe getRecipe(String name) {
        Recipe recipe = db.getRecipe(name);
        db.close();

        return recipe;
    }

    public void removeRecipe(Recipe recipe) {
        db.removeRecipe(recipe);
        db.close();
    }

    public Recipe createRecipe(String name, String time, String cat, String[] ingredientNameArray, String img, String desc) {

        LinkedList<Ingredient> ingredientLinkedList = new LinkedList<>();
        for (int i = 0; i < ingredientNameArray.length; i++) {
            Ingredient tmp = db.getIngredient(ingredientNameArray[i]);
            if (tmp == null) {
                tmp = new Ingredient(ingredientNameArray[i]);
                db.addIngredient(tmp);
            }
            ingredientLinkedList.add(tmp);
        }

        Category category = db.getCategory(cat);
        db.close();

        return new Recipe(name, time, category, ingredientLinkedList, img, desc);
    }

    public void editRecipe(Recipe previousRecipe, Recipe newRecipe) {
        db.editRecipe(previousRecipe, newRecipe);
        db.close();
    }

    public void addIngredients(String[] ingredientNameArray) {
        for (int i = 0; i < ingredientNameArray.length; i++) {
            Ingredient tmp = db.getIngredient(ingredientNameArray[i]);
            if (tmp == null) {
                tmp = new Ingredient(ingredientNameArray[i]);
                db.addIngredient(tmp);
            }
        }
        db.close();
    }

    public Ingredient getIngredient(String name) {
        Ingredient ingredient = db.getIngredient(name);
        db.close();
        return ingredient;
    }

    public Ingredient[] getIngredientArray() {
        Ingredient[] ingredientArray = db.getIngredientArray();
        db.close();
        return ingredientArray;
    }

    public void addIngredient(String name) {
        db.addIngredient(new Ingredient(name));
        db.close();
    }

    public Category getCategory(String name) {
        Category category = db.getCategory(name);
        db.close();
        return category;
    }

    public String[] getCategoryNameArray() {
        Category[] categoryArray = db.getCategoryArray();
        db.close();

        String[] categoryNameArray = new String[categoryArray.length];
        for (int i = 0; i < categoryNameArray.length; i++) {
            categoryNameArray[i] = categoryArray[i].getName();
        }
        return categoryNameArray;
    }
}
