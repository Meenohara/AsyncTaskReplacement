package com.applauded.meena.notes.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.applauded.meena.notes.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    long[] insertNotes(Note... notes); //Note... same as Note[]
    //returning void is acceptable for insertNotes
    //array return has the indes of rows deleted
    //if nothing deleted -1 is returned

    @Insert
    long[] insertNotesRx(Note... notes);

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Update
    int updateNotes(Note... notes); //return number of rows affected

    @Update
    int updateNotesRx(Note... notes); //return number of rows affected

    @Delete
    int deleteNotes(Note... notes);//return number of rows deleted

    @Delete
    int deleteNotesRx(Note... notes);

}
