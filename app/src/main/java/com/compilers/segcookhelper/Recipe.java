package com.compilers.segcookhelper;



import java.util.LinkedList;

/**
 * Created by Jolain Poirier on 11/16/2016.
 */

public class Recipe {

    // **** Instance variable


    private String name;

    public Recipe(String name, String cookTime, LinkedList<Category> categories, LinkedList<Ingredient> ingredients, int img, String description) {
        this.name = name;
        this.cookTime = cookTime;
        this.categories = categories;
        this.ingredients = ingredients;
        this.img = img;
        this.description = description;

    }

    private String description;
    private int img; // Maybe not Image type
    private LinkedList<Ingredient> ingredients = new LinkedList<>();
    private LinkedList<Category> categories = new LinkedList<>();
    private String cookTime;

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public LinkedList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(LinkedList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedList<Category> getCategories() {
        return categories;
    }

    public void setCategories(LinkedList<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category){
        categories.add(category);
    }


    // **** Constructor


}
