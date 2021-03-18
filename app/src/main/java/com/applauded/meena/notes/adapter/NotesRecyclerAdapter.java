package com.applauded.meena.notes.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applauded.meena.notes.R;
import com.applauded.meena.notes.model.Note;
import com.applauded.meena.notes.utils.Utility;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder>{

    private ArrayList<Note> mNotes = new ArrayList<>(); //datastructure for the adapter to hold the data(here notes)
            //ArrayList automatically adapts to the size, add, remove. We cannot do that with a regular array
    private OnNoteListener mOnNoteListener;

    private static final String TAG = "NotesRecycAdapter :Flow";

    public NotesRecyclerAdapter(ArrayList<Note> notes, OnNoteListener onNoteListener) {
        this.mNotes = notes;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     //called for every single entry in the list
        try
        {
            String month =  mNotes.get(position).getTimestamp().substring(0,2);
            month = Utility.getMonthFromNumber(month);
            String year = mNotes.get(position).getTimestamp().substring(3);
            String timeStamp = month + " " + year;
            holder.timestamp.setText(timeStamp);
            holder.title.setText(mNotes.get(position).getTitle());
        }catch(NullPointerException exception)
        {
            Log.e(TAG, "onBindViewHolder: NullPointerException "+ exception.getMessage());
        }

        Log.d(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        //gets called for every item in the recylerview
        Log.d(TAG, "getItemCount: ");
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title, timestamp;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);//How is this explicit view reference different? TODO
            timestamp = itemView.findViewById((R.id.note_timestamp));//Why not just findviewbyid
                                                    //Because we hve not done setcontentview?
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
            Log.d(TAG, "ViewHolder: ");
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
