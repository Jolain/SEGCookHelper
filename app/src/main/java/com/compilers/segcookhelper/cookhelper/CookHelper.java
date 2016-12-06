package com.compilers.segcookhelper.cookhelper;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.LinkedList;

/**
 * Facade class to CookHelper
 */

public class CookHelper {

    private static CookHelper instance;
    private final Database db;

    private Recipe[] recipeArray;

    private CookHelper(Context context) {
        // Initialise database singleton
        db = Database.getInstance(context);
        db.onLoad();
        db.close();
    }

    /**
     * Get the instance of CookHelper
     *
     * @param context the context of the application
     * @return the instance
     */
    public static CookHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CookHelper(context);
        }
        return instance;
    }

    /**
     * Get an array of recipes from the database from an Array of ingredients
     *
     * @param ingredientArray the ingredients queried
     * @return The recipes found
     */
    private Recipe[] recipeQuery(Ingredient[] ingredientArray) {
        recipeArray = db.recipeQuery(ingredientArray);
        db.close();
        return recipeArray;
    }

    /**
     * Add a recipe to the database
     *
     * @param recipe the recipe to add
     */
    public void addRecipe(Recipe recipe) {
        db.addRecipe(recipe);
        db.close();
    }

    /**
     * Get a recipe from the database with a name
     *
     * @param name the name of the recipe
     * @return the recipe found
     */
    public Recipe getRecipe(String name) {
        Recipe recipe = db.getRecipe(name);
        db.close();

        return recipe;
    }

    /**
     * Remove a recipe from the database
     *
     * @param recipe the recipe to remove
     */
    public void removeRecipe(Recipe recipe) {
        db.deleteRecipe(recipe);
        db.close();
    }

    /**
     * Create a recipe using multiple fields
     *
     * @param name                the name of the recipe
     * @param time                the time required to make the recipe
     * @param categoryName        the name of the category the recipe belongs to
     * @param ingredientNameArray an Array of ingredient names to be part of the recipe
     * @param img                 the image associated with the recipe
     * @param desc                the description of the recipe
     * @return the recipe created
     */
    public Recipe createRecipe(String name, String time, String categoryName, String[] ingredientNameArray, Bitmap img, String desc) {

        LinkedList<Ingredient> ingredientLinkedList = new LinkedList<>();
        for (String anIngredientNameArray : ingredientNameArray) {
            Ingredient tmp = db.getIngredient(anIngredientNameArray);
            if (tmp == null) {
                tmp = new Ingredient(anIngredientNameArray);
                db.addIngredient(tmp);
            }
            ingredientLinkedList.add(tmp);
        }

        Category category = db.getCategory(categoryName);
        db.close();

        return new Recipe(name, time, category, ingredientLinkedList, img, desc);
    }

    /**
     * Edit a recipe in the database
     *
     * @param previousRecipe the recipe before being modified
     * @param newRecipe      the modified recipe
     */
    public void editRecipe(Recipe previousRecipe, Recipe newRecipe) {
        db.editRecipe(previousRecipe, newRecipe);
        db.close();
    }

    /**
     * Add an array of ingredients to the database
     *
     * @param ingredientNameArray the array fo ingredient names
     */
    public void addIngredients(String[] ingredientNameArray) {
        for (String anIngredientNameArray : ingredientNameArray) {
            Ingredient tmp = db.getIngredient(anIngredientNameArray);
            if (tmp == null) {
                tmp = new Ingredient(anIngredientNameArray);
                db.addIngredient(tmp);
            }
        }
        db.close();
    }

    /**
     * Get an ingredient from the databse
     *
     * @param name the name of the ingredient
     * @return the ingredient found
     */
    public Ingredient getIngredient(String name) {
        Ingredient ingredient = db.getIngredient(name);
        db.close();
        return ingredient;
    }

    /**
     * Get an array of all the ingredients in the database
     *
     * @return the ingredients in the database
     */
    public Ingredient[] getIngredientArray() {
        Ingredient[] ingredientArray = db.getIngredientArray();
        db.close();
        return ingredientArray;
    }

    /**
     * Add a new ingredient to the database
     *
     * @param name the name of the ingredient to add
     */
    public void addIngredient(String name) {
        db.addIngredient(new Ingredient(name));
        db.close();
    }

    /**
     * Get a category from the database
     *
     * @param name the name of the category to get
     * @return the category found
     */
    public Category getCategory(String name) {
        Category category = db.getCategory(name);
        db.close();
        return category;
    }

    /**
     * Get an array of all the category names in the database
     *
     * @return the category names
     */
    public String[] getCategoryNameArray() {
        Category[] categoryArray = db.getCategoryArray();
        db.close();

        String[] categoryNameArray = new String[categoryArray.length];
        for (int i = 0; i < categoryNameArray.length; i++) {
            categoryNameArray[i] = categoryArray[i].getName();
        }
        return categoryNameArray;
    }

    /**
     * Creates an array of ingredients from an array of names
     *
     * @param ingredientNameArray the names of the ingredients
     * @return an array of ingredients
     */
    public Ingredient[] createIngredientArray(String[] ingredientNameArray) {
        Ingredient[] ingredients = new Ingredient[ingredientNameArray.length];
        for (int i = 0; i < ingredientNameArray.length; i++) {
            ingredients[i] = new Ingredient(ingredientNameArray[i]);
        }

        return ingredients;
    }

    /**
     * Sorts the recipes based on the arguments
     *
     * @param ingredients the ingredients used in the sorting
     * @param category    the categories used in the sorting
     * @param operators   the operators used in the sorting
     */
    public void sortPertinence(Ingredient[] ingredients, Category category, String[] operators) {

        recipeQuery(ingredients); // Liste de recette qui contient au moins 1 ingrédient

        Pertinence.updatePertinence(recipeArray, category, ingredients, operators);
        Pertinence.sortRecipe(recipeArray);

    }

    /**
     * Returns an array of recipes which were sorted the last time sortPertinence was called
     *
     * @return the sorted recipes
     */
    public Recipe[] getSortedRecipes() {
        return recipeArray;
    }
    //remove a specific element from an array, in this case, the method is use to remove a deleteRecipe in attribut recipeArray
    public Recipe[] removeElements(Recipe[] input, String deleteMe) {
        LinkedList<Recipe> results = new LinkedList<>();

        for(Recipe item : input)
            if(!deleteMe.equals(item.getName()))
                results.add(item);

        Recipe[] newResults = new Recipe[results.toArray(input).length-1];
        for(int i=0;i<newResults.length;i++){
            newResults[i] = results.toArray(input)[i];

        }
        recipeArray = newResults;
        return recipeArray;
    }
    //put a byte of image in the database
    /**public void addEntry(String name, byte[] image){
        db.addEntry(name,image);
    }
    //get an image from the database
    public Bitmap getImage(String name){
        return db.getImage(name);
    }*/
}
