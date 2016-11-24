package com.compilers.segcookhelper;

import java.util.LinkedList;

/**
 * Created by Jolain Poirier on 11/16/2016.
 */

public class Category {

    // **** Instance variable

    private String name;
    private LinkedList<Recipe> linkedRecipe = new LinkedList<>();

    // **** Constructor

    public Category(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addRecipe(Recipe recipe){
        if(!linkedRecipe.contains(recipe)) {
            linkedRecipe.add(recipe);
        } else{
            System.out.print("Recipe " + recipe.toString() +
                    " is already associated with category "
                    + name);
        }
    }

    //TODO possibly add more info to toString
    public String toString(){
        return name;
    }
}
