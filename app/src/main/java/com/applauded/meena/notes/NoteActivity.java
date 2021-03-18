package com.applauded.meena.notes;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.applauded.meena.notes.model.Note;
import com.applauded.meena.notes.persistence.NoteRepository;
import com.applauded.meena.notes.utils.Utility;

public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    private static final String TAG = "NoteActivity : Flow";

    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    //ui components
    private LinedEditText mLinedEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;
    //vars
    private boolean mIsNewNote;
    private Note mInitialNote;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;
    private Note mFinalNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mLinedEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);

        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);

        mNoteRepository = new NoteRepository(this);

        if(getIncomingIntent())
        {
          //this is a new note EDIT mode
            Log.d(TAG, "onCreate: New Note Getting Created");
            Toast.makeText(this, "New Note Getting Created", Toast.LENGTH_LONG);
            setNewNoteProperties();
            enableEditMode();
        }
        else
        {
            // this is an existing note VIEW mode
            Log.d(TAG, "onCreate: New Note Getting Created");
            //TODO should I implement try catch for Nullpointer Exception here instead of if else statement?
            setNoteProperties();
            disableContentInteraction();
        }
            setListeners();

    }
    
    private void setListeners()
    {
        mLinedEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
        mViewTitle.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);
    }

    private boolean getIncomingIntent()
    {
        if(getIntent().hasExtra("content"))
        {
           /* Note incomingNote = getIntent().getParcelableExtra("content");
            Log.d(TAG, "getIncomingIntent: "+ incomingNote.toString());*/

            mInitialNote = getIntent().getParcelableExtra("content");
// mInitialNote and mFinalNote share same object in memory
// therefore changes done to final note show in initial note
// because they share the same address
// this is what is happening when we update the notes

            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimestamp(mInitialNote.getTimestamp());
            mFinalNote.setId(mInitialNote.getId());
            Log.d(TAG, "getIncomingIntent: "+ mInitialNote.toString());

            mMode = EDIT_MODE_DISABLED;

            mIsNewNote = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewNote =true;
        return true;
    }

    private void saveChanges()
    {
        if(mIsNewNote)
        {
            Log.d(TAG, "saveChanges: Saving New Note");
            saveNewNote();
        }
        else
        {
            updateNote();
        }
    }

    private void updateNote()
    {
        mNoteRepository.updateNote(mFinalNote);
    }

    private void saveNewNote()
    {
        Log.d(TAG, "saveNewNote: Saving new Note");
        //mNoteRepository.insertNoteTask(mFinalNote);
        mNoteRepository.insertNoteRxTask(mFinalNote);
    }

    private void enableContentInteraction()
    {
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        mLinedEditText.setFocusable(true);
        mLinedEditText.setFocusableInTouchMode(true);
        mLinedEditText.setCursorVisible(true);
        mLinedEditText.requestFocus();
    }

    private void disableContentInteraction()
    {
        mLinedEditText.setKeyListener(null);
        mLinedEditText.setFocusable(false);
        mLinedEditText.setFocusableInTouchMode(false);
        mLinedEditText.setCursorVisible(false);
        mLinedEditText.clearFocus();
    }

    private void enableEditMode()
    {
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        enableContentInteraction();
    }

    private void disableEditMode()
    {
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        disableContentInteraction();

        //to check for presence of content
        String temp = mLinedEditText.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if(temp.length() > 0)
        {
            mFinalNote.setTitle(mEditTitle.getText().toString());
            mFinalNote.setContent(mLinedEditText.getText().toString());
            String timestamp = Utility.getCurrentTimestamp();
            mFinalNote.setTimestamp(timestamp);

            if(!mFinalNote.getContent().equals(mInitialNote.getContent()) ||
                    !mFinalNote.getTitle().equals(mInitialNote.getTitle()))
            {
                Log.d(TAG, "disableEditMode: New Note");
                saveChanges();
            }
        }

        Log.d(TAG, "disableEditMode: entered here");
    }

    private void hideSoftKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null)
        {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setNoteProperties() {
        Log.d(TAG, "setNoteProperties: HERE!!!");
        Log.d(TAG, "setNoteProperties: "+ mInitialNote.getTitle());
        mViewTitle.setText(mInitialNote.getTitle());
        mEditTitle.setText(mInitialNote.getTitle());
        mLinedEditText.setText(mInitialNote.getContent());
    }

    private void setNewNoteProperties()
    {
        mViewTitle.setText("Untitled");
        mEditTitle.setText("Unspecified");

        mInitialNote = new Note();
        mFinalNote = new Note();
        mInitialNote.setTitle("Initial Note Title");
        mFinalNote.setTitle("Final Note Title");

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap: double tapped ");
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(View view) {
        //is getId same as getTag
        switch (view.getId()) {
            case R.id.toolbar_check:
            {
                hideSoftKeyboard();
                disableEditMode();
                Log.d(TAG, "onClick: New Note ");
                break;
            }
            case R.id.note_text_title:
            {
                enableEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            }
            case R.id.toolbar_back_arrow:
            {
                finish();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mMode == EDIT_MODE_ENABLED)
        {
            onClick(mCheck);
        }
        else
            {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mViewTitle.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
