package com.example.mynotekeeper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<NoteInfo> mNoteInfo;

    public NoteAdapter(Context context, List<NoteInfo> noteInfo) {
        mContext = context;
        mNoteInfo = noteInfo;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            NoteInfo note = mNoteInfo.get(position);
            holder.mTextCourse.setText(note.getCourse().getTitle());
            holder.mTextBody.setText(note.getTitle());
            holder.mId = note.getId();
    }

    @Override
    public int getItemCount() {
        return mNoteInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextCourse;
        public TextView mTextBody;
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextCourse = itemView.findViewById(R.id.textCourse);
             mTextBody = itemView.findViewById(R.id.textTitle);


             itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(mContext, NoteActivity.class);
            intent.putExtra(Constants.NOTE_INFO, mId);
            mContext.startActivity(intent);
        }
    }
}
