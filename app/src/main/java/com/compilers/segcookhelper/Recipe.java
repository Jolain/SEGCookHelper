package com.compilers.segcookhelper;



import java.util.Iterator;
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

        //TODO database.addRecipe(this) (test implementation)

        Database db = Database.getInstance(null);
        db.addRecipe(this);
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

        Database db = Database.getInstance(null);

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
     * Remove an ingredient form the recipe, deletes the recipe if the last ingredient is removed
     *
     * @param ingredient the ingredient to remove
     */
    public void removeIngredient(Ingredient ingredient) {
        //TODO database.removeRecipe(this) (test implementation)
        Database db = Database.getInstance(null);

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
        // String[] array = linkedlist.toArray(new String[linkedlist.size()]);
        // return (Ingredient[])linkedIngredient.toArray();
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
                result += " ";
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
