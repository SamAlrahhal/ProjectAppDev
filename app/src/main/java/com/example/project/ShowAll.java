package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ShowAll extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private MyItemRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;

    public ShowAll() {
    }

    public static ShowAll newInstance(int columnCount) {
        ShowAll fragment = new ShowAll();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all_list, container, false);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        updateAdapter();

        return view;
    }

    public void updateAdapter() {
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        List<Person> personList = databaseHelper.getAllPersons();
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(personList));
    }
}
