package com.compilers.segcookhelper.cookhelper;

/**
 * Created by Jolain Poirier on 11/16/2016.
 */

public class Category {
    // **** Instance variable

    private String name;

    // **** Constructor

    /**
     * Constructs a Category
     * @param name the name of the category
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Get the name of the category
     * @return the name of the category
     */
    public String getName(){
        return name;
    }


    public boolean equals(Category other){
        return this.name.equals(other.name);
    }

    /**
     * Returns a String representing the category
     * @return the String
     */
    public String toString(){
        return name;
    }
}



/*
private LinkedList<Recipe> linkedRecipe = new LinkedList<>();

public void addRecipe(Recipe recipe){
    if(!linkedRecipe.contains(recipe)) {
        linkedRecipe.add(recipe);
    } else{
        System.out.print("Category " + getName() +
                " is already associated with recipe "
                + recipe.getName());
    }
}

public void removeRecipe(Recipe recipe){
    if(!linkedRecipe.remove(recipe)) {
        System.out.println("Failed to remove " + recipe.getName() + " from " + getName());
    }
}

public Recipe[] getRecipeArray(){
    return (Recipe[])linkedRecipe.toArray();
}
*/
