package com.example.chattingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


//A Fragment is a part of an activity.
//A fragment defines and manages its own layout, has its own lifecycle, and can handle its own input events.
//Fragments cannot live on their own--they must be hosted by an activity or another fragment.
public class statusFragment extends Fragment {
    @Nullable
    @Override
    //displaying the fragment xml we created and linking it with this class
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statusfragment, null);
    }
}
