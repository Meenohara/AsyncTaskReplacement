package com.applauded.meena.notes;

import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applauded.meena.notes.adapter.NotesRecyclerAdapter;
import com.applauded.meena.notes.model.Note;
import com.applauded.meena.notes.persistence.NoteRepository;
import com.applauded.meena.notes.utils.VerticalSpacingItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NotesListActivity extends AppCompatActivity implements NotesRecyclerAdapter.OnNoteListener, View.OnClickListener{

    private static final String TAG = "NListActivity :Flow";

    //UI components
    private RecyclerView mRecyclerview; //widget

    //vars - neither view nor widget
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNotesRecyclerAdapter;

    private NoteRepository mNoteRepository;

    private ArrayList<String> jobS = new ArrayList<String>(Arrays.asList("HubSpot", "DocuSign",
           "Ultimate Software", "Google", "LinkedIn", "Khoros", "MathWorks", "NVIDIA", "Microsoft",
           "Morgan Stanley", "Goldman Sachs", "Facebook", "Square", "Compass", "SurveyMonkey",
           "SalesForce", "Kronos Incorporated", "VMware", "Adobe", "AppFolio", "SAP", "Arrow",
           "GoodWorks", "Uber", "Netflix"));
    private String[] greatPackagesnPerks = {"20 lakhs", "30 lakhs", "40 lakhs", "50 lakhs",
            "20 lakhs+a great condominium", "30laksh+world tour",
            "20 lakhs+retiral benefits till I am 100", "30 lakhs", "40 lakhs", "50 lakhs",
            "20 lakhs+a great condominium", "30laksh+world tour","20 lakhs", "30 lakhs",
            "40 lakhs", "50 lakhs", "20 lakhs+a great condominium", "30laksh+world tour","20 lakhs",
            "30 lakhs", "40 lakhs", "50 lakhs", "20 lakhs+a great condominium", "30laksh+world tour", "1 crore"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        Log.d(TAG, "onCreate: Troubleshooting");
        mRecyclerview = findViewById(R.id.recyclerview);//implicit view reference

        findViewById(R.id.fab).setOnClickListener(this);

        initRecyclerView();

        mNoteRepository = new NoteRepository(this);

        retrieveNotes();

        Log.d(TAG, "onCreate: thread: " + Thread.currentThread().getName());




        Toolbar toolbar = findViewById(R.id.notes_toolbar);
        toolbar.setTitle("Notes");
    }


    private void insertFillerNotes() {
       // for(int i=0; i<100; i++)
        for(int i=0; i<jobS.size(); i++)
        {
            Note note = new Note();
            note.setTitle("Job# "+jobS.get(i));
            //note.setContent("description #: "+i);//make an array(or arraylist?) of company names and feed here
            note.setContent("description #: "+greatPackagesnPerks[i]);//make an array(or arraylist?) of company names and feed here
            note.setTimestamp("Jan 2019");
            mNotes.add(note);
        }
            mNotesRecyclerAdapter.notifyDataSetChanged(); //TODO what this line does exactly
    }


    private void retrieveNotes()
    {
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if(mNotes.size() > 0)
                {
                    mNotes.clear();
                }
                if(notes != null)
                {
                    mNotes.addAll(notes);
                }
                mNotesRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator =
                new VerticalSpacingItemDecorator(10);
        mRecyclerview.addItemDecoration(verticalSpacingItemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerview);
        mNotesRecyclerAdapter = new NotesRecyclerAdapter(mNotes, this);
        mRecyclerview.setAdapter(mNotesRecyclerAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: clicked"+position);

        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("content", mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
       //this is for floating action button clicked.
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    private void deleteNote(Note note)
    {
        mNotes.remove(note);
        mNotesRecyclerAdapter.notifyDataSetChanged();//TODO investigate what this really does

        mNoteRepository.deleteNote(note);
    }

    /*
    androidx.recyclerview.widget.RecyclerView.Adapter<VH extends RecyclerView.ViewHolder>
    public final void notifyDataSetChanged()

    Notify any registered observers that the data set has changed.
    There are two different classes of data change events, item changes and structural changes.
    Item changes are when a single item has its data updated but no positional changes have occurred.
    Structural changes are when items are inserted, removed or moved within the data set.
    This event does not specify what about the data set has changed, forcing any observers to assume
    that all existing items and structure may no longer be valid. LayoutManagers will be forced to
    fully rebind and relayout all visible views.
    RecyclerView will attempt to synthesize visible structural change events for adapters that report
    that they have stable IDs when this method is used. This can help for the purposes of animation
    and visual object com.applauded.meena.notes.persistence but individual item views will still need to be rebound and relaid out.
    If you are writing an adapter it will always be more efficient to use the more specific change
    events if you can. Rely on notifyDataSetChanged() as a last resort.
*/

    //ItemTouchHelper.Callback is available too with more functionalities
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
                }
            };


}


//    mNoteRepository.insertNoteTask(new Note()); inserting in main thread to just check the app crashing
//    java.lang.RuntimeException: Unable to start activity
//    ComponentInfo {com.applauded.meena.notes/com.applauded.meena.notes.NotesListActivity}:
//    java.lang.RuntimeException: cannot find implementation for
//    com.applauded.meena.notes.persistence.NoteDatabase. NoteDatabase_Impl does not exist

/*
        //used just till data base is created
                //Using constructor with paramaters
        //Step1
        Note note = new Note("American Express", "Offer Package", "Dec 8 2020");
        mNotes.add(note);

        //Using empty constructor and then using setter methods
        Note note2 = new Note();
        note2.setTitle("JP Morgan Chase");
        note2.setContent("An even more better package");
        note2.setTimestamp("Dec 8 2020");
        mNotes.add(note2);
        insertFillerNotes();

        */