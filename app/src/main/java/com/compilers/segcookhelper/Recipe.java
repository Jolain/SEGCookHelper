package com.compilers.segcookhelper;



import java.util.LinkedList;

/**
 * Created by Jolain Poirier on 11/16/2016.
 */

public class Recipe {

    /*
    Instance variables
     */
    private String name;
    private String description;
    private int img; // Maybe not Image type
    private LinkedList<Ingredient> linkedIngredient = new LinkedList<>();
    private Category category;
    private String cookTime;
    public int recipePertinence;

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
    public Recipe(String name, String cookTime, Category category, LinkedList<Ingredient> linkedIngredient, int img, String description) {
        this.name = name;
        this.cookTime = cookTime;
        //this.linkedCategory = linkedCategory;
        this.category = category;
        this.linkedIngredient = linkedIngredient;
        this.img = img;
        this.description = description;
        this.recipePertinence = 0;
        //TODO database.addRecipe(this)
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
    public int getImg() {
        return img;
    }

    /**
     * Set the int of the image
     */
    public void setImg(int img) {
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
     * Add an ingredient to the recipe
     *
     * @param ingredient the ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        //TODO check database for existing ingredients
        if (!linkedIngredient.contains(ingredient)) {
            linkedIngredient.add(ingredient);
        } else {
            System.out.println("Ingredient " + ingredient.toString() +
                    " is already associated with recipe " + getName());
        }
    }

    /**
     * Remove an ingredient form the recipe, deletes the recipe if the last ingredient is removed
     *
     * @param ingredient the ingredient to remove
     */
    public void removeIngredient(Ingredient ingredient) {
        if (linkedIngredient.remove(ingredient)) { // If the remove operation succeeded, check list size
            if (linkedIngredient.size() < 1 && linkedIngredient.size() < 1) {
            } //TODO database.removeRecipe(this)
        } else {
            System.out.println("Failed to remove " + ingredient.getName() + " from " + getName());
        }
    }

    /**
     * Get an array of ingredients that are part of the recipe
     *
     * @return an Array of the ingredients
     */
    public Ingredient[] getIngredientArray() {
        // String[] array = linkedlist.toArray(new String[linkedlist.size()]);
        // return (Ingredient[])linkedIngredient.toArray();
        Ingredient[] tempArray = linkedIngredient.toArray(new Ingredient[linkedIngredient.size()]);
        return tempArray;
    }

    // added this method to facilitate the sorting process.
    public boolean containIngredients(Ingredient ingredient) {
        if (linkedIngredient.contains(ingredient)) {
            return true;
        } else {
            return false;
        }
    }

    //TODO possibly add more info to toString
    public String toString() {
        return name;
    }
}
