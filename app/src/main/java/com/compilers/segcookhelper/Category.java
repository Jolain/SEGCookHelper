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
            System.out.print("Category " + getName() +
                    " is already associated with recipe "
                    + recipe.getName());
        }
    }

    //TODO possibly add more info to toString
    public String toString(){
        return name;
    }
}
