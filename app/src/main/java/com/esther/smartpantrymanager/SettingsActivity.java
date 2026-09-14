package com.esther.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private Switch expiryAlertSwitch;
    private TextView unitPreferenceText;

    private TextView navPantry;
    private TextView navRecipes;
    private TextView navSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settingsToolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        expiryAlertSwitch = findViewById(R.id.expiryAlertSwitch);
        unitPreferenceText = findViewById(R.id.unitPreferenceText);

        navPantry = findViewById(R.id.settingsNavPantry);
        navRecipes = findViewById(R.id.settingsNavRecipes);
        navSettings = findViewById(R.id.settingsNavSettings);

        /*
         * Expiry alert setting.
         *
         * This setting is stored while the app is running.
         */
        expiryAlertSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {
                        unitPreferenceText.setText(
                                "Expiry alerts are enabled"
                        );
                    } else {
                        unitPreferenceText.setText(
                                "Expiry alerts are disabled"
                        );
                    }
                }
        );

        /*
         * Pantry navigation.
         */
        navPantry.setOnClickListener(view -> {

            Intent intent = new Intent(
                    SettingsActivity.this,
                    PantryActivity.class
            );

            startActivity(intent);
        });

        /*
         * Suggested Recipes navigation.
         */
        navRecipes.setOnClickListener(view -> {

            Intent intent = new Intent(
                    SettingsActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });

        /*
         * Settings is already the current screen.
         */
        navSettings.setOnClickListener(view -> {
            // Already on Settings screen.
        });
    }
}
