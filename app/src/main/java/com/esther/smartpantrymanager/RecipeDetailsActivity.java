package com.esther.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView recipeName;
    private TextView recipeIngredients;
    private TextView recipeInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Java to the XML layout
        setContentView(R.layout.activity_recipe_details);

        // Connect the XML TextViews to Java
        recipeName = findViewById(R.id.recipeName);
        recipeIngredients = findViewById(R.id.recipeIngredients);
        recipeInstructions = findViewById(R.id.recipeInstructions);

        // Example recipe information
        recipeName.setText("Chicken Pasta");

        recipeIngredients.setText(
                "Chicken\n" +
                        "Pasta\n" +
                        "Tomato\n" +
                        "Onion\n" +
                        "Salt"
        );

        recipeInstructions.setText(
                "1. Cook the pasta.\n\n" +
                        "2. Cook the chicken.\n\n" +
                        "3. Add the tomato and onion.\n\n" +
                        "4. Mix everything together and serve."
        );
    }
}
