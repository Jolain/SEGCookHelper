package com.compilers.segcookhelper.cookhelper;

import java.util.Objects;

/**
 * Util class used to determine the pertinence of Recipes and sort them based off that pertinence
 */

class Pertinence {

    private static final int categoryPertinence = 3;
    private static final int ingredientPertinence = 1;
    private static final int andPertinence = 1;
    private static final int notPertinence = 3;
    private static int highestPertinence = -100; // will facilitate the sorting
    private static int lowestPertinence = 100; // will facilitate the sorting

    /**
     * Updates the pertinence of recipes based on arguments
     *
     * @param recipeArray the recipes to update
     * @param category    the categories to base the pertinence off
     * @param ingredients the ingredients to base the pertinence off
     * @param operator    the operators to base the pertinence off
     */
    static void updatePertinence(Recipe[] recipeArray, Category category, Ingredient[] ingredients, String[] operator) {
        highestPertinence = 0;
        lowestPertinence = 100;
        for (Recipe aRecipe : recipeArray) {
            //System.out.println("recette with name: " + this.recipeArray[x].getName());
            calculatePertinence(aRecipe, category, ingredients, operator);
        }
    }

    // calculate the pertinence of the recipe with respect to the list of ingredients and the category
    private static void calculatePertinence(Recipe recipe, Category category, Ingredient[] ingredients, String[] operateur) {
        int p = 0;
        boolean[] ingredientsMatch;
        ingredientsMatch = new boolean[ingredients.length];
        // if the same category add the equivalent pertinence
        if ((recipe.getCategoryName()).equals(category.getName())) {
            p = p + categoryPertinence;
        }

        // verifying which ingredient is present
        String[] listRecipeIngredient = recipe.getIngredientStringArray();

        for (int i = 0; i < ingredients.length; i++) {
            boolean start = true;
            int index = 0;
            // ici on test chaque ingredients de la recette selection avec la liste d'ingredient
            // donner par l'utilisateur
            while (start) {
                if (index < listRecipeIngredient.length) {
                    if (Objects.equals(listRecipeIngredient[index], ingredients[i].getName())) {
                        p = p + ingredientPertinence;
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
                        p = p + andPertinence;
                    }
                    break;

                case "not":
                    if (ingredientsMatch[i + 1]) {
                        p = p - notPertinence;
                    }
                    break;

                default:
                    break;
            }
        }
        // update the pertinence
        recipe.recipePertinence = p;
        // this place make the sorting faster by setting upper and lower bound on the pertinence
        if (p > highestPertinence) {
            highestPertinence = p;
        }
        if (p < lowestPertinence) {
            lowestPertinence = p;
        }
    }

    /**
     * Sorts an array based on it's pertinence
     *
     * @param recipeArray the recipes to sort
     */
    static void sortRecipe(Recipe[] recipeArray) {
        // this method sort the recipe since we already determined the highest and lowest pertinence it give us a
        // starting point
        int position = 0;
        int pertinenceIndex = highestPertinence;
        Recipe placeholder;

        while (position < recipeArray.length) {

            for (int i = position; i < recipeArray.length; i++) {
                //
                if (recipeArray[i].recipePertinence == pertinenceIndex) {
                    placeholder = recipeArray[i];
                    recipeArray[i] = recipeArray[position];
                    recipeArray[position] = placeholder;
                    position++;
                }
            }
            pertinenceIndex = pertinenceIndex - 1;
        }
    }
}
