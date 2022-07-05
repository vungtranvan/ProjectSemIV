package com.example.projectsemiv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectsemiv.activity.UpdateAccountActivity;
import com.example.projectsemiv.fragment.HistoryFragment;
import com.example.projectsemiv.fragment.HomeFragment;
import com.example.projectsemiv.fragment.ManagerAccountFragment;
import com.example.projectsemiv.fragment.ManagerQuestionFragment;
import com.example.projectsemiv.helper.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int Fragment_Home = 0;
    private static final int Fragment_Manager_Question = 1;
    private static final int Fragment_Manager_Account = 2;
    private static final int Fragment_History = 3;
    private int mCurrentFragment = Fragment_Home;

    private DrawerLayout mdrawerLayout;
    private SessionManager sessionManager;
    private TextView txtUserNameLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        if (!sessionManager.checkLogin()) {
            redirectLoginActivity();
        }

        mdrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mdrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        if (!sessionManager.getRoleUserInSession()) {
            navigationView.getMenu().findItem(R.id.nav_history).setVisible(false);
            navigationView.getMenu().findItem(R.id.manager_account_nav).setVisible(false);
            navigationView.getMenu().findItem(R.id.manager_question_nav).setVisible(false);
        }
        View headerView = navigationView.getHeaderView(0);
        HashMap<String, String> userInfo = sessionManager.getUserInfoInSession();
        txtUserNameLogged = headerView.findViewById(R.id.txtUserNameLogged);
        txtUserNameLogged.setText(userInfo.get(SessionManager.KEY_NAME_DISPLAY_USER));
        String imgAccLogged = userInfo.get(SessionManager.KEY_USER_IMAGE);
        if (imgAccLogged != null) {
            Glide.with(MainActivity.this).load(imgAccLogged).into((CircleImageView) headerView.findViewById(R.id.imgUserLogged));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sessionManager.checkLogin()) {
            redirectLoginActivity();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                if (mCurrentFragment != Fragment_Home) {
                    replaceFragment(new HomeFragment());
                    mCurrentFragment = Fragment_Home;
                }
                break;
            case R.id.manager_question_nav:
                if (mCurrentFragment != Fragment_Manager_Question) {
                    replaceFragment(new ManagerQuestionFragment());
                    mCurrentFragment = Fragment_Manager_Question;
                }
                break;
            case R.id.manager_account_nav:
                if (mCurrentFragment != Fragment_Manager_Account) {
                    replaceFragment(new ManagerAccountFragment());
                    mCurrentFragment = Fragment_Manager_Account;
                }
                break;
            case R.id.nav_history:
                if (mCurrentFragment != Fragment_History) {
                    replaceFragment(new HistoryFragment());
                    mCurrentFragment = Fragment_History;
                }
                break;
            case R.id.nav_my_profile:
                Intent intent = new Intent(MainActivity.this, UpdateAccountActivity.class);
                intent.putExtra("idAcc", sessionManager.getUserIdInSession());
                startActivity(intent);
                break;
            case R.id.nav_logout:
                dialogLogout();
                break;
        }
        mdrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerLayout.isDrawerOpen(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private void redirectLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void dialogLogout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.notification);
        builder.setMessage(R.string.notification_logout);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sessionManager.logout();
                redirectLoginActivity();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

}