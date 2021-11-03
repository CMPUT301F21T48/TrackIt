package com.example.trackit;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class userSearchAdapter extends RecyclerView.Adapter<userSearchAdapter.ViewHolder> implements Filterable {

    List<String> userNames;
    List<String> userNamesComplete;
    private OnEntryClickListener mOnEntryClickListener;

    public interface OnEntryClickListener{
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener (OnEntryClickListener onEntryClickListener){
        mOnEntryClickListener = onEntryClickListener;
    }

    public userSearchAdapter(List<String> userNamesR){
        this.userNames = userNamesR;
        this.userNamesComplete = userNamesR;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.userdisplay_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText(userNames.get(position));

    }

    public Filter getFilter(){
        return filter;
    }

    Filter filter = new Filter() { // filter is a problem area because it doesnt seem to execute filter results
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            Log.d(TAG, charSequence.toString());
            List<String> filteredResults = new ArrayList<>();
            Integer count = 0;
            if(charSequence.toString().isEmpty()){
                filteredResults.addAll(userNamesComplete);

                Log.d(TAG, charSequence.toString());
            }
            else{
                for(String check : userNamesComplete){
                    if(check.toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredResults.add(check);
                        Log.d(TAG, "performFiltering:  " + count++);
                    }
                    Log.d(TAG, "performFiltering:  " + count++);
                }
            }
            for(String check: filteredResults){
                Log.d(TAG, check);
            }
            FilterResults filtered = new FilterResults();
            filtered.values = filteredResults;
            return  filtered;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            userNames.clear();
            Log.d(TAG, "publishResults: Cleared");
            userNames.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return userNames.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            userName = itemView.findViewById(R.id.userName);
        }

        @Override
        public void onClick(View view) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(view, getLayoutPosition());
            }
        }
    }
}
