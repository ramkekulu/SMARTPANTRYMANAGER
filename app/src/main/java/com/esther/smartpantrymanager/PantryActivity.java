package com.esther.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView pantryRecyclerView;
    private PantryAdapter pantryAdapter;
    private DatabaseHelper databaseHelper;

    private TextView emptyPantryText;

    private TextView pantryNavPantry;
    private TextView pantryNavRecipes;
    private TextView pantryNavSettings;

    private FloatingActionButton addPantryButton;

    private List<PantryItem> pantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry);

        // =========================================================
        // TOOLBAR
        // =========================================================

        Toolbar toolbar = findViewById(R.id.pantryToolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // =========================================================
        // DATABASE
        // =========================================================

        databaseHelper = new DatabaseHelper(this);

        // =========================================================
        // VIEWS
        // =========================================================

        pantryRecyclerView = findViewById(R.id.pantryRecyclerView);
        emptyPantryText = findViewById(R.id.emptyPantryText);
        addPantryButton = findViewById(R.id.addPantryButton);

        pantryNavPantry = findViewById(R.id.pantryNavPantry);
        pantryNavRecipes = findViewById(R.id.pantryNavRecipes);
        pantryNavSettings = findViewById(R.id.pantryNavSettings);

        // =========================================================
        // RECYCLER VIEW
        // =========================================================

        pantryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        pantryItems = new ArrayList<>();

        pantryAdapter = new PantryAdapter(
                this,
                pantryItems,
                new PantryAdapter.OnPantryItemClickListener() {

                    @Override
                    public void onEditClick(PantryItem item) {

                        Intent intent = new Intent(
                                PantryActivity.this,
                                AddEditIngredientActivity.class
                        );

                        intent.putExtra("mode", "edit");
                        intent.putExtra("id", item.getId());

                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(PantryItem item) {

                        deletePantryItem(item);
                    }
                }
        );

        pantryRecyclerView.setAdapter(pantryAdapter);

        // =========================================================
        // ADD INGREDIENT BUTTON
        // =========================================================

        addPantryButton.setOnClickListener(view -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditIngredientActivity.class
            );

            intent.putExtra("mode", "add");

            startActivity(intent);
        });

        // =========================================================
        // BOTTOM NAVIGATION - PANTRY
        // =========================================================

        pantryNavPantry.setOnClickListener(view -> {

            // Already on Pantry screen
            loadPantryItems();
        });

        // =========================================================
        // BOTTOM NAVIGATION - RECIPES
        // =========================================================

        pantryNavRecipes.setOnClickListener(view -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    RecipeDetailsActivity.class
            );

            startActivity(intent);
        });

        // =========================================================
        // BOTTOM NAVIGATION - SETTINGS
        // =========================================================

        pantryNavSettings.setOnClickListener(view -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });

        // =========================================================
        // LOAD DATA
        // =========================================================

        loadPantryItems();
    }

    // =============================================================
    // LOAD PANTRY ITEMS
    // =============================================================

    private void loadPantryItems() {

        pantryItems.clear();

        Cursor cursor = databaseHelper.getAllPantryItems();

        try {

            while (cursor.moveToNext()) {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.PANTRY_ID
                        )
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.PANTRY_NAME
                        )
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.PANTRY_QUANTITY
                        )
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.PANTRY_UNIT
                        )
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.PANTRY_EXPIRY
                        )
                );

                PantryItem item = new PantryItem(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                pantryItems.add(item);
            }

        } finally {

            cursor.close();
        }

        pantryAdapter.notifyDataSetChanged();

        // =========================================================
        // EMPTY STATE
        // =========================================================

        if (pantryItems.isEmpty()) {

            emptyPantryText.setVisibility(TextView.VISIBLE);

            pantryRecyclerView.setVisibility(RecyclerView.GONE);

        } else {

            emptyPantryText.setVisibility(TextView.GONE);

            pantryRecyclerView.setVisibility(RecyclerView.VISIBLE);
        }
    }

    // =============================================================
    // DELETE PANTRY ITEM
    // =============================================================

    private void deletePantryItem(PantryItem item) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage(
                        "Are you sure you want to delete "
                                + item.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            databaseHelper.deletePantryItem(
                                    item.getId()
                            );

                            loadPantryItems();
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }

    // =============================================================
    // REFRESH WHEN RETURNING TO SCREEN
    // =============================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {
            loadPantryItems();
        }
    }
}
