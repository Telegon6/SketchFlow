package com.chessytrooper.sketchflow.welcome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chessytrooper.sketchflow.R;

import java.util.List;

public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.TestimonialViewHolder> {
    private List<Testimonial> testimonials;

    public TestimonialAdapter(List<Testimonial> testimonials) {
        this.testimonials = testimonials;
    }

    @NonNull
    @Override
    public TestimonialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testimonial, parent, false);
        return new TestimonialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestimonialViewHolder holder, int position) {
        Testimonial testimonial = testimonials.get(position);
        holder.nameTextView.setText(testimonial.getName());
        holder.commentTextView.setText(testimonial.getComment());
    }

    @Override
    public int getItemCount() {
        return testimonials.size();
    }

    static class TestimonialViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView commentTextView;

        TestimonialViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.testimonialNameTextView);
            commentTextView = itemView.findViewById(R.id.testimonialCommentTextView);
        }
    }
}