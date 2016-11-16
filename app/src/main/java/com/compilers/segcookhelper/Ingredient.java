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

    public Ingredient(String n, Recipe r) {
        name = n;
        linkedRecipe.add(r);
    }

    // **** Public methods

    public void addRecipe(Recipe r) {
        linkedRecipe.add(r);
    }

    public void removeRecipe(Recipe r) {
        if(linkedRecipe.remove(r)) { // If the remove operation succeeded, check list size
            if(linkedRecipe.size() < 1) {} // database.removeIngredient(this)
        } else {
            System.out.println("Failed to remove " + r + " from " + name);
        }
    }

    public String toString() {
        return name;
    }

}
