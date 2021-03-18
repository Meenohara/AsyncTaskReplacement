package com.applauded.meena.notes.rxjava;

import com.applauded.meena.notes.model.Note;
import com.applauded.meena.notes.persistence.NoteDao;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxOperations {

    NoteDao noteDao;

    public RxOperations(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public void insertNotes(Note note)
    {

        Observable.fromCallable(() -> {
            noteDao.insertNotesRx(note);
            return true;
            // RxJava does not accept null return value. Null will be treated as a failure.
            // So just make it return true
        })  //Execute in IO thread, i.e. background thread
                .subscribeOn(Schedulers.io())
                //report or post the result to main thread
                .observeOn(AndroidSchedulers.mainThread())
                // execute this RxJava
                .subscribe();
    }

    public void notesRx(Note note, String mode)
    {
        Observable.fromCallable(() -> {
            switch (mode)
            {
                case "insert": noteDao.insertNotesRx(note); break;
                case "update": noteDao.updateNotesRx(note); break;
                case "delete": noteDao.deleteNotesRx(note); break;
                 default: break;
            }
            return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

}

//Should I use just or creat operator here and the note is an observable in this project?
//what is subscribeOn?
//what is observeon?
//What are we exactly doing here?

/*
    //create Observable (method will not execute yet)
    Observable<Task> callable = Observable
            .fromCallable(new Callable<Task>() {
                @Override
                public Task call() throws Exception {
                    return null;
                    //return MyDB.getTask();
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());*/