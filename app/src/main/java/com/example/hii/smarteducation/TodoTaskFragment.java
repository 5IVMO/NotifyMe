package com.example.hii.smarteducation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodoTaskFragment extends Fragment {

    View view;

    public TodoTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_todo_task, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        String[] titles = {"Todo","Progress","Done"};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),titles.length,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;

    }


}
