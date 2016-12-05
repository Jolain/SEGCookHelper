package com.compilers.segcookhelper.activity;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.compilers.segcookhelper.R;
import com.compilers.segcookhelper.cookhelper.Recipe;

/**
 * Created by Weierstrass on 2016-11-23.
 */

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private final Context context;
    private final Recipe[] recettes;

    public RecipeAdapter(Context context, Recipe[] recettes) {
        super(context, R.layout.item_recipe, recettes);
        this.context = context;
        this.recettes = recettes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_recipe, parent, false);
        TextView recipeName = (TextView) rowView.findViewById(R.id.RecipeName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ImageRecipe);
        Recipe recipe = recettes[position];
        imageView.setImageResource(getImgID(recettes[position]));
        recipeName.setText(recipe.getName());

        return rowView;
    }

    private int getImgID(Recipe r) {
        Resources assets = context.getResources();
        return assets.getIdentifier(r.getImg(), "drawable", context.getPackageName());
    }
}

