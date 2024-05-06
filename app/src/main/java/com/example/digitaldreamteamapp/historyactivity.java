package com.example.digitaldreamteamapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class historyactivity extends AppCompatActivity {

    private ImageView closeButton;
    private Button hideShowButton;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private List<HomeActivity.ChatMessage> messagesList;
    private HomeActivity.MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historyactivity);

        closeButton = findViewById(R.id.closeButton);
        hideShowButton = findViewById(R.id.buttonHistory);
        recyclerView = findViewById(R.id.historychatRecyclerView);

        // for the Firestore
        db = FirebaseFirestore.getInstance();

        // hide/show button
        hideShowButton.setOnClickListener(v -> {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                recyclerView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        // to Close the button click listener
        closeButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(historyactivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // for the messages list and the adapter
        messagesList = new ArrayList<>();
        messageAdapter = new HomeActivity.MessageAdapter(messagesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Fetch chat messages from Firestore
        fetchChatMessages();
    }

    // this is to fetch chat messages from Firestore
    @SuppressLint("NotifyDataSetChanged")
    private void fetchChatMessages() {
        db.collection("dreamteam")
                .orderBy("createTime", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    messagesList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        HomeActivity.ChatMessage message = doc.toObject(HomeActivity.ChatMessage.class);
                        messagesList.add(message);
                    }
                    // remember this is the adapter of data changes
                    messageAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching messages", e));
    }

    // messageAdapter class and the inner MessageViewHolder class
    public static class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
        private final List<HomeActivity.ChatMessage> messages;

        public MessageAdapter(List<HomeActivity.ChatMessage> messages) {
            this.messages = messages;
        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historythemessage, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            HomeActivity.ChatMessage message = messages.get(position);
            holder.historypromptTextView.setText(message.getPrompt());
            holder.historyresponseTextView.setText(message.getResponse());

            // Debug logs for verification
            Log.d("MessageViewHolder", "Prompt Text: " + message.getPrompt());
            Log.d("MessageViewHolder", "Response Text: " + message.getResponse());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        // the inner class for MessageViewHolder
        public static class MessageViewHolder extends RecyclerView.ViewHolder {
            TextView historypromptTextView;
            TextView historyresponseTextView;

            public MessageViewHolder(@NonNull View itemView) {
                super(itemView);
                historypromptTextView = itemView.findViewById(R.id.historypromptTextView);
                historyresponseTextView = itemView.findViewById(R.id.historyresponseTextView);
            }
        }
    }
}