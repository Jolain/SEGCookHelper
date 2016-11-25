package com.compilers.segcookhelper;

import java.util.LinkedList;

/**
 * Created by Jolain Poirier on 11/16/2016.
 */

public class Ingredient {

    // **** Instance variable

    private String name;
    private LinkedList<Recipe> linkedRecipe = new LinkedList<>();

    // **** Constructor

    public Ingredient(String name, Recipe recipe) {
        this.name = name;
        linkedRecipe.add(recipe);
        //TODO database.addIngredient(this)
    }

    // **** Public methods

    public String getName(){
        return name;
    }

    public void addRecipe(Recipe recipe) {
        if (!linkedRecipe.contains(recipe)){
            linkedRecipe.add(recipe);
        } else {
            System.out.println("Ingredient " + getName()
            + " is already associated with recipe " + recipe.getName());
        }
    }

    public void removeRecipe(Recipe recipe) {
        if(linkedRecipe.remove(recipe)) { // If the remove operation succeeded, check list size
            if(linkedRecipe.size() < 1) {} //TODO database.removeIngredient(this)
        } else {
            System.out.println("Failed to remove " + recipe + " from " + name);
        }
    }

    public Recipe[] getRecipeArray(){
        return (Recipe[])linkedRecipe.toArray();
    }

    public String toString() {
        return name;
    }

}
