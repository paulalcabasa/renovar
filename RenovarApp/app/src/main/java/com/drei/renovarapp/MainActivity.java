package com.drei.renovarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.drei.renovarapp.Activities.CartActivity;
import com.drei.renovarapp.Activities.HomeActivity;
import com.drei.renovarapp.Activities.ScheduleActivity;
import com.drei.renovarapp.Adapters.ViewPagerAdapter;
import com.drei.renovarapp.Fragments.AboutFragment;
import com.drei.renovarapp.Fragments.CatalogFragment;
import com.drei.renovarapp.Fragments.FaceFragment;
import com.drei.renovarapp.Fragments.ProductsFragment;
import com.drei.renovarapp.Fragments.ServiceFragment;
import com.drei.renovarapp.Fragments.TherapyFragment;
import com.drei.renovarapp.Utils.NavigationIconClickListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_THERAPY = 1;
    CatalogFragment catalogFragment;
    TherapyFragment therapyFragment;
    FaceFragment faceFragment;
    AboutFragment aboutFragment;
    ServiceFragment serviceFragment;
    ProductsFragment productsFragment;

    Toolbar toolbar;
    FrameLayout frameLayout;
    Menu menu;

    ViewPagerAdapter adapter;

    private boolean isCart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar(getWindow().getDecorView().getRootView());
        frameLayout = findViewById(R.id.product_grid);

        setNavigation();

//        Bundle bundle = new Bundle();
//        bundle.putString("category", "1");
//        catalogFragment = new CatalogFragment();
//        catalogFragment.setArguments(bundle);
//        replaceFragment(catalogFragment);
//        toolbar.setSubtitle("Face Care");

        aboutFragment = new AboutFragment();
        replaceFragment(aboutFragment);
        toolbar.setSubtitle("About Us");

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.INTERNET,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WAKE_LOCK,
                android.Manifest.permission.SET_ALARM,
                android.Manifest.permission.VIBRATE,
                android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                android.Manifest.permission.FOREGROUND_SERVICE,
                android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        startActivity(new Intent(MainActivity.this,HomeActivity.class));
    }


    MaterialButton navFaceCare, navBodyCare, navChanged, navAbout, navService, navProductRegistration,navFace,navCollections;
    ImageView navHome;

    public void setNavigation() {
        navCollections = findViewById(R.id.navCollections);
        navFaceCare = findViewById(R.id.navFaceCare);
        navBodyCare = findViewById(R.id.navBodyCare);
        navChanged = findViewById(R.id.navChange);
        navAbout = findViewById(R.id.navAbout);
        navService = findViewById(R.id.navService);
        navFace = findViewById(R.id.navFace);
        navProductRegistration = findViewById(R.id.navProductRegistration);
        navHome= findViewById(R.id.navHome);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,HomeActivity.class));
            }
        });

        navCollections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsFragment = new ProductsFragment();
                replaceFragment(productsFragment);
                toolbar.setSubtitle("Collections");
                showHideNew(true);
                toggleNavigation();
                isCart = true;
                menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shopping_cart_black_24dp));

            }
        });

        navFaceCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "1");
                catalogFragment = new CatalogFragment();
                catalogFragment.setArguments(bundle);
                replaceFragment(catalogFragment);
                toolbar.setSubtitle("ReDefine");
                showHideNew(false);
                isCart = false;
                toggleNavigation();
            }
        });

        navBodyCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "2");
                catalogFragment = new CatalogFragment();
                catalogFragment.setArguments(bundle);
                replaceFragment(catalogFragment);
                toolbar.setSubtitle("ReGenerate");
                showHideNew(false);
                isCart = false;
                toggleNavigation();
            }
        });

        navChanged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                therapyFragment = new TherapyFragment();
                replaceFragment(therapyFragment);
                toolbar.setSubtitle("Therapies");
                showHideNew(true);
                toggleNavigation();
                isCart = false;
                menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_black_24dp));
            }
        });

        navFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceFragment = new FaceFragment();
                replaceFragment(faceFragment);
                toolbar.setSubtitle("My Face Changed");
                showHideNew(false);
                isCart = false;
                toggleNavigation();
            }
        });

        navAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutFragment = new AboutFragment();
                replaceFragment(aboutFragment);
                toolbar.setSubtitle("About Us");
                showHideNew(false);
                isCart = false;
                toggleNavigation();
            }
        });


        navService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceFragment = new ServiceFragment();
                replaceFragment(serviceFragment);
                toolbar.setSubtitle("Customer Service");
                showHideNew(false);
                isCart = false;
                toggleNavigation();
            }
        });

        navProductRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://renovarhealth.typeform.com/to/QjUmIy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                isCart = false;
                startActivity(i);
            }
        });

    }

    public void showHideNew(boolean isShown) {
        MenuItem menuItem = menu.findItem(R.id.action_new);
        menuItem.setVisible(isShown);
    }

    public void toggleNavigation() {
        ArrayList<View> outViews = new ArrayList<>();
        toolbar.findViewsWithText(outViews, "nav", View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        outViews.get(0).performClick();
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_therapy, menu);
        this.menu = menu;

        showHideNew(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_new:
                if(!isCart)
                {
                    Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    private void setUpToolbar(View view) {
        toolbar = findViewById(R.id.app_bar);
        toolbar.setNavigationContentDescription("nav");
        AppCompatActivity activity = (AppCompatActivity) MainActivity.this;
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                this,
                view.findViewById(R.id.product_grid),
                new AccelerateDecelerateInterpolator(),
                getApplicationContext().getResources().getDrawable(R.drawable.dna_menu), // Menu open icon
                getApplicationContext().getResources().getDrawable(R.drawable.ic_close_black_24dp))); // Menu close icon
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
