package com.compilers.segcookhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Weierstrass on 2016-11-23.
 */

public class RecipeAdapter extends ArrayAdapter<Recipe>{

    public RecipeAdapter(Context context, List<Recipe> recettes) {
        super(context, R.layout.recipe_container,recettes );



    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_recipe, parent, false);
        TextView recipeName = (TextView) rowView.findViewById(R.id.RecipeName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ImageRecipe);


        Recipe recipe = this.getItem(position);
        imageView.setImageResource(recipe.getImg());
        recipeName.setText(recipe.getName());


        return rowView;
    }
}

