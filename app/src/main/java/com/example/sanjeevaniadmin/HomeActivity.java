package com.example.sanjeevaniadmin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sanjeevaniadmin.Fragments.AddProductsFragment;
import com.example.sanjeevaniadmin.Fragments.CategoryFragment;
import com.example.sanjeevaniadmin.Fragments.HomeFragment;
import com.example.sanjeevaniadmin.Fragments.OrdersFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int HOME_FRAGMENT = 0;
    private static final int CATEGORY_FRAGMENT = 1;
    private static final int ORDER_FRAGMENT = 2;
    private static final int ADDPRODUCT_FRAGMENT = 3;
    private int currentFragment = -1;
    private FrameLayout frameLayout;
    private static boolean viewfab = false;
    private FloatingActionButton fab, fabChat, fabaddproduct;
    private Dialog selectView;
    private TextView bannerSlider, stripadsbanner, horizontalproductlist, gridproductlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectView = new Dialog(HomeActivity.this);
        selectView.setContentView(R.layout.select_view_type_dialog);
        selectView.setCancelable(true);
        selectView.getWindow().setBackgroundDrawable(getDrawable(R.drawable.gradintbackgoudt2));
        selectView.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bannerSlider = selectView.findViewById(R.id.bannerSliderselect);
        stripadsbanner = selectView.findViewById(R.id.Stripadsbanner);
        horizontalproductlist = selectView.findViewById(R.id.Horizontalproduct);
        gridproductlist = selectView.findViewById(R.id.gridproduct);

        bannerSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bannerIntent = new Intent(HomeActivity.this, AddBannerSliderActivity.class);
                startActivity(bannerIntent);
                selectView.dismiss();

            }
        });

        stripadsbanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stripbannerIntent = new Intent(HomeActivity.this, AddStripAdsActivity.class);
                startActivity(stripbannerIntent);
                selectView.dismiss();

            }
        });
        horizontalproductlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent horizantalIntent = new Intent(HomeActivity.this, AddHorizontalListActivity.class);
                startActivity(horizantalIntent);
                selectView.dismiss();
            }
        });
        gridproductlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gridIntent = new Intent(HomeActivity.this, AddGridProductList.class);
                startActivity(gridIntent);
                selectView.dismiss();
                Toast.makeText(HomeActivity.this, "add grid list", Toast.LENGTH_SHORT).show();
            }
        });


        fab = findViewById(R.id.fab);
        fabChat = findViewById(R.id.fab_chat);
        fabaddproduct = findViewById(R.id.fab_addproduct);

        fabChat.hide();
        fabaddproduct.hide();
        fabaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectView.show();
            }
        });

        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(HomeActivity.this, ChatActivity.class);
                startActivity(chatIntent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!viewfab) {
                    fabChat.show();
                    fabaddproduct.show();
                    viewfab = true;
                } else {
                    fabChat.hide();
                    fabaddproduct.hide();
                    viewfab = false;
                }

            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        frameLayout = findViewById(R.id.Home_Framelayout);
        gotoFragment("Home", new HomeFragment(), HOME_FRAGMENT);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_AdminHome) {
            gotoFragment("Home", new HomeFragment(), HOME_FRAGMENT);
        } else if (id == R.id.nav_AdminCategory) {
            gotoFragment("Category", new CategoryFragment(), CATEGORY_FRAGMENT);
        } else if (id == R.id.nav_AdminOrder) {
            gotoFragment("Orders", new OrdersFragment(), ORDER_FRAGMENT);
        } else if (id == R.id.nav_addProduct) {
            gotoFragment("Add Product", new AddProductsFragment(), ADDPRODUCT_FRAGMENT);
        } else if (id == R.id.nav_addList) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gotoFragment(String Title, Fragment fragment, int FragmentNo) {
        invalidateOptionsMenu();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(Title);
        setFragment(fragment, FragmentNo);
    }

    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.commit();
            if (fragmentNo == HOME_FRAGMENT) {
                fab.show();
            } else {
                fab.hide();
            }

        }
    }
}
