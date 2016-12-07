package com.compilers.segcookhelper.cookhelper;



import android.graphics.Bitmap;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Recipe object with a name, a Description, an image, multiple Ingredients, a Category and a cookTime
 */

public class Recipe {

    int recipePertinence;
    private String name;
    private String description;
    private Bitmap img; // Maybe not Image type
    private LinkedList<Ingredient> linkedIngredient = new LinkedList<>();
    private Category category;
    private String cookTime;



    private String imageFromDatabase;

    /**
     * Constructs a Recipe
     *
     * @param name             the name of the recipe
     * @param cookTime         time required to cook
     * @param category         the category
     * @param linkedIngredient LinkedList of ingrdients that are part of the recipe
     * @param img              image chosen
     * @param description      description of the recipe
     */
    public Recipe(String name, String cookTime, Category category, LinkedList<Ingredient> linkedIngredient, Bitmap img, String description) {
        this.name = name;
        this.cookTime = cookTime;
        this.category = category;
        this.linkedIngredient = linkedIngredient;
        this.img = img;
        setDescription(description);
        this.recipePertinence = 0;
    }

    public Recipe(String name, String cookTime, Category category, Ingredient[] ingredientArray, Bitmap img, String description) {
        this.name = name;
        this.cookTime = cookTime;
        this.category = category;
        addIngredient(ingredientArray);
        this.img = img;
        setDescription(description);
        this.recipePertinence = 0;
    }


    public String getImageFromDatabase() {
        return imageFromDatabase;
    }

    public void setImageFromDatabase(String imageFromDatabase) {
        this.imageFromDatabase = imageFromDatabase;
    }
    /*
    Public methods
     */

    /**
     * Get the name of the recipe
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the recipe
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description of the recipe
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the recipe
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the int of the image
     *
     * @return the int of the image
     */
    public Bitmap getImg() {
        return img;
    }

    /**
     * Set the int of the image
     */
    public void setImg(Bitmap img) {
        this.img = img;
    }

    /**
     * Get the cooktime required for this recipe
     *
     * @return the cooktime
     */
    public String getCookTime() {
        return cookTime;
    }

    /**
     * Set the required cooktime
     *
     * @param cookTime the cooktime
     */
    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    /**
     * Get the category of the recipe
     *
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Get the name of the category of this recipe
     * @return the name of the category
     */
    public String getCategoryName(){
        return category.getName();
    }

    /**
     * Add an ingredient to the recipe
     *
     * @param ingredient the ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        if (!linkedIngredient.contains(ingredient)) {
                linkedIngredient.add(ingredient);
        } else {
            System.out.println("Ingredient " + ingredient.getName() +
                    " is already associated with recipe " + getName());
        }
    }

    /**
     * Add an array of ingredients to the recipe
     *
     * @param ingredientArray the array of ingredients to add
     */
    public void addIngredient(Ingredient[] ingredientArray) {
        for (Ingredient anIngredientArray : ingredientArray) {
            addIngredient(anIngredientArray);
        }
    }

    /**
     * Remove an ingredient form the recipe
     *
     * @param ingredient the ingredient to remove
     */
    public void removeIngredient(Ingredient ingredient) {
        if (!linkedIngredient.remove(ingredient)) {
            System.out.println("Failed to remove " + ingredient.getName() + " from " + getName());
        }
    }

    /**
     * Get an array of ingredients that are part of the recipe
     *
     * @return an Array of the ingredients
     */
    public Ingredient[] getIngredientArray() {
        return linkedIngredient.toArray(new Ingredient[linkedIngredient.size()]);
    }

    // added this method to facilitate the sorting process.
    public boolean containIngredients(Ingredient ingredient) {
        return linkedIngredient.contains(ingredient);
    }

    /**
     * get an Array of ingredient names
     *
     * @return the array of ingredient names
     */
    public String[] getIngredientStringArray() {
        String[] res = new String[linkedIngredient.size()];

        Iterator<Ingredient> i = linkedIngredient.iterator();
        Ingredient node;
        int j = 0;
        while (i.hasNext()) {
            node = i.next();
            res[j] = node.getName();
            j++;
        }
        return res;
    }

    /**
     * Get all the ingredients in String form
     * @return the ingredients separated with a space
     */
    public String getIngredientsString() {
        Iterator<Ingredient> i = linkedIngredient.iterator();

        String result = "";
        Ingredient node;

        while(i.hasNext()){
            node = i.next();
            result += node.getName();

            if(i.hasNext()){//add a space after if there is another element
                result += ", ";
            }
        }
        return result;
    }

    public boolean equals(Recipe other){
        return this.name.equals(other.name);
    }

    public String toString() {
        return name;
    }
}
