package com.applauded.meena.notes.async;

import android.os.AsyncTask;
import android.util.Log;

import com.applauded.meena.notes.model.Note;
import com.applauded.meena.notes.persistence.NoteDao;


//TODO replace async tasks with RxJava

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

    private static final String TAG="InsertAsyncTask : Flow";

    private NoteDao mNoteDao;

    public InsertAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) { //ellipsis denotes an optional list
        //insertAsyncTask called in NoteRepository has execute function, note passed there is what
        //get passed here in doInBackground. In execute() we are passing a list of one note
        Log.d(TAG, "doInBackground: InsertAsyncTask thread "+ Thread.currentThread().getName());
        mNoteDao.insertNotes(notes);
        return null;
        //As a result type (a function with a return value of type Void) it means that the function
        // *always * return null (it cannot return anything other than null,
        // because Void has no instances).
        //The most common use of Void is for reflection,
        // but that is not the only place where it may be used.
    }
}
