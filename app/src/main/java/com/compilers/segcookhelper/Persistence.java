package com.compilers.segcookhelper;

/**
 * Created by martin on 2016-11-25.
 */

public class Persistence {
    private static Pertinence pertinence = new Pertinence();

    private int categoriePertinence = 3;
    private int ingredientPertinence = 1;
    private int andPertinence = 1;
    private int notPertinence = -1;
    private int highestPertinence = 0;
    private int lowestPertinence = 0;
    public Recipe[] recipeArray;

    private Pertinence(){}

    public Pertinence getPertinence(){
        return pertinence;
    }

    public void updateRecipe(Recipe[] recipes){
        recipeArray = recipes;
    }

    public void updatePertinence(Category category, Ingredient[] ingredients, String[] operateur){
        for(int x = 0; x < recipeArray.length;x++){
            this.calculatePertinence(this.recipeArray[x], category, ingredients, operateur);
        }
    }

    public void calculatePertinence(Recipe recipe,Category category, Ingredient[] ingredients,String[] operateur){
        int p = 0;

    }

    public void sortRecipe(){

    }

    public Recipe[] getRecipes(){
        return this.recipeArray;
    }
}
