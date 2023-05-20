package com.example.project;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.project.databinding.FragmentShowAllListBinding;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Person> mValues;

    public MyItemRecyclerViewAdapter(List<Person> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentShowAllListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Person person = mValues.get(position);
        holder.itemView.setTag(person.getId()); // Add this line
        holder.mNameView.setText(person.getName());
        holder.mBirthdateView.setText(person.getBirthdate());
        holder.mPhoneNumberView.setText(person.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public final TextView mBirthdateView;
        public final TextView mPhoneNumberView;

        public ViewHolder(FragmentShowAllListBinding binding) {
            super(binding.getRoot());
            mNameView = binding.textViewName;
            mBirthdateView = binding.textViewBirthdate;
            mPhoneNumberView = binding.textViewPhoneNumber;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int personId = (int) v.getTag(); // Get the id from the tag

                    Intent intent = new Intent(v.getContext(), PersonDetailActivity.class);
                    intent.putExtra("PERSON_ID", personId); // Add the person's id to the intent

                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "', '" + mBirthdateView.getText() + "', '" + mPhoneNumberView.getText() + "'";
        }
    }
}
