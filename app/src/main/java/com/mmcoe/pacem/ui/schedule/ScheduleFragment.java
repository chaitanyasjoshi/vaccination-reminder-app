package com.mmcoe.pacem.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mmcoe.pacem.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ScheduleFragment extends Fragment {
    private ExpandableListViewAdapter expandableListViewAdapter;
    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        ExpandableListView expandableListView = root.findViewById(R.id.expandableVaccineView);
        listDataGroup = new ArrayList<>();
        listDataChild = new HashMap<>();
        expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(), listDataGroup, listDataChild);
        expandableListView.setAdapter(expandableListViewAdapter);
        initListData();
        return root;
    }

    private void initListData() {

        // Adding group data
        listDataGroup.add("At Birth");
        listDataGroup.add("6 Weeks");
        listDataGroup.add("10 Weeks");
        listDataGroup.add("14 Weeks");
        listDataGroup.add("6 Months");
        listDataGroup.add("9 Months");
        listDataGroup.add("9-12 Months");
        listDataGroup.add("12 Months");
        listDataGroup.add("15 Months");
        listDataGroup.add("16-18 Months");
        listDataGroup.add("18 Months");
        listDataGroup.add("2 Years");
        listDataGroup.add("4-6 Years");

        // array of strings
        String[] array;

        array = getResources().getStringArray(R.array.vaccine_birth);
        List<String> vaccineBirth = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_week_6);
        List<String> vaccineWeek6 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_week_10);
        List<String> vaccineWeek10 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_week_14);
        List<String> vaccineWeek14 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_6);
        List<String> vaccineMonth6 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_9);
        List<String> vaccineMonth9 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_9_12);
        List<String> vaccineMonth912 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_12);
        List<String> vaccineMonth12 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_15);
        List<String> vaccineMonth15 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_16_18);
        List<String> vaccineMonth1618 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_18);
        List<String> vaccineMonth18 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_year_2);
        List<String> vaccineYear2 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_year_4_6);
        List<String> vaccineYear46 = new ArrayList<>(Arrays.asList(array));

        // Adding child data
        listDataChild.put(listDataGroup.get(0), vaccineBirth);
        listDataChild.put(listDataGroup.get(1), vaccineWeek6);
        listDataChild.put(listDataGroup.get(2), vaccineWeek10);
        listDataChild.put(listDataGroup.get(3), vaccineWeek14);
        listDataChild.put(listDataGroup.get(4), vaccineMonth6);
        listDataChild.put(listDataGroup.get(5), vaccineMonth9);
        listDataChild.put(listDataGroup.get(6), vaccineMonth912);
        listDataChild.put(listDataGroup.get(7), vaccineMonth12);
        listDataChild.put(listDataGroup.get(8), vaccineMonth15);
        listDataChild.put(listDataGroup.get(9), vaccineMonth1618);
        listDataChild.put(listDataGroup.get(10), vaccineMonth18);
        listDataChild.put(listDataGroup.get(11), vaccineYear2);
        listDataChild.put(listDataGroup.get(12), vaccineYear46);

        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged();
    }
}