package com.test.nss.ui.camp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.test.nss.R;

public class CampFragment extends Fragment {

    private CampViewModel campViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        campViewModel =
                ViewModelProviders.of(this).get(CampViewModel.class);
        View root = inflater.inflate(R.layout.fragment_camp, container, false);
        final TextView textView = root.findViewById(R.id.text_camp);
        campViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}