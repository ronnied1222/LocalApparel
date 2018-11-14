package com.example.cmsc436.localapparel.Objects;

import android.widget.Toast;

import com.example.cmsc436.localapparel.Activities.MainPage;
import com.example.cmsc436.localapparel.Objects.Item;
import com.example.cmsc436.localapparel.Objects.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class FireBaseBackEnd {
    int id;
    FirebaseDatabase fire;
    DatabaseReference ref;
    List<User> allUsers;

    public FireBaseBackEnd(FirebaseDatabase fire){
        this.fire = fire;
        allUsers = new ArrayList<User>();

        ref = fire.getInstance().getReference();
        if(ref.child("items") != null){
            id = getItemCount();
        }else{
            id = 0;
        }

        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    User user = child.getValue(User.class);
                    allUsers.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addUser(User user){
        ref.child("users").child(user.getUid()).setValue(user);
    }

    public User getCurrentUser(final String userID){
        for(User u : allUsers){
            if(u.getUid().equals(userID)){
                return u;
            }
        }
        return null;
    }

    public void listItem(String item, String url){

        if(ref.child("items") != null){
            id = getItemCount();
        }
        ref.child("items").child(item).setValue(new Item(id,url));

    }

    public int getItemCount(){
        ref.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                id = 0;
                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    id++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return id;
    }


}


