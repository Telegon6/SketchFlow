package com.chessytrooper.sketchflow.bottomnavigation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.chessytrooper.sketchflow.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SketchpadFragment extends Fragment implements View.OnTouchListener {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private SketchPadView sketchPad;
    private ImageView currentToolIndicator;
    private int currentColor = Color.BLACK;
    private int eraserColor = Color.WHITE;
    private float currentBrushSize = 10f;
    private Tool currentTool = Tool.BRUSH;

    enum Tool { BRUSH, PENCIL, ERASER }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sketchpad, container, false);

        sketchPad = view.findViewById(R.id.sketch_pad);
        currentToolIndicator = view.findViewById(R.id.current_tool_indicator);

        ImageButton colorBtn = view.findViewById(R.id.color_btn);
        ImageButton brushBtn = view.findViewById(R.id.brush_btn);
        ImageButton pencilBtn = view.findViewById(R.id.pencil_btn);
        ImageButton eraserBtn = view.findViewById(R.id.eraser_btn);
        ImageButton saveBtn = view.findViewById(R.id.save_btn);
        ImageButton clearBtn = view.findViewById(R.id.clear_btn);
        ImageButton undoBtn = view.findViewById(R.id.undo_btn);
        ImageButton redoBtn = view.findViewById(R.id.redo_btn);

        colorBtn.setOnClickListener(v -> showColorPicker());
        brushBtn.setOnClickListener(v -> setCurrentTool(Tool.BRUSH));
        pencilBtn.setOnClickListener(v -> setCurrentTool(Tool.PENCIL));
        eraserBtn.setOnClickListener(v -> setCurrentTool(Tool.ERASER));
        saveBtn.setOnClickListener(v -> saveDrawing());
        clearBtn.setOnClickListener(v -> sketchPad.clearCanvas());
        undoBtn.setOnClickListener(v -> undo());
        redoBtn.setOnClickListener(v -> redo());

        currentToolIndicator.setOnTouchListener(this);

        return view;
    }

    private void undo() {
        boolean undone = sketchPad.undo();
        if (!undone) {
            Toast.makeText(requireContext(), "Nothing to undo", Toast.LENGTH_SHORT).show();
        }
    }

    private void redo() {
        boolean redone = sketchPad.redo();
        if (!redone) {
            Toast.makeText(requireContext(), "Nothing to redo", Toast.LENGTH_SHORT).show();
        }
    }

    private void showColorPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View colorPickerView = inflater.inflate(R.layout.color_picker_dialog, null);

        final int[] colors = {
                Color.BLACK, Color.DKGRAY, Color.GRAY, Color.LTGRAY, Color.WHITE,
                Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA
        };

        GridLayout colorGrid = colorPickerView.findViewById(R.id.color_grid);
        for (final int color : colors) {
            View colorView = new View(getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 100;
            params.height = 100;
            params.setMargins(5, 5, 5, 5);
            colorView.setLayoutParams(params);
            colorView.setBackgroundColor(color);
            colorView.setOnClickListener(v -> {
                currentColor = color;
                if (currentTool != Tool.ERASER) {
                    sketchPad.setColor(currentColor);
                }
                updateToolIndicator();
                builder.create().dismiss();
            });
            colorGrid.addView(colorView);
        }

        builder.setView(colorPickerView)
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setCurrentTool(Tool tool) {
        currentTool = tool;
        switch (tool) {
            case BRUSH:
                sketchPad.setBrushSize(currentBrushSize);
                sketchPad.setColor(currentColor);
                break;
            case PENCIL:
                sketchPad.setBrushSize(currentBrushSize / 2);
                sketchPad.setColor(currentColor);
                break;
            case ERASER:
                sketchPad.setBrushSize(currentBrushSize * 2);
                sketchPad.setColor(eraserColor);
                break;
        }
        updateToolIndicator();
    }

    private void updateToolIndicator() {
        int iconResId;
        switch (currentTool) {
            case BRUSH:
                iconResId = R.drawable.ic_brush;
                break;
            case PENCIL:
                iconResId = R.drawable.ic_pencil;
                break;
            case ERASER:
                iconResId = R.drawable.ic_eraser;
                break;
            default:
                return;
        }
        currentToolIndicator.setImageResource(iconResId);
        currentToolIndicator.setColorFilter(currentTool == Tool.ERASER ? 0 : currentColor);
        currentToolIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.current_tool_indicator) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    v.setX(event.getRawX() - v.getWidth() / 2);
                    v.setY(event.getRawY() - v.getHeight() / 2);
                    break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }

    private void saveDrawing() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveDrawingApi29AndAbove();
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
            } else {
                saveDrawingApi28AndBelow();
            }
        }
    }

    private void saveDrawingApi29AndAbove() {
        String fileName = generateFileName();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (imageUri != null) {
            try (OutputStream out = requireContext().getContentResolver().openOutputStream(imageUri)) {
                sketchPad.getDrawingBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                Toast.makeText(requireContext(), "Drawing saved successfully to gallery", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to save drawing", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDrawingApi28AndBelow() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), generateFileName());
        try (FileOutputStream out = new FileOutputStream(file)) {
            sketchPad.getDrawingBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(requireContext(), "Drawing saved to " + file.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to save drawing", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        return "SketchPad_" + timestamp + ".png";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveDrawingApi28AndBelow();
            } else {
                Toast.makeText(requireContext(), "Permission denied to save drawing", Toast.LENGTH_SHORT).show();
            }
        }
    }

}