package com.compilers.segcookhelper.cookhelper;

import java.util.Objects;

/**
 * Created by martin on 2016-11-25.
 */

class Pertinence {
    private static Pertinence pertinence;

    private int categoriePertinence = 3;
    private int ingredientPertinence = 1;
    private int andPertinence = 1;
    private int notPertinence = 3;
    private int highestPertinence = -100; // will facilitate the sorting
    private int lowestPertinence = 100; // will facilitate the sorting
    private Recipe[] recipeArray;

    // the classe pertinence is a singleton
    private Pertinence() {
    }

    // permet d'avoir acces a l'objet pertinence
    public static Pertinence getPertinence() {
        if (pertinence == null) {
            pertinence = new Pertinence();
        }
        return pertinence;
    }

    // UpdateRecipe recoit une liste de recipe de la database apres qu'un search aille ete effectuer
    // C'est cette liste de recipe qui serais trier.
    void updateRecipe(Recipe[] recipes) {
        recipeArray = recipes;
    }

    // permet au different activity au travers de l'application d'avoir asser au recette trier
    Recipe[] getRecipeArray() {
        //TODO currently gets all recipes from database without sorting
        return recipeArray;
    }

    // send every recipe to have their pertinence calculated and updated
    // the recipes sended are the one found from the database
    void updatePertinence(Category category, Ingredient[] ingredients, String[] operateur) {
        this.highestPertinence = 0;
        this.lowestPertinence = 100;
        for (Recipe aRecipeArray : recipeArray) {
            //System.out.println("recette with name: " + this.recipeArray[x].getName());
            this.calculatePertinence(aRecipeArray, category, ingredients, operateur);
        }
    }

    // calculate the pertinence of the recipe with respect to the list of ingredients and the category
    void calculatePertinence(Recipe recipe, Category category, Ingredient[] ingredients, String[] operateur) {
        int p = 0;
        boolean[] ingredientsMatch;
        ingredientsMatch = new boolean[ingredients.length];
        // if the same category add the equivalent pertinence
        if ((recipe.getCategory().getName()).equals(category.getName())) {
            p = p + this.categoriePertinence;
        }

        // verifying which ingredient is present
        Ingredient[] listRecipeIngredient = recipe.getIngredientArray();

        for (int i = 0; i < ingredients.length; i++) {
            boolean start = true;
            int index = 0;
            // ici on test chaque ingredients de la recette selection avec la liste d'ingredient
            // donner par l'utilisateur
            while (start) {
                if (index < listRecipeIngredient.length) {
                    if (Objects.equals(listRecipeIngredient[index].getName(), ingredients[i].getName())) {
                        p = p + this.ingredientPertinence;
                        ingredientsMatch[i] = true;
                        start = false;
                    } else {
                        index++;
                    }
                } else {
                    start = false;
                }
            }
        }

        // taking into account the and, or, not. I am assuming that between every ingredients
        // an operator is present. In other word operator length is 1 less then the ingredients length
        for (int i = 0; i < operateur.length; i++) {
            switch (operateur[i]) {

                case "and":
                    if (ingredientsMatch[i] && ingredientsMatch[i + 1]) {
                        p = p + this.andPertinence;
                    }
                    break;

                case "not":
                    if (ingredientsMatch[i + 1]) {
                        p = p - this.notPertinence;
                    }
                    break;

                default:
                    break;
            }
        }

        // update the pertinence
        recipe.recipePertinence = p;
        // this place make the sorting faster by setting upper and lower bound on the pertinence
        if (p > this.highestPertinence) {
            this.highestPertinence = p;
        }
        if (p < this.lowestPertinence) {
            this.lowestPertinence = p;
        }


    }

    // this method sort the recipe since we already determined the highest and lowest pertinence it give us a
    // starting point
    void sortRecipe() {
        int position = 0;
        int pertinenceIndex = this.highestPertinence;
        Recipe placeholder;

        while (position < this.recipeArray.length) {

            for (int i = position; i < this.recipeArray.length; i++) {
                //
                if (this.recipeArray[i].recipePertinence == pertinenceIndex) {
                    placeholder = this.recipeArray[i];
                    this.recipeArray[i] = this.recipeArray[position];
                    this.recipeArray[position] = placeholder;
                    position++;
                }
            }

            pertinenceIndex = pertinenceIndex - 1;
        }

    }
}
