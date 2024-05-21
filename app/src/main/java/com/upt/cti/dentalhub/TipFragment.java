package com.upt.cti.dentalhub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TipFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE = "image";

    public static TipFragment newInstance(String name, String description, int image) {

        TipFragment fragment = new TipFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGE, image);
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_tip_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        ImageView imageView = view.findViewById(R.id.imageViewDevice);

        Bundle args = getArguments();
        if (args != null) {
            titleTextView.setText(args.getString(ARG_NAME));
            descriptionTextView.setText(args.getString(ARG_DESCRIPTION));
            imageView.setImageResource(args.getInt(ARG_IMAGE));
        }

    }

}