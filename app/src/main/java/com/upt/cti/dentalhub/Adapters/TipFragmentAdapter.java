package com.upt.cti.dentalhub.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import com.upt.cti.dentalhub.Database.DatabaseHelper;
import com.upt.cti.dentalhub.Models.TipFragment;

public class TipFragmentAdapter extends FragmentStateAdapter {

    private static class TipFragmentData {

        private final String name;
        private final String description;
        private final int image;

        public TipFragmentData(String name, String description, int image) {

            this.name = name;
            this.description = description;
            this.image = image;

        }

        public String getName() { return name; }

        public String getDescription() { return description; }

        public int getImage() { return image; }

    }

    private List<TipFragmentData> tips;

    public TipFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {

        super(fragmentActivity);
        loadTipsFromDatabase(fragmentActivity);

    }

    private void loadTipsFromDatabase(Context context) {

        tips = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TIPS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIP_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIP_DESCRIPTION));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIP_IMAGE));
                tips.add(new TipFragmentData(name, description, image));
            }
            cursor.close();
        }

        db.close();

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        TipFragmentData tip = tips.get(position);
        return TipFragment.newInstance(tip.getName(), tip.getDescription(), tip.getImage());

    }

    @Override
    public int getItemCount() { return tips.size(); }

}