package com.upt.cti.dentalhub;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class Activity_Care extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageView leftArrow;
    private ImageView rightArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);

        viewPager.setAdapter(new CardFragmentAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {}).attach();

        // Setează listenerii pentru săgețile de navigare
        setupNavigationArrows();

        // Setează inițial vizibilitatea săgeților
        updateArrowsVisibility();
    }

    private void setupNavigationArrows() {
        leftArrow.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1));
        rightArrow.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1));

        // Adaugă un callback pentru schimbarea paginii pentru a actualiza vizibilitatea săgeților
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateArrowsVisibility();
            }
        });
    }

    private void updateArrowsVisibility() {
        int currentItem = viewPager.getCurrentItem();
        int totalItems = Objects.requireNonNull(viewPager.getAdapter()).getItemCount();

        leftArrow.setVisibility(currentItem > 0 ? View.VISIBLE : View.INVISIBLE);
        rightArrow.setVisibility(currentItem < totalItems - 1 ? View.VISIBLE : View.INVISIBLE);
    }
}
