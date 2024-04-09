package com.example.digitaldreamteamapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText messageInput;
    private ImageView sendMessageButton;
    private RecyclerView messagesRecyclerView;
    private List<ChatMessage> messagesList = new ArrayList<>();
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);

        db = FirebaseFirestore.getInstance();

        messageInput = findViewById(R.id.messageInput);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        messagesRecyclerView = findViewById(R.id.chatRecyclerView);
        MaterialButton accessHistoryButton = findViewById(R.id.buttonAccessHistory);
        ImageView closeButton = findViewById(R.id.closeButton);

        messageAdapter = new MessageAdapter(messagesList);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messageAdapter);

        sendMessageButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                ChatMessage userMessage = new ChatMessage(messageText, true, new Timestamp(new Date()));
                messagesList.add(userMessage);
                messageAdapter.notifyItemInserted(messagesList.size() - 1);
                sendMessageToFirestore(messageText);
                messageInput.setText("");
                simulateResponse();
            }
        });

        accessHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, historyactivity.class);
            startActivity(intent);
        });

        closeButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        listenForMessagesFromFirestore();
    }

    private void simulateResponse() {
        new Handler().postDelayed(() -> {
            ChatMessage botMessage = new ChatMessage("This is a simulated response from the chatbot.", false, new Timestamp(new Date()));
            messagesList.add(botMessage);
            messageAdapter.notifyItemInserted(messagesList.size() - 1);
        }, 2000);
    }

    private void sendMessageToFirestore(String userMessage) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("prompt", userMessage);
        db.collection("dreamteam").add(messageMap)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Message sent successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error sending message", e));
    }

    private void listenForMessagesFromFirestore() {
        db.collection("dreamteam")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Listen failed.", e);
                        return;
                    }

                    messagesList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        ChatMessage message = doc.toObject(ChatMessage.class);
                        messagesList.add(message);
                    }
                    messageAdapter.notifyDataSetChanged();
                    messagesRecyclerView.scrollToPosition(messagesList.size() - 1);
                });
    }

    public static class ChatMessage {
        private String prompt;
        private boolean isUser;
        private Timestamp timestamp;

        public ChatMessage(String prompt, boolean isUser, Timestamp timestamp) {
            this.prompt = prompt;
            this.isUser = isUser;
            this.timestamp = timestamp;
        }

        // Empty constructor needed for Firestore
        public ChatMessage() {}

        // Getters
        public String getPrompt() { return prompt; }
        public boolean isUser() { return isUser; }
        public Timestamp getTimestamp() { return timestamp; }
    }

    public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
        private final List<ChatMessage> messages;

        public MessageAdapter(List<ChatMessage> messages) {
            this.messages = messages;
        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.themessage, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            ChatMessage message = messages.get(position);
            holder.messageText.setText(message.getPrompt());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class MessageViewHolder extends RecyclerView.ViewHolder {
            TextView messageText;

            public MessageViewHolder(@NonNull View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.themessage); // Make sure this ID matches your TextView in message_item2.xml
            }
        }
    }
}

