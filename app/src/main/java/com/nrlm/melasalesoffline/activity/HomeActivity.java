package com.nrlm.melasalesoffline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.melasalesoffline.BuildConfig;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.fragment.ContactUsFragment;
import com.nrlm.melasalesoffline.fragment.DashBoardFragment;
import com.nrlm.melasalesoffline.fragment.HomeFragment;
import com.nrlm.melasalesoffline.fragment.ProfileFragment;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.google.android.material.navigation.NavigationView;
import com.nrlm.melasalesoffline.utils.DialogFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {
    private HomeActivity.OnBackPressedListener onBackPressedListener;
    Toolbar mtoolbar;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    TextView tbTitle;
   /* @BindView(R.id.shgNameTv)
    TextView shgNameTv;
    @BindView(R.id.mobileNumberTv)
    TextView mobileNumberTv;
    @BindView(R.id.appVersionTv)
    TextView appVersionTv;*/
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean mChangeFragment;
    private int selectedItem;
    AppSharedPreferences appSharedPreferences;
    DialogFactory dialogFactory;
    CommonRepository commonRepository;
    TextView shgNameTv, mobileNumberTv, appVersionTv;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mtoolbar = findViewById(R.id.toolbar);
        tbTitle = findViewById(R.id.tbTitle);
        appSharedPreferences = AppSharedPreferences.getsharedprefInstances(HomeActivity.this);
        dialogFactory = DialogFactory.getInstance(HomeActivity.this);
        commonRepository = CommonRepository.getInstance(HomeActivity.this);

        appSharedPreferences.setMpinCount("3");
        appSharedPreferences.setCountDownTime("");

        setupToolbar();
        setupNavigationView();
        AppUtility.getInstance().replaceFragment(getSupportFragmentManager(), DashBoardFragment.getInstance(), DashBoardFragment.class.getSimpleName(), false, R.id.fragmentContainer);
      /*  handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        startHandler();*/
    }
    private void setupToolbar() {
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(null);
        //tbTitle.setText(getString(R.string.home));
    }
    private void setupNavigationView() {
        mDrawerLayout.setScrimColor(Color.parseColor("#99000000"));
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        View headerView = navigationView.getHeaderView(0);
        shgNameTv = (TextView) headerView.findViewById(R.id.shgNameTv);
        mobileNumberTv = (TextView) headerView.findViewById(R.id.mobileNumberTv);
        appVersionTv = (TextView) headerView.findViewById(R.id.appVersionTv);
        shgNameTv.setText(commonRepository.getShgName(appSharedPreferences.getShgRegId()));
        mobileNumberTv.setText(appSharedPreferences.getMobile());
        appVersionTv.setText(BuildConfig.VERSION_NAME);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtoolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                if (mChangeFragment) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    mChangeFragment = false;
                    switch (selectedItem) {
                        case R.id.menu_Home: {
                            AppUtility.getInstance().replaceFragment(getSupportFragmentManager(), DashBoardFragment.getInstance(), DashBoardFragment.class.getSimpleName(), false, R.id.fragmentContainer);
                            break;
                        }
                        case R.id.menu_profile: {
                            AppUtility.getInstance().replaceFragment(getSupportFragmentManager(), ProfileFragment.getInstance(), ProfileFragment.class.getSimpleName(), false, R.id.fragmentContainer);
                            break;
                        }
                        case R.id.menu_contact_us: {
                            AppUtility.getInstance().replaceFragment(getSupportFragmentManager(), ContactUsFragment.getInstance(), ContactUsFragment.class.getSimpleName(), false, R.id.fragmentContainer);
                            break;
                        }
                        case R.id.menu_history: {
                            Intent intent = new Intent(HomeActivity.this, BillHistoyActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case R.id.menu_logout: {
                            appSharedPreferences.clearAllKeyPref();
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        }
                        case R.id.menu_notification: {
                            Intent intent = new Intent(HomeActivity.this, SalesDetailActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                AppUtility.hideSoftKeyboard(HomeActivity.this);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportFragmentManager().addOnBackStackChangedListener(this::onBackStackChanged);
    }

    public void setToolBarTitle(String toolBarTitle) {
        tbTitle.setText(toolBarTitle);
        assert getSupportActionBar() != null;
        getSupportActionBar().show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AppUtility.getInstance().showLog("menuItem" + item, HomeActivity.class);
        item.setChecked(!item.isChecked());
        selectedItem = item.getItemId();
        AppUtility.getInstance().showLog("selectedItem" + selectedItem, HomeActivity.class);
        mDrawerLayout.closeDrawers();
        mChangeFragment = true;
        return true;
    }


    @Override
    public void onBackStackChanged() {

    }

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (onBackPressedListener != null) {
                onBackPressedListener.doBack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onUserInteraction() {            //kill application after 30 min when user not intecting with screen
        super.onUserInteraction();
       // stopHandler();
       // startHandler();
    }

    public void stopHandler() {
        handler.removeCallbacks(runnable);
    }

    public void startHandler() {
        handler.postDelayed(runnable,  5000);
    }
}