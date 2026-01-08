package edu.urv.lifeify;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import edu.urv.lifeify.model.ModObjective;
import edu.urv.lifeify.placeholder.ObjectivesActivity;
import edu.urv.lifeify.placeholder.PlaceholderContent.PlaceholderItem;
import edu.urv.lifeify.databinding.ObjectiveItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyObjectiveRecyclerViewAdapter extends RecyclerView.Adapter<MyObjectiveRecyclerViewAdapter.ViewHolder> {

    private final List<ModObjective> mValues;
    private ObjectivesActivity activity;


    public MyObjectiveRecyclerViewAdapter(List<ModObjective> items, Activity activity) {
        mValues = items;
        this.activity= (ObjectivesActivity)activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(ObjectiveItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).content);
        if(holder.mItem.type == ModObjective.ObjType.NUMBER){
            holder.mText.setVisibility(View.VISIBLE);
            holder.mCheck.setVisibility(View.INVISIBLE);
        }else{
            holder.mText.setVisibility(View.INVISIBLE);
            holder.mCheck.setVisibility(View.VISIBLE);
        }

        if (holder.textWatcher == null) {
            holder.textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String s1 = s.toString();
                    if (s1.trim().isEmpty()) {
                        holder.mItem.act_value = 0;
                    } else {
                        holder.mItem.act_value = Integer.parseInt(s1);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                // No logic needed here (if you don't need it)
                }
            };
            holder.mText.addTextChangedListener(holder.textWatcher);
        }

        if(holder.checkWatcher==null) {
            holder.checkWatcher = new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Update the item's checked state when the user changes it
                    holder.mItem.b_value = isChecked;
                    Log.i("info","checkbox have been pressed");
                }
            } ;
            holder.mCheck.setOnCheckedChangeListener(holder.checkWatcher);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;

        public final EditText mText;

        public final CheckBox mCheck;

        public TextWatcher textWatcher = null;

        public CompoundButton.OnCheckedChangeListener checkWatcher = null;

        public ModObjective mItem;



        public ViewHolder(ObjectiveItemBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
            mText = binding.txtObjective;
            mCheck = binding.checkboxObjective;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}