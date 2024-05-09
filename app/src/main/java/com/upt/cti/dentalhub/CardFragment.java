package com.upt.cti.dentalhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CardFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_RES = "image";

    public static CardFragment newInstance(String title, String description, int imageRes) {

        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGE_RES, imageRes);
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.card_fragment, container, false);
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        ImageView imageView = view.findViewById(R.id.imageView);

        Bundle args = getArguments();
        if (args != null) {
            titleTextView.setText(args.getString(ARG_TITLE));
            descriptionTextView.setText(args.getString(ARG_DESCRIPTION));
            imageView.setImageResource(args.getInt(ARG_IMAGE_RES));
        }
        return view;

    }

}
