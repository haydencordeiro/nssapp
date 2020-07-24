package com.test.nss.ui.work;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.TestAdapter;

import java.util.ArrayList;
import java.util.List;


public class WorkDetailsFirstFrag extends Fragment {
    View root;
    ListView compHours;
    FragmentManager fm;
    List<AdapterDataWork> workListData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_work_details_first, container, false);
        workListData = firstHalfWorkData();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewWork = root.findViewById(R.id.firstWorkRec);

        WorkDataAdapter adapterWork = new WorkDataAdapter(workListData, requireContext());
        recyclerViewWork.setHasFixedSize(true);
        recyclerViewWork.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewWork.setAdapter(adapterWork);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fm = requireActivity().getSupportFragmentManager();
        fm.popBackStack("WorkFrag", 0);
    }

    public List<AdapterDataWork> firstHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        TestAdapter m = new TestAdapter(requireContext());
        m.createDatabase();
        m.open();
        int areaComp = m.getSumHours("First Year Area Based");
        int clgComp = m.getSumHours("First Year College");
        int univComp = m.getSumHours("First Year University");

        String areaRemHours;
        String univRemHours;
        String clgRemHours;

        if (areaComp > 1 && 80 - areaComp > 0)
            areaRemHours = String.valueOf(80 - areaComp);
        else
            areaRemHours = "00";

        if (clgComp > 1 && 20 - clgComp > 0)
            clgRemHours = String.valueOf(20 - clgComp);
        else
            clgRemHours = "00";

        if (univComp > 0 && 20 - univComp > 0)
            univRemHours = String.valueOf(20 - univComp);
        else
            univRemHours = "00";

        Log.e("AAA", "" + areaComp);
        m.close();


        data.add(new AdapterDataWork(getString(R.string.area), "80", String.valueOf(areaComp), areaRemHours));
        data.add(new AdapterDataWork(getString(R.string.univ), "20", String.valueOf(univComp), univRemHours));
        data.add(new AdapterDataWork(getString(R.string.clg), "20", String.valueOf(clgComp), clgRemHours));
        return data;
    }
}