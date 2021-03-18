package com.applauded.meena.notes.async;

import android.os.AsyncTask;
import android.util.Log;

import com.applauded.meena.notes.model.Note;
import com.applauded.meena.notes.persistence.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private static final String TAG="DeleteAsyncTask : Flow";

    private NoteDao mNoteDao;

    public DeleteAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) { //ellipsis denotes an optional list
        //insertAsyncTask called in NoteRepository has execute function, note passed there is what
        //get passed here in doInBackground. In excecute() we are passing a list of one note
        Log.d(TAG, "doInBackground: DeleteAsyncTask thread "+ Thread.currentThread().getName());
        mNoteDao.deleteNotes(notes);
        return null;
    }
}