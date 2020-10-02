package com.example.ItsAWatch.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ItsAWatch.R;
import com.example.ItsAWatch.adapters.ViewPagerAdapter;
import com.example.ItsAWatch.fragments.option.OptionFragment;
import com.example.ItsAWatch.fragments.profile.ProfileFragment;
import com.example.ItsAWatch.modeles.Options;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // ATTRIBUTS

    private Options options;
    private Fragment profile;
    private Fragment option;

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        options = new Options();

        // Recupere les éléments graphique
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.fragment_container);

        // Fixe les listener
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_option).setChecked(true);
                        break;
                }
            }
        });

        // Listener item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
                        viewPager2.setCurrentItem(0,false);
                        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
                        break;
                    case R.id.navigation_option:
                        viewPager2.setCurrentItem(1,false);
                        bottomNavigationView.getMenu().findItem(R.id.navigation_option).setChecked(true);
                        break;
                    case R.id.navigation_search:
                        item.setChecked(false); // Ne fonctionne pas
                        Intent i = new Intent(getApplicationContext(),SwipeActivity.class);
                        //i.putExtra("options",options);
                        i.putExtra("options",option.getArguments().getSerializable("options"));
                        startActivity(i);
                        bottomNavigationView.getMenu().findItem(R.id.navigation_search).setChecked(true);
                        break;
                }
                return false;
            }
        });

        setupViewPager(viewPager2);
        //bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        // Fixe le fragment de départ
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
    }

    /**
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        switch (viewPager2.getCurrentItem()) {
            case 0:
                bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
                break;
            case 1:
                bottomNavigationView.getMenu().findItem(R.id.navigation_option).setChecked(true);
                break;
        }
    }

    /**
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager2 viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        profile =new ProfileFragment();
        option = OptionFragment.newInstance(options);
        //option =new OptionFragment(options);

        adapter.addFragment(profile);
        adapter.addFragment(option);

        viewPager.setAdapter(adapter);
    }
}