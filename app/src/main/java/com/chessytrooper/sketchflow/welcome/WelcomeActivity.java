package com.chessytrooper.sketchflow.welcome;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chessytrooper.sketchflow.R;
import com.chessytrooper.sketchflow.auth.LoginActivity;
import com.chessytrooper.sketchflow.auth.RegisterActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import java.util.Arrays;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private ExoPlayer player;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private RecyclerView featuresRecyclerView;
    private StyledPlayerView playerView;
    private TextView benefitsTitleTextView;
    private TextView benefitsContentTextView;
    private RecyclerView testimonialsRecyclerView;
    private Button signUpButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        subtitleTextView = findViewById(R.id.subtitleTextView);
        featuresRecyclerView = findViewById(R.id.featuresRecyclerView);
        playerView = findViewById(R.id.playerView);
        benefitsTitleTextView = findViewById(R.id.benefitsTitleTextView);
        benefitsContentTextView = findViewById(R.id.benefitsContentTextView);
        testimonialsRecyclerView = findViewById(R.id.testimonialsRecyclerView);
        signUpButton = findViewById(R.id.signUpButton);
        loginButton = findViewById(R.id.loginButton);

        // Set up ExoPlayer for demo video
        setupExoPlayer();

        // Set click listeners
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "Sign Up clicked", Toast.LENGTH_SHORT).show();
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show();
        });

        // Load and display features
        loadFeatures();

        // Load and display testimonials
        loadTestimonials();

        // Start animations
        startAnimations();
    }

    private void setupExoPlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri("https://example.com/demo_video.mp4");
        player.setMediaItem(mediaItem);
        player.prepare();

        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                // Handle playback error
                Toast.makeText(WelcomeActivity.this, "No Video at the moment: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadFeatures() {
        List<Feature> features = Arrays.asList(
                new Feature("Advanced Drawing Tools", "Create stunning sketches with our professional-grade tools"),
                new Feature("Cloud Sync", "Access your sketches from anywhere, anytime"),
                new Feature("Collaboration", "Work together in real-time with other artists")
        );

        FeatureAdapter adapter = new FeatureAdapter(features);
        featuresRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuresRecyclerView.setAdapter(adapter);
    }

    private void loadTestimonials() {
        List<Testimonial> testimonials = Arrays.asList(
                new Testimonial("John Doe", "SketchFlow has revolutionized my workflow!"),
                new Testimonial("Jane Smith", "The best sketching app I've ever used."),
                new Testimonial("Mike Johnson", "Incredible features and user-friendly interface.")
        );

        TestimonialAdapter adapter = new TestimonialAdapter(testimonials);
        testimonialsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        testimonialsRecyclerView.setAdapter(adapter);
    }

    private void startAnimations() {
        // Fade in animations
        ObjectAnimator titleFadeIn = ObjectAnimator.ofFloat(titleTextView, View.ALPHA, 0f, 1f);
        ObjectAnimator subtitleFadeIn = ObjectAnimator.ofFloat(subtitleTextView, View.ALPHA, 0f, 1f);
        ObjectAnimator featuresFadeIn = ObjectAnimator.ofFloat(featuresRecyclerView, View.ALPHA, 0f, 1f);
        ObjectAnimator playerFadeIn = ObjectAnimator.ofFloat(playerView, View.ALPHA, 0f, 1f);
        ObjectAnimator benefitsTitleFadeIn = ObjectAnimator.ofFloat(benefitsTitleTextView, View.ALPHA, 0f, 1f);
        ObjectAnimator benefitsContentFadeIn = ObjectAnimator.ofFloat(benefitsContentTextView, View.ALPHA, 0f, 1f);
        ObjectAnimator testimonialsFadeIn = ObjectAnimator.ofFloat(testimonialsRecyclerView, View.ALPHA, 0f, 1f);
        ObjectAnimator buttonsFadeIn = ObjectAnimator.ofFloat(signUpButton, View.ALPHA, 0f, 1f);
        ObjectAnimator buttonlFadeIn = ObjectAnimator.ofFloat(loginButton, View.ALPHA, 0f, 1f);

        // Slide up animations
        //ObjectAnimator titleSlideUp = ObjectAnimator.ofFloat(titleTextView, View.TRANSLATION_Y, 50f, 0f);
        //ObjectAnimator subtitleSlideUp = ObjectAnimator.ofFloat(subtitleTextView, View.TRANSLATION_Y, 50f, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(
                titleFadeIn,
                subtitleFadeIn,
                featuresFadeIn,
                playerFadeIn,
                benefitsTitleFadeIn,
                benefitsContentFadeIn,
                testimonialsFadeIn,
                buttonsFadeIn,
                buttonlFadeIn
        );
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}