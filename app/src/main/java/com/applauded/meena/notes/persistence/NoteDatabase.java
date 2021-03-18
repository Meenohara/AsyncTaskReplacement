package com.applauded.meena.notes.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.applauded.meena.notes.model.Note;

@Database(entities = {Note.class}, version = 1)
//TODO learn more about version and room.schemaLocation
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes_db";

    private static NoteDatabase instance;

    static NoteDatabase getInstance(final Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class,
                    DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract NoteDao getNoteDao();
}


//any of the database calls using live data are by default asynchronous
// which means they operate on background thread
///database trxscns cannot be done on main thread using room peristence library
//it would crash causing an error