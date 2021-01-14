package com.example.jobfinder.fragments.notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jobfinder.R;
import com.example.jobfinder.adapter.NotificationRvAdapter;
import com.example.jobfinder.models.Models;

import java.util.LinkedList;


public class NotificationsFragment extends Fragment {

    private final LinkedList<Models.Notification> notificationList = new LinkedList<>();

    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.notifications_layout, container, false);

        RecyclerView notificationRv = v.findViewById(R.id.notificationRv);
        notificationRv.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false));
        NotificationRvAdapter notificationRvAdapter = new NotificationRvAdapter(requireContext(),notificationList);
        notificationRv.setAdapter(notificationRvAdapter);
        return v;
    }
}