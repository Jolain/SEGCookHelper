package com.compilers.segcookhelper;
import java.util.Objects;

/**
 * Created by martin on 2016-11-25.
 */

public class Pertinence {
    private static Pertinence pertinence = new Pertinence();

    private int categoriePertinence = 3;
    private int ingredientPertinence = 1;
    private int andPertinence = 1;
    private int notPertinence = 3;
    private int highestPertinence = 0; // will facilitate the sorting
    private int lowestPertinence = 100; // will facilitate the sorting
    private Recipe[] recipeArray = new Recipe[0];

    private Pertinence() {
    }

    public static Pertinence getPertinence() {
        return pertinence;
    }

    public void updateRecipe(Recipe[] recipes) {
        recipeArray = recipes;
    }

    public Recipe[] getRecipeArray(){
        //TODO currently gets all recipes from database without sorting
        Database db = Database.getInstance(null);
        return db.getRecipeArray();
    }

    // send every recipe to have their pertinence calculated and updated
    public void updatePertinence(Category category, Ingredient[] ingredients, String[] operateur) {
        this.highestPertinence = 0;
        this.lowestPertinence = 100;
        for (int x = 0; x < recipeArray.length; x++) {
            //System.out.println("recette with name: " + this.recipeArray[x].getName());
            this.calculatePertinence(this.recipeArray[x], category, ingredients, operateur);
        }
    }

    // calculate the pertinence of the recipe with respect to the list of ingredients and the category
    public void calculatePertinence(Recipe recipe, Category category, Ingredient[] ingredients, String[] operateur) {
        int p = 0;
        boolean[] ingredientsMatch;
        ingredientsMatch = new boolean[ingredients.length];
        // if the same category add the equivalent pertinence
        if ((recipe.getCategory().getName()).equals(category.getName())) {
            p = p + this.categoriePertinence;
        }

        // verifying which ingredient is present
        Ingredient[] listRecipeIngredient = recipe.getIngredientArray();
        //System.out.println("the size of ingredient is: "+listRecipeIngredient.length);
        for (int i = 0; i < ingredients.length; i++) {
            boolean start = true;
            int index = 0;

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
        // not sure if this part is really needed
        if (p > this.highestPertinence) {
            this.highestPertinence = p;
        }
        if (p < this.lowestPertinence) {
            this.lowestPertinence = p;
        }


    }

    // this method sort the recipe since we already determined the highest and lowest pertinence it give us a
    // starting point
    public void sortRecipe() {
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
