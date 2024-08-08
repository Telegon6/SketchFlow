package com.chessytrooper.sketchflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.chessytrooper.sketchflow.bottomnavigation.HomeFragment;
import com.chessytrooper.sketchflow.bottomnavigation.ProfileFragment;
import com.chessytrooper.sketchflow.bottomnavigation.SettingsFragment;
import com.chessytrooper.sketchflow.bottomnavigation.SketchpadFragment;
import com.chessytrooper.sketchflow.sidenavigation.AdvancedToolsFragment;
import com.chessytrooper.sketchflow.sidenavigation.CollaborationFragment;
import com.chessytrooper.sketchflow.sidenavigation.NotificationSettingsFragment;
import com.chessytrooper.sketchflow.sidenavigation.RecentWorkFragment;
import com.chessytrooper.sketchflow.welcome.WelcomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SketchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView headerNameTextView, headerEmailTextView;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        headerNameTextView = headerView.findViewById(R.id.header_name);
        headerEmailTextView = headerView.findViewById(R.id.header_email);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new RecentWorkFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_recent_work);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Load the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

        updateNavHeaderWithUserData();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.navigation_sketchpad) {
            selectedFragment = new SketchpadFragment();
        } else if (itemId == R.id.navigation_profile) {
            selectedFragment = new ProfileFragment();
        } else if (itemId == R.id.navigation_settings) {
            selectedFragment = new SettingsFragment();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth != null && firebaseAuth.getCurrentUser() != null) {
                firebaseAuth.signOut();
                // Update UI or navigate to login screen
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Firebase Authentication instance or current user is null
                // Handle this case, e.g., display an error message to the user
                Toast.makeText(this, "Unable to sign out. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
        }

        return true;
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_recent_work) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new RecentWorkFragment()).commit();
        } else if (id == R.id.nav_notification_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new NotificationSettingsFragment()).commit();
        } else if (id == R.id.nav_advanced_tools) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new AdvancedToolsFragment()).commit();
        } else if (id == R.id.nav_collaboration) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new CollaborationFragment()).commit();
        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new ProfileFragment()).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void updateNavHeaderWithUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userId);
            String email = currentUser.getEmail();

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        if (username != null && !username.isEmpty()) {
                            headerNameTextView.setText(username);
                        } else {
                            headerNameTextView.setText(email);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Toast.makeText(SketchActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });

            if (email != null) {
                headerEmailTextView.setText(email);
            } else {
                headerEmailTextView.setText("Unknown mail");
            }
        } else {
            headerNameTextView.setText(R.string.app_name);
            headerEmailTextView.setText("Unknown email");
        }
    }
}