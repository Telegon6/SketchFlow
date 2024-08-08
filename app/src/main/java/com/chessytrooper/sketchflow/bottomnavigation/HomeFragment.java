package com.chessytrooper.sketchflow.bottomnavigation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chessytrooper.sketchflow.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private RecyclerView recentSketchesRecycler;
    private RecyclerView activityOverviewRecycler;
    private FloatingActionButton createNewSketchBtn;
    private Button premiumFeaturesBtn;
    private Button settingsBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recentSketchesRecycler = view.findViewById(R.id.recent_sketches_recycler);
        activityOverviewRecycler = view.findViewById(R.id.activity_overview_recycler);
        createNewSketchBtn = view.findViewById(R.id.create_new_sketch_btn);
        premiumFeaturesBtn = view.findViewById(R.id.premium_features_btn);
        settingsBtn = view.findViewById(R.id.settings_btn);

        // Set up RecyclerViews
        setUpRecentSketchesRecycler();
        setUpActivityOverviewRecycler();

        // Set up floating button
        createNewSketchBtn.setOnClickListener(v -> createNewSketch());

        // Set up quick access buttons
        premiumFeaturesBtn.setOnClickListener(v -> openPremiumFeatures());
        settingsBtn.setOnClickListener(v -> openSettings());

        return view;
    }

    private void setUpRecentSketchesRecycler() {
        // Implement logic to fetch and display the user's recent sketches
        recentSketchesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        // Set up adapter and other logic for the recent sketches RecyclerView
    }

    private void setUpActivityOverviewRecycler() {
        // Implement logic to fetch and display the user's recent activities
        activityOverviewRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        // Set up adapter and other logic for the activity overview RecyclerView
    }

    private void createNewSketch() {
        // Navigate to the sketch creation screen
    }

    private void openPremiumFeatures() {
        // Navigate to the premium features screen
    }

    private void openSettings() {
        // Navigate to the settings screen
    }
}