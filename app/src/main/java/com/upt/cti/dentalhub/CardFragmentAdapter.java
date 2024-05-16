package com.upt.cti.dentalhub;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CardFragmentAdapter extends FragmentStateAdapter {

    public CardFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {super(fragmentActivity);}

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return CardFragment.newInstance("Checkup", "Regular dental checkups are vital for maintaining healthy teeth and gums. Experts recommend visiting the dentist at least once every six months for a routine examination and cleaning. During the visit, your dentist will check for cavities, gum disease, and other oral health issues. They will also provide professional cleaning to remove plaque and tartar buildup. If you experience pain, sensitivity, or other changes in your oral health, schedule an appointment immediately. Early detection and treatment of dental problems can save your teeth and ensure your mouth stays healthy.", R.drawable.check_up);
            case 1:
                return CardFragment.newInstance("Brushing", "To maintain good oral hygiene, brushing your teeth correctly is essential. Start by choosing a soft-bristled toothbrush and fluoride toothpaste. Hold your brush at a 45-degree angle to your gums and use gentle circular motions to clean each tooth thoroughly. Brush for at least two minutes, covering all surfaces: the outside, inside, and chewing areas of each tooth. Don’t forget to brush your tongue to remove bacteria and freshen your breath. For optimal dental health, brush your teeth twice a day, in the morning and before bedtime.", R.drawable.brushing);
            case 2:
                return CardFragment.newInstance("Flossing", "Using dental floss is crucial for removing plaque and food particles from between your teeth, where a toothbrush can't reach. Cut about 18 inches of floss and wind most of it around one of your middle fingers, with the rest around the opposite middle finger. Hold the floss tightly between your thumbs and index fingers and gently slide it up and down between your teeth. Curve the floss around the base of each tooth, making sure you go beneath the gumline. Never snap or force the floss, as this may cut or bruise delicate gum tissue. Use clean sections of floss as you move from tooth to tooth.", R.drawable.flossing);
            case 3:
                return CardFragment.newInstance("Rinsing", "Rinsing your mouth properly is a key step in your dental care routine. Use an antimicrobial or fluoride mouthwash to enhance oral health. Pour the recommended amount of mouthwash into a cup, empty it into your mouth, and swish vigorously for 30 seconds. Make sure the liquid moves throughout your mouth and between your teeth. Spit out the mouthwash; do not swallow it. For best results, avoid eating or drinking for 30 minutes after using mouthwash so the active ingredients can work effectively.\n" + "\n", R.drawable.rinsing);
            default:
                throw new IllegalArgumentException("Invalid position");
        }

    }

    @Override
    public int getItemCount() {return 4;}

}
