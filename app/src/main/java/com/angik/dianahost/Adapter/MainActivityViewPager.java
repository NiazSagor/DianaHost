package com.angik.dianahost.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.angik.dianahost.MainActivityFragment.AccountFragment;
import com.angik.dianahost.MainActivityFragment.ContactFragment;
import com.angik.dianahost.MainActivityFragment.HomeFragment;
import com.angik.dianahost.MainActivityFragment.OfferFragment;
import com.angik.dianahost.MainActivityFragment.TicketFragment;

public class MainActivityViewPager extends FragmentStateAdapter {


    public MainActivityViewPager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OfferFragment();
            case 1:
                return new ContactFragment();
            case 2:
                return new HomeFragment();
            case 3:
                return new TicketFragment();
            case 4:
                return new AccountFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
