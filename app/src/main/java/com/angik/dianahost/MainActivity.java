package com.angik.dianahost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.angik.dianahost.Adapter.MainActivityViewPager;
import com.angik.dianahost.MainActivityFragment.AboutUsFragment;
import com.angik.dianahost.MainActivityFragment.ContactFragment;
import com.angik.dianahost.Utility.AlertDialogUtility;
import com.angik.dianahost.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private MenuItem prevMenuItem;

    private OnBackPressedListener listener;

    public interface OnBackPressedListener {
        void onBackPressed();
    }

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnNavigationItemSelectedListener(mNavListener);

        binding.bottomNavigation.setBackground(null);

        binding.navView.setNavigationItemSelectedListener(this);

        binding.toolbar.setOnClickListener(toolbarOnClickListener);

        binding.floatingHomeButton.setOnClickListener(floatingHomeButtonClickListener);

        binding.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!MyApp.IS_CONNECTED) {
            AlertDialogUtility.showAlertDialog(this);
        }

        setupViewPager();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    if (binding.fragmentContainer.getVisibility() == View.VISIBLE)
                        hideFrameContainer();

                    switch (menuItem.getItemId()) {
                        case R.id.nav_offer:
                            binding.viewpager.setCurrentItem(0, true);
                            break;

                        case R.id.nav_contact:
                            makeCallIntent();
                            break;

                        case R.id.nav_ticket:
                            binding.viewpager.setCurrentItem(3, true);
                            break;

                        case R.id.nav_account:
                            binding.viewpager.setCurrentItem(4, true);
                            break;
                    }
                    return false;
                }
            };

    private final View.OnClickListener floatingHomeButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            binding.viewpager.setCurrentItem(2, true);
        }
    };

    private final View.OnClickListener toolbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.isDrawerOpen(GravityCompat.START);
            }
        }
    };

    private void setupViewPager() {
        MainActivityViewPager adapter = new MainActivityViewPager(getSupportFragmentManager(), getLifecycle());

        binding.viewpager.setUserInputEnabled(false);

        binding.viewpager.setOffscreenPageLimit(1);

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    binding.bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = binding.bottomNavigation.getMenu().getItem(position);
            }
        });

        binding.viewpager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (binding.viewpager.getCurrentItem() == 0) {
            exitApp();
        } else if (binding.fragmentContainer.getVisibility() == View.VISIBLE) {
            hideFrameContainer();
        } else {
            listener.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about_us:
                loadFragment("About Us");
                break;
            case R.id.contact:
                loadFragment("Contact");
                break;
            case R.id.email:
                composeEmail("support@dianahost.com", "DianaHost Support");
                break;
        }

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void hideFrameContainer() {
        binding.fragmentContainer.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_right_out));
        binding.fragmentContainer.setVisibility(View.GONE);
    }

    private void makeCallIntent() {
        String phone = "+8801985771771";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    private void loadFragment(String page) {

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        binding.fragmentContainer.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_left_in));
        binding.fragmentContainer.setVisibility(View.VISIBLE);

        if (page.equals("About Us")) {
            transaction.replace(R.id.fragment_container, new AboutUsFragment());
        } else if (page.equals("Contact")) {
            transaction.replace(R.id.fragment_container, new ContactFragment());
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void composeEmail(String addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", addresses, null));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void exitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finishAffinity();
                    }
                }).show();
    }
}