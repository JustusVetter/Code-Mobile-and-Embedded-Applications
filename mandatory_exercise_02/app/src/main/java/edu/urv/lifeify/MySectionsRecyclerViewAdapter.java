package edu.urv.lifeify;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.urv.lifeify.model.ModSection;
import edu.urv.lifeify.placeholder.PlaceholderContent.PlaceholderItem;
import edu.urv.lifeify.databinding.SectionItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySectionsRecyclerViewAdapter extends RecyclerView.Adapter<MySectionsRecyclerViewAdapter.ViewHolder> {

    private final List<ModSection> mValues;
    private SectionsActivity activity;

    public MySectionsRecyclerViewAdapter(List<ModSection> items, Activity activity) {
        mValues = items;
        this.activity = (SectionsActivity)activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(SectionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).content);

        switch (holder.mItem.id) {
            case "1":
                holder.mIconSection.setImageResource(R.drawable.sections_1);
                break;
            case "2":
                holder.mIconSection.setImageResource(R.drawable.sections_2);
                break;
            case "3":
                holder.mIconSection.setImageResource(R.drawable.sections_3);
                break;
        }

        if (holder.mItem.level == 0.0) {
            holder.mIconLevel.setImageResource(R.drawable.sad_face);
        } else {
            holder.mIconLevel.setImageResource(R.drawable.happy_face);
        }

        holder.mCardView.setOnClickListener(v -> {
                /*Toast.makeText(activity.getApplicationContext(),
                        "test click!", Toast.LENGTH_LONG).show()*/
                activity.OpenObjectives(holder.mItem.id);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mIconSection;
        public final ImageView mIconLevel;
        public final CardView mCardView;
        public final TextView mContentView;
        public ModSection mItem;

        public ViewHolder(SectionItemBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
            mIconSection = binding.iconSection;
            mIconLevel = binding.iconLevel;
            mCardView = binding.cardView;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}