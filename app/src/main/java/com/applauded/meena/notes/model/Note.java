package com.applauded.meena.notes.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note implements Parcelable {
    //Data Model
    //First class that got created and called from Main activity which is NotelistActivity here
    //cannot be attached as bundle by default
    //therefore need to be declared as Parcelable
    //A Parcelable is a way to package objects

    private static final String TAG = "Note Class :Flow";
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "timestamp")
    private String timestamp;

    public Note(String title, String content, String timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        Log.d(TAG, "Note: In Constructor");
    }

    @Ignore //because it has select application not always applicable
    public Note() {
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(timestamp);
    }
}

//Can priority flags be an int array?
//Priority flags can be optional, it is okay for them to be unassigned

/*    Parcel extends Object
    Container for a message (data and object references) that can be sent through an IBinder.
    A Parcel can contain both flattened data that will be unflattened on the other side of the
    IPC (using the various methods here for writing specific types, or the general Parcelable interface),
    and references to live android.os.IBinder objects that will result in the other side receiving a
    proxy IBinder connected with the original IBinder in the Parcel.
    Parcel is not a general-purpose serialization mechanism. This class (and the corresponding
    Parcelable API for placing arbitrary objects into a Parcel) is designed as a high-performance
    IPC transport.
    As such, it is not appropriate to place any Parcel data in to persistent storage:
    changes in the underlying implementation of any of the data in the Parcel can render older data unreadable.
    The bulk of the Parcel API revolves around reading and writing data of various types.
    There are six major classes of such functions available.
            Primitives
    The most basic data functions are for writing and reading primitive data types:
    writeByte(byte), readByte(), writeDouble(double), readDouble(), writeFloat(float), readFloat(),
    writeInt(int), readInt(), writeLong(long), readLong(), writeString(String), readString().
    Most other data operations are built on top of these.
    The given data is written and read using the endianess of the host CPU.*/

//Creator generates instances of a our Parcelable class from the Parcel