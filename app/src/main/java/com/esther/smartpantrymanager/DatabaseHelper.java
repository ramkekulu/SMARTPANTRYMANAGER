package com.esther.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // ============================================================
    // DATABASE INFORMATION
    // ============================================================

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // ============================================================
    // PANTRY TABLE
    // ============================================================

    public static final String TABLE_PANTRY = "pantry";

    public static final String PANTRY_ID = "id";
    public static final String PANTRY_NAME = "ingredient_name";
    public static final String PANTRY_QUANTITY = "quantity";
    public static final String PANTRY_UNIT = "unit";
    public static final String PANTRY_EXPIRY = "expiry_date";

    // ============================================================
    // RECIPES TABLE
    // ============================================================

    public static final String TABLE_RECIPES = "recipes";

    public static final String RECIPE_ID = "id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_METHOD = "method";

    // ============================================================
    // RECIPE INGREDIENTS TABLE
    // ============================================================

    public static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String RECIPE_INGREDIENT_ID = "id";
    public static final String RECIPE_INGREDIENT_RECIPE_ID = "recipe_id";
    public static final String RECIPE_INGREDIENT_NAME =
            "ingredient_name";
    public static final String RECIPE_REQUIRED_QUANTITY =
            "required_quantity";
    public static final String RECIPE_INGREDIENT_UNIT = "unit";

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ============================================================
    // CREATE DATABASE TABLES
    // ============================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // --------------------------------------------------------
        // PANTRY TABLE
        // --------------------------------------------------------

        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PANTRY_NAME + " TEXT NOT NULL, " +
                        PANTRY_QUANTITY + " REAL NOT NULL, " +
                        PANTRY_UNIT + " TEXT NOT NULL, " +
                        PANTRY_EXPIRY + " TEXT" +
                        ")";

        db.execSQL(createPantryTable);

        // --------------------------------------------------------
        // RECIPES TABLE
        // --------------------------------------------------------

        String createRecipesTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RECIPE_NAME + " TEXT NOT NULL, " +
                        RECIPE_METHOD + " TEXT NOT NULL" +
                        ")";

        db.execSQL(createRecipesTable);

        // --------------------------------------------------------
        // RECIPE INGREDIENTS TABLE
        // --------------------------------------------------------

        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        RECIPE_INGREDIENT_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RECIPE_INGREDIENT_RECIPE_ID +
                        " INTEGER NOT NULL, " +

                        RECIPE_INGREDIENT_NAME +
                        " TEXT NOT NULL, " +

                        RECIPE_REQUIRED_QUANTITY +
                        " REAL NOT NULL, " +

                        RECIPE_INGREDIENT_UNIT +
                        " TEXT NOT NULL, " +

                        "FOREIGN KEY (" +
                        RECIPE_INGREDIENT_RECIPE_ID +
                        ") REFERENCES " +
                        TABLE_RECIPES +
                        "(" + RECIPE_ID + ")" +
                        ")";

        db.execSQL(createRecipeIngredientsTable);

        // --------------------------------------------------------
        // PRELOAD RECIPES
        // --------------------------------------------------------

        seedRecipes(db);
    }

    // ============================================================
    // DATABASE UPGRADE
    // ============================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "
                + TABLE_RECIPE_INGREDIENTS);

        db.execSQL("DROP TABLE IF EXISTS "
                + TABLE_RECIPES);

        db.execSQL("DROP TABLE IF EXISTS "
                + TABLE_PANTRY);

        onCreate(db);
    }

    // ============================================================
    // PANTRY - CREATE
    // ============================================================

    public long addPantryItem(
            String ingredientName,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                PANTRY_NAME,
                normalizeIngredient(ingredientName)
        );

        values.put(PANTRY_QUANTITY, quantity);

        values.put(
                PANTRY_UNIT,
                normalizeUnit(unit)
        );

        values.put(PANTRY_EXPIRY, expiryDate);

        long id = db.insert(
                TABLE_PANTRY,
                null,
                values
        );

        db.close();

        return id;
    }

    // ============================================================
    // PANTRY - READ ALL
    // ============================================================

    public Cursor getAllPantryItems() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                PANTRY_NAME + " ASC"
        );
    }

    // ============================================================
    // PANTRY - READ ONE
    // ============================================================

    public Cursor getPantryItem(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_PANTRY,
                null,
                PANTRY_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
    }

    // ============================================================
    // PANTRY - UPDATE
    // ============================================================

    public int updatePantryItem(
            int id,
            String ingredientName,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                PANTRY_NAME,
                normalizeIngredient(ingredientName)
        );

        values.put(PANTRY_QUANTITY, quantity);

        values.put(
                PANTRY_UNIT,
                normalizeUnit(unit)
        );

        values.put(PANTRY_EXPIRY, expiryDate);

        int result = db.update(
                TABLE_PANTRY,
                values,
                PANTRY_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result;
    }

    // ============================================================
    // PANTRY - DELETE
    // ============================================================

    public int deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_PANTRY,
                PANTRY_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result;
    }

    // ============================================================
    // RECIPE - READ ALL
    // ============================================================

    public Cursor getAllRecipes() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                RECIPE_NAME + " ASC"
        );
    }

    // ============================================================
    // RECIPE - READ ONE
    // ============================================================

    public Cursor getRecipe(int recipeId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_RECIPES,
                null,
                RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );
    }

    // ============================================================
    // RECIPE INGREDIENTS
    // ============================================================

    public Cursor getRecipeIngredients(int recipeId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                RECIPE_INGREDIENT_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                RECIPE_INGREDIENT_NAME + " ASC"
        );
    }

    // ============================================================
    // STRICT MATCHING
    // ============================================================

    public boolean canMakeRecipe(int recipeId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor recipeIngredients = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                RECIPE_INGREDIENT_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );

        boolean canMake = true;

        try {

            while (recipeIngredients.moveToNext()) {

                String requiredName =
                        recipeIngredients.getString(
                                recipeIngredients.getColumnIndexOrThrow(
                                        RECIPE_INGREDIENT_NAME
                                )
                        );

                double requiredQuantity =
                        recipeIngredients.getDouble(
                                recipeIngredients.getColumnIndexOrThrow(
                                        RECIPE_REQUIRED_QUANTITY
                                )
                        );

                String requiredUnit =
                        recipeIngredients.getString(
                                recipeIngredients.getColumnIndexOrThrow(
                                        RECIPE_INGREDIENT_UNIT
                                )
                        );

                // ------------------------------------------------
                // Search pantry for this ingredient
                // ------------------------------------------------

                Cursor pantryCursor = db.query(
                        TABLE_PANTRY,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );

                boolean ingredientFound = false;

                try {

                    while (pantryCursor.moveToNext()) {

                        String pantryName =
                                pantryCursor.getString(
                                        pantryCursor.getColumnIndexOrThrow(
                                                PANTRY_NAME
                                        )
                                );

                        double pantryQuantity =
                                pantryCursor.getDouble(
                                        pantryCursor.getColumnIndexOrThrow(
                                                PANTRY_QUANTITY
                                        )
                                );

                        String pantryUnit =
                                pantryCursor.getString(
                                        pantryCursor.getColumnIndexOrThrow(
                                                PANTRY_UNIT
                                        )
                                );

                        // ----------------------------------------
                        // Compare normalized names
                        // ----------------------------------------

                        boolean nameMatches =
                                ingredientsMatch(
                                        pantryName,
                                        requiredName
                                );

                        // ----------------------------------------
                        // Compare quantities
                        // ----------------------------------------

                        boolean quantityMatches =
                                pantryQuantity >= requiredQuantity;

                        // ----------------------------------------
                        // Compare units
                        // ----------------------------------------

                        boolean unitMatches =
                                unitsMatch(
                                        pantryUnit,
                                        requiredUnit
                                );

                        if (nameMatches
                                && quantityMatches
                                && unitMatches) {

                            ingredientFound = true;
                            break;
                        }
                    }

                } finally {

                    pantryCursor.close();
                }

                // ------------------------------------------------
                // ONE missing ingredient means recipe is excluded
                // ------------------------------------------------

                if (!ingredientFound) {

                    canMake = false;
                    break;
                }
            }

        } finally {

            recipeIngredients.close();
        }

        db.close();

        return canMake;
    }

    // ============================================================
    // GET STRICTLY SUGGESTED RECIPES
    // ============================================================

    public List<Integer> getSuggestedRecipeIds() {

        List<Integer> suggestedRecipes =
                new ArrayList<>();

        Cursor recipes = getAllRecipes();

        try {

            while (recipes.moveToNext()) {

                int recipeId =
                        recipes.getInt(
                                recipes.getColumnIndexOrThrow(
                                        RECIPE_ID
                                )
                        );

                if (canMakeRecipe(recipeId)) {

                    suggestedRecipes.add(recipeId);
                }
            }

        } finally {

            recipes.close();
        }

        return suggestedRecipes;
    }

    // ============================================================
    // INGREDIENT NORMALIZATION
    // ============================================================

    public String normalizeIngredient(String ingredient) {

        if (ingredient == null) {
            return "";
        }

        String value =
                ingredient
                        .trim()
                        .toLowerCase(Locale.ROOT);

        // Remove extra spaces
        value = value.replaceAll("\\s+", " ");

        // Common plural forms
        if (value.endsWith("ies")) {

            value = value.substring(
                    0,
                    value.length() - 3
            ) + "y";

        } else if (value.endsWith("oes")) {

            value = value.substring(
                    0,
                    value.length() - 2
            );

        } else if (value.endsWith("s")
                && !value.endsWith("ss")) {

            value = value.substring(
                    0,
                    value.length() - 1
            );
        }

        // Common ingredient aliases
        switch (value) {

            case "tomato":
                return "tomato";

            case "potato":
                return "potato";

            case "onion":
                return "onion";

            case "pepper":
            case "bell pepper":
            case "capsicum":
                return "pepper";

            case "egg":
                return "egg";

            case "bread":
                return "bread";

            case "chicken":
            case "chicken breast":
                return "chicken";

            case "cheese":
                return "cheese";

            case "carrot":
                return "carrot";

            case "milk":
                return "milk";

            case "rice":
                return "rice";

            case "pasta":
                return "pasta";

            default:
                return value;
        }
    }

    // ============================================================
    // INGREDIENT COMPARISON
    // ============================================================

    public boolean ingredientsMatch(
            String pantryIngredient,
            String recipeIngredient) {

        String pantry =
                normalizeIngredient(pantryIngredient);

        String recipe =
                normalizeIngredient(recipeIngredient);

        return pantry.equals(recipe);
    }

    // ============================================================
    // UNIT NORMALIZATION
    // ============================================================

    public String normalizeUnit(String unit) {

        if (unit == null) {
            return "";
        }

        String value =
                unit.trim().toLowerCase(Locale.ROOT);

        switch (value) {

            case "grams":
            case "gram":
            case "g":
                return "g";

            case "kilograms":
            case "kilogram":
            case "kg":
                return "kg";

            case "millilitres":
            case "milliliters":
            case "millilitre":
            case "milliliter":
            case "ml":
                return "ml";

            case "litres":
            case "liters":
            case "litre":
            case "liter":
            case "l":
                return "l";

            case "pieces":
            case "piece":
            case "pcs":
            case "pc":
                return "piece";

            case "cups":
            case "cup":
                return "cup";

            case "tablespoons":
            case "tablespoon":
            case "tbsp":
                return "tbsp";

            case "teaspoons":
            case "teaspoon":
            case "tsp":
                return "tsp";

            default:
                return value;
        }
    }

    // ============================================================
    // UNIT COMPARISON
    // ============================================================

    public boolean unitsMatch(
            String pantryUnit,
            String recipeUnit) {

        String pantry =
                normalizeUnit(pantryUnit);

        String recipe =
                normalizeUnit(recipeUnit);

        return pantry.equals(recipe);
    }

    // ============================================================
    // SEED RECIPES
    // ============================================================

    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(
                db,
                "Egg and Tomato Toast",
                "Toast the bread. Cook the egg and tomato together in a pan. Place the mixture on the toast and serve.",
                new String[][]{
                        {"bread", "2", "piece"},
                        {"egg", "2", "piece"},
                        {"tomato", "1", "piece"}
                }
        );

        addRecipe(
                db,
                "Cheese Omelette",
                "Beat the eggs. Cook them in a pan and add cheese. Fold the omelette and serve.",
                new String[][]{
                        {"egg", "2", "piece"},
                        {"cheese", "50", "g"}
                }
        );

        addRecipe(
                db,
                "Chicken Rice",
                "Cook the rice. Cook the chicken with onion and tomato. Combine with the cooked rice and serve.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"chicken", "200", "g"},
                        {"onion", "1", "piece"},
                        {"tomato", "1", "piece"}
                }
        );

        addRecipe(
                db,
                "Tomato Pasta",
                "Cook the pasta. Fry onion and tomato, then mix with the cooked pasta.",
                new String[][]{
                        {"pasta", "200", "g"},
                        {"tomato", "2", "piece"},
                        {"onion", "1", "piece"}
                }
        );

        addRecipe(
                db,
                "Chicken Pasta",
                "Cook the pasta. Cook the chicken with onion. Mix everything together and serve.",
                new String[][]{
                        {"pasta", "200", "g"},
                        {"chicken", "200", "g"},
                        {"onion", "1", "piece"}
                }
        );

        addRecipe(
                db,
                "Vegetable Rice",
                "Cook the rice. Fry the vegetables until tender and mix them with the rice.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"carrot", "2", "piece"},
                        {"onion", "1", "piece"},
                        {"pepper", "1", "piece"}
                }
        );

        addRecipe(
                db,
                "Chicken Vegetable Rice",
                "Cook the rice. Cook chicken and vegetables separately, then combine all ingredients.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"chicken", "200", "g"},
                        {"carrot", "1", "piece"},
                        {"pepper", "1", "piece"}
                }
        );

        addRecipe(
                db,
                "Cheesy Chicken",
                "Cook the chicken thoroughly. Add cheese and cook until melted.",
                new String[][]{
                        {"chicken", "200", "g"},
                        {"cheese", "50", "g"}
                }
        );

        addRecipe(
                db,
                "Cheese Toast",
                "Place cheese on bread and toast until the cheese melts.",
                new String[][]{
                        {"bread", "2", "piece"},
                        {"cheese", "50", "g"}
                }
        );

        addRecipe(
                db,
                "Egg Fried Rice",
                "Cook the rice. Fry the egg with onion and mix with the rice.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"egg", "2", "piece"},
                        {"onion", "1", "piece"}
                }
        );

        addRecipe(
                db,
                "Chicken Tomato Toast",
                "Cook the chicken and tomato. Place the mixture between toasted bread.",
                new String[][]{
                        {"chicken", "100", "g"},
                        {"tomato", "1", "piece"},
                        {"bread", "2", "piece"}
                }
        );

        addRecipe(
                db,
                "Tomato Egg Rice",
                "Cook the rice. Cook tomato and egg together and serve over the rice.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"tomato", "1", "piece"},
                        {"egg", "2", "piece"}
                }
        );


        addRecipe(
                db,
                "Carrot Egg Rice",
                "Cook the rice and carrot. Fry the egg and combine everything.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"carrot", "1", "piece"},
                        {"egg", "2", "piece"}
                }
        );

        addRecipe(
                db,
                "Tomato Cheese Toast",
                "Place tomato and cheese on bread and toast until the cheese melts.",
                new String[][]{
                        {"bread", "2", "piece"},
                        {"tomato", "1", "piece"},
                        {"cheese", "50", "g"}
                }
        );

        addRecipe(
                db,
                "Chicken Carrot Rice",
                "Cook the rice. Cook chicken and carrot, then combine with rice.",
                new String[][]{
                        {"chicken", "200", "g"},
                        {"carrot", "1", "piece"},
                        {"rice", "200", "g"}
                }
        );

        addRecipe(
                db,
                "Pepper Cheese Omelette",
                "Beat the eggs. Add pepper and cheese and cook until the eggs are set.",
                new String[][]{
                        {"egg", "2", "piece"},
                        {"pepper", "1", "piece"},
                        {"cheese", "50", "g"}
                }
        );

        addRecipe(
                db,
                "Chicken Tomato Rice",
                "Cook the rice. Cook chicken, tomato and onion, then combine with the rice.",
                new String[][]{
                        {"chicken", "200", "g"},
                        {"tomato", "1", "piece"},
                        {"onion", "1", "piece"},
                        {"rice", "200", "g"}
                }
        );

        addRecipe(
                db,
                "Vegetable Omelette",
                "Beat the eggs. Add carrot, pepper and onion. Cook until firm.",
                new String[][]{
                        {"egg", "2", "piece"},
                        {"carrot", "1", "piece"},
                        {"pepper", "1", "piece"},
                        {"onion", "1", "piece"}
                }
        );

        addRecipe(
                db,
                "Chicken Cheese Pasta",
                "Cook the pasta and chicken. Add cheese and mix everything together.",
                new String[][]{
                        {"pasta", "200", "g"},
                        {"chicken", "200", "g"},
                        {"cheese", "50", "g"}
                }
        );

        addRecipe(
                db,
                "Full Pantry Rice Bowl",
                "Cook the rice. Cook chicken with onion, tomato, carrot and pepper. Serve over rice with cheese.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"chicken", "200", "g"},
                        {"onion", "1", "piece"},
                        {"tomato", "1", "piece"},
                        {"carrot", "1", "piece"},
                        {"pepper", "1", "piece"},
                        {"cheese", "50", "g"}
                }
        );
    }

    // ============================================================
    // ADD RECIPE
    // ============================================================

    private long addRecipe(
            SQLiteDatabase db,
            String recipeName,
            String method,
            String[][] ingredients) {

        ContentValues recipeValues =
                new ContentValues();

        recipeValues.put(
                RECIPE_NAME,
                recipeName
        );

        recipeValues.put(
                RECIPE_METHOD,
                method
        );

        long recipeId =
                db.insert(
                        TABLE_RECIPES,
                        null,
                        recipeValues
                );

        for (String[] ingredient : ingredients) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    RECIPE_INGREDIENT_RECIPE_ID,
                    recipeId
            );

            ingredientValues.put(
                    RECIPE_INGREDIENT_NAME,
                    normalizeIngredient(
                            ingredient[0]
                    )
            );

            ingredientValues.put(
                    RECIPE_REQUIRED_QUANTITY,
                    Double.parseDouble(
                            ingredient[1]
                    )
            );

            ingredientValues.put(
                    RECIPE_INGREDIENT_UNIT,
                    normalizeUnit(
                            ingredient[2]
                    )
            );

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    ingredientValues
            );
        }

        return recipeId;
    }
}

