package com.dwipa.user.csc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.dwipa.user.csc.Fragments.DashboardFragment;
import com.dwipa.user.csc.Fragments.HomeFragment;
import com.dwipa.user.csc.Fragments.MekFragment;
import com.dwipa.user.csc.Fragments.SaFragment;
import com.dwipa.user.csc.Fragments.UserProfileFragment;
import com.dwipa.user.csc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new UserProfileFragment();
                    break;

            }
            return loadFragment(fragment);
        }
        private boolean loadFragment(Fragment fragment) {
            //switching fragment
            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("sa_code").exists()){
                    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                    //Set default fragment
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new SaFragment());
                    fragmentTransaction.commit();
                }else if (dataSnapshot.child("mek_code").exists()){
                    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                    //Set default fragment
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new MekFragment());
                    fragmentTransaction.commit();
                }else {
                    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                    //Set default fragment
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
