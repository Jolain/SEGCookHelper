package com.compilers.segcookhelper.cookhelper;

/**
 * Created by Jolain Poirier on 11/16/2016.
 */

public class Ingredient {
    // **** Instance variable

    private String name;

    // **** Constructor

    /**
     * Constructs an ingredient
     * @param name the name of the ingredient
     */
    public Ingredient(String name){
        this.name = name;
    }

//    public Ingredient(String name, Recipe recipe) {
//        this(name);
//        linkedRecipe.add(recipe);
//    }

    // **** Public methods

    /**
     * Get the name of the ingredient
     * @return the name of the ingredient
     */
    public String getName(){
        return name;
    }

    public boolean equals(Ingredient other){
        return this.name.equals(other.name);
    }

    /**
     * Returns a String representing the ingredient
     * @return the String
     */
    public String toString() {
        return name;
    }
}

/*
private LinkedList<Recipe> linkedRecipe = new LinkedList<>();

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
        if(linkedRecipe.size() < 1) {}
    } else {
        System.out.println("Failed to remove " + recipe + " from " + name);
    }
}

public Recipe[] getRecipeArray(){
    return (Recipe[])linkedRecipe.toArray();
}
*/
