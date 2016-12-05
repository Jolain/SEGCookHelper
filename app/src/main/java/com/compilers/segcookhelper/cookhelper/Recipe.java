package com.compilers.segcookhelper.cookhelper;



import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Jolain Poirier on 11/16/2016.
 */

public class Recipe {

    public int recipePertinence;
    /*
    Instance variables
     */
    private String name;
    private String description;
    private String img; // Maybe not Image type
    private LinkedList<Ingredient> linkedIngredient = new LinkedList<>();
    private Category category;
    private String cookTime;
    private int MAX_DESCRIPTION_LIMIT = 500;
    private int MIN_DESCRIPTION_LIMIT = 1;
    private Database db;

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
    public Recipe(String name, String cookTime, Category category, LinkedList<Ingredient> linkedIngredient, String img, String description) {
        this.name = name;
        this.cookTime = cookTime;
        this.category = category;
        this.linkedIngredient = linkedIngredient;
        this.img = img;
        setDescription(description);
        this.recipePertinence = 0;
    }

    public Recipe(String name, String cookTime, Category category, Ingredient[] ingredientArray, String img, String description) {
        this.name = name;
        this.cookTime = cookTime;
        this.category = category;
        addIngredient(ingredientArray);
        this.img = img;
        setDescription(description);
        this.recipePertinence = 0;
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
        if (description.length() <= MAX_DESCRIPTION_LIMIT && description.length() >= MIN_DESCRIPTION_LIMIT) {
            this.description = description;
        } else{
            //TODO Do something if description > 500 characters
        }
    }

    /**
     * Get the int of the image
     *
     * @return the int of the image
     */
    public String getImg() {
        return img;
    }

    /**
     * Set the int of the image
     */
    public void setImg(String img) {
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
        //TODO check database for existing ingredients (test inplementation)

        if (!linkedIngredient.contains(ingredient)) {
            if(!db.containsIngredient(ingredient)) {
                linkedIngredient.add(ingredient);
                db.addIngredient(ingredient);
            } else{
                linkedIngredient.add(db.getIngredient(ingredient.getName()));
            }
        } else {
            System.out.println("Ingredient " + ingredient.toString() +
                    " is already associated with recipe " + getName());
        }
    }

    /**
     * Add an array of ingredients to the recipe
     *
     * @param ingredientArray the array of ingredients to add
     */
    public void addIngredient(Ingredient[] ingredientArray) {
        for (int i = 0; i < ingredientArray.length; i++) {
            addIngredient(ingredientArray[i]);
        }
    }

    /**
     * Remove an ingredient form the recipe, deletes the recipe if the last ingredient is removed
     *
     * @param ingredient the ingredient to remove
     */
    public void removeIngredient(Ingredient ingredient) {
        //TODO database.removeRecipe(this) (test implementation)

        if (linkedIngredient.remove(ingredient)) { // If the remove operation succeeded, check list size
            if (linkedIngredient.size() < 1 && linkedIngredient.size() < 1) {
                db.removeRecipe(this);
            }
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
        return linkedIngredient.toArray(new Ingredient[linkedIngredient.size()]);
    }

    // added this method to facilitate the sorting process.
    public boolean containIngredients(Ingredient ingredient) {
        return linkedIngredient.contains(ingredient);
    }

    /**
     * Get all the ingredients in String form
     * @return the ingredients separated with a space
     */
    public String ingredientListToString(){
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
