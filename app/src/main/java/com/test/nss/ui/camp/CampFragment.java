package com.test.nss.ui.camp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.R;

public class CampFragment extends Fragment {
    View root;
    Toolbar toolbar;

    TextView camp_days;
    TextView camp_details;
    TextView camp_act;
    FragmentManager fm;
    LinearLayout camp_main_details;

    TextView toolbarTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_camp, container, false);

        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_camp));

        fm = requireActivity().getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            //fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //fm.popBackStack("CampFrag", 0);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = requireActivity().findViewById(R.id.toolbar);
        camp_days = root.findViewById(R.id.camp_days);
        camp_act = root.findViewById(R.id.camp_act);
        camp_main_details = root.findViewById(R.id.camp_main_details);
        camp_details = root.findViewById(R.id.camp_details);

        toolbar.setVisibility(View.VISIBLE);
        //camp_main_details.setVisibility(View.VISIBLE);

        camp_act.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                camp_main_details.setVisibility(View.GONE);
                fm.popBackStack();
                fm.beginTransaction().replace(R.id.camp_frag, new CampDetailsDays(), "CampDetailsDays").addToBackStack("CampFrag").commit();
            }, 350);
        });

        camp_days.setOnClickListener(view2 -> {
            new Handler().postDelayed(() -> {
                camp_main_details.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.camp_frag, new CampActListDetails(), "CampList2").addToBackStack("CampFrag").commit();
            }, 350);
        });

        camp_details.setOnClickListener(view1 -> {
            new Handler().postDelayed(() -> {
                camp_main_details.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.camp_frag, new CampDetails(), "CampDetails").addToBackStack("CampFrag").commit();
            }, 350);
        });

    }

    @Override
    public void onDetach() {
        camp_main_details.setVisibility(View.GONE);
        super.onDetach();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("CampFrag", "onDetach: " + fm.getBackStackEntryCount());
//            fm.popBackStack("CampFrag", 0);
        }
    }
}