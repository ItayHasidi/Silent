package com.example.silent_ver_1.ui.about;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.silent_ver_1.R;
import com.example.silent_ver_1.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {
    private AboutViewModel aboutViewModel;
    private FragmentAboutBinding binding;
    private TextView text;
    private String  wantedText = /*"This application created by Itai, Amichai and Daniel.\n" +*/
            "Have you ever had your phone ring while you were in the middle of an important meeting?\n" +
            "This application will solve your problem!\nAll you have to do is written below:\n\n" +
                    "1. Allow all the required permissions.\n" +
                    "2. You HAVE to allow manually the \"Call log\" permission\n" +
                    "3. Use \"Sync now\" after you've registered.\n" +
                    "4. Relax, you have nothing to worry about.\n\n" +
                    "Created by Itai, Amichai and Daniel.";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        text = (TextView)root.findViewById(R.id.textViewText);

        text.setText(wantedText);
        text.setMovementMethod(new ScrollingMovementMethod());




        aboutViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
