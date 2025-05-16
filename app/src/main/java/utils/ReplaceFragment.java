package utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.schoolmanager.R;

public class ReplaceFragment {

    private static volatile ReplaceFragment instance;

    public static ReplaceFragment getInstance() {
        if (instance == null) {
            synchronized (ReplaceFragment.class) {
                if (instance == null) {
                    instance = new ReplaceFragment();
                }
            }
        }
        return instance;
    }

    public void replaceFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
