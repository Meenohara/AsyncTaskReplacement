package com.applauded.meena.notes.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.applauded.meena.notes.async.DeleteAsyncTask;
import com.applauded.meena.notes.async.InsertAsyncTask;
import com.applauded.meena.notes.async.UpdateAsyncTask;
import com.applauded.meena.notes.rxjava.RxOperations;
import com.applauded.meena.notes.model.Note;

import java.util.List;

public class NoteRepository {
    //technically not part of room persistence library
    //but recommended by Android documentation using Repository
    //to keep code clean and separated
    //esp if you have different data sources

    private NoteDatabase mNoteDatabase;
    private NoteDao noteDao;
    RxOperations rxOperations;

    public NoteRepository(Context context)
    {
        mNoteDatabase = NoteDatabase.getInstance(context); //reference to the database
    }

    public void insertNoteTask(Note note)
    {
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note); //we are passing a list of one note
    }

    public void insertNoteRxTask(Note note)
    {
        new RxOperations(mNoteDatabase.getNoteDao()).notesRx(note, "insert");
    }

    public void updateNote(Note note)
    {
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNoteRxTask(Note note)
    {
        new RxOperations(mNoteDatabase.getNoteDao()).notesRx(note, "update");
    }

    public LiveData<List<Note>> retrieveNotesTask()
    {
        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Note note)
    {
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void deleteNoteRxTask(Note note)
    {
        new RxOperations(mNoteDatabase.getNoteDao()).notesRx(note, "delete");
    }

}
