package id.my.asmith.rizalapps.views.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.MenuHorizontalModel;
import id.my.asmith.rizalapps.model.User;
import id.my.asmith.rizalapps.views.adapter.MenuHorizontalAdapter;
import id.my.asmith.rizalapps.views.adapter.MenuVerticalAdapter;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private SliderLayout mSlider;
    private RecyclerView mRecycler;
    private GridView mGrid;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public static final String[] TITLES= {"Terbaru","Free Ongkir","Grosir", "Eceran"};
    public static final Integer[] IMAGES= {R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    public static String[] gridViewStrings = {
            "Beras", "Bawang Merah", "Bawang Putih", "Cabai", "Tomat", "Rempah", "Lain-Lain"


    };

    public static int[] gridViewImages = {
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Horizontal and Vertical RecyclerView Menu
        initViews();
        populatRecyclerView();
        gridViews();

        //Slider Iklan
        mSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, String> file_maps = new HashMap<String, String>();
        file_maps.put("Discount", "https://seduhteh.files.wordpress.com/2015/07/discount.jpg");
        file_maps.put("Sale", "http://bravob.net/wp-content/uploads/2016/07/Promo-dan-Diskon-Besar-Besaran-752x440.jpg");

        for (String name : file_maps.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(MainActivity.this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mSlider.addSlider(textSliderView);


        }

        mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(4000);
        mSlider.addOnPageChangeListener(MainActivity.this);
        //End Slider Iklan


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else if (id == R.id.nav_store) {
            mDatabase.child("users/"+ getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            User users = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (!users.toko) {
                                // User is null, error out
                                Toast.makeText(MainActivity.this,
                                        "Error: Anda belum membuka toko!.",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                startActivity(new Intent(MainActivity.this, StoreActivity.class));

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Government", "getUser:onCancelled", databaseError.toException());
                        }
            });
        } else if (id == R.id.nav_wish) {
            startActivity(new Intent(MainActivity.this, WishlistActivity.class));
            //Toast.makeText(MainActivity.this, "My Wishlist", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            //Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_LONG).show();
            //bagian pengaturan silahkan di edit sesuai keinginan
        } else if (id == R.id.nav_information) {
            startActivity(new Intent(MainActivity.this, InformationActivity.class));
            //Toast.makeText(MainActivity.this, "Information", Toast.LENGTH_LONG).show();
            //information activity silahkan di edit sesuai keinginan!
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            //Toast.makeText(MainActivity.this, "About", Toast.LENGTH_LONG).show();
            //about activity silahkan di edit sesuai keinginan!
        } else if (id == R.id.nav_logout) {
            //Showing AlertDialog after clicking Button
            final AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Are you sure?");
            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
            alertDialogBuilder.setCancelable(false);
            // set positive button: Sing out
            alertDialogBuilder.setPositiveButton("logout",new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    // go to a new activity of the app
                    // onClickEvent to sing out
                    mAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });
            // set negative button: Cancel
            alertDialogBuilder.setNegativeButton("Not now",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // cancel the alert box and put a Toast to the user
                    dialog.cancel();
                    // notify to user hes press the cancel button
                    Toast.makeText(MainActivity.this, "Canceled",
                            Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show alert
            alertDialog.show();
        }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Slider item
    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //End Slider Item

    // Initialize the RecyclerView Horizontal Menu
    private void initViews() {
        mRecycler = (RecyclerView)
                findViewById(R.id.recycler_view);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

    }


    // populate the list view by adding data to arraylist
    private void populatRecyclerView() {
        ArrayList<MenuHorizontalModel> arrayList = new ArrayList<>();
        for (int i = 0; i < TITLES.length; i++) {
            arrayList.add(new MenuHorizontalModel(TITLES[i],IMAGES[i]));
        }


        MenuHorizontalAdapter adapter = new MenuHorizontalAdapter(MainActivity.this, arrayList);
        mRecycler.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();// Notify the adapter

    }
    //End Of RecyclerView Horizontal Menu

    private void gridViews(){
        mGrid = (GridView) findViewById(R.id.grid);
        mGrid.setAdapter(new MenuVerticalAdapter(this, gridViewStrings, gridViewImages));
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position == 0) {
                    Intent i = new Intent(MainActivity.this, ListActivity.class);
                    String kategori = "Beras";
                    i.putExtra("kategori", kategori);
                    startActivity(i);

                }

                if (position == 1) {
                    Intent i = new Intent(MainActivity.this, ListActivity.class);
                    String kategori = "Bawang Merah";
                    i.putExtra("kategori", kategori);
                    startActivity(i);
                }

                if (position == 2) {
                    Intent i = new Intent(MainActivity.this, ListActivity.class);
                    String kategori = "Bawang Putih";
                    i.putExtra("kategori", kategori);
                    startActivity(i);
                }

                if (position == 3) {
                    Intent i = new Intent(MainActivity.this, ListActivity.class);
                    String kategori = "Cabai";
                    i.putExtra("kategori", kategori);
                    startActivity(i);
                }

                if (position == 4) {
                    Intent i = new Intent(MainActivity.this, ListActivity.class);
                    String kategori = "Tomat";
                    i.putExtra("kategori", kategori);
                    startActivity(i);
                }

                if (position == 5) {
                    Intent i = new Intent(MainActivity.this, ListActivity.class);
                    String kategori = "Rempah";
                    i.putExtra("kategori", kategori);
                    startActivity(i);
                }

                if (position == 6) {
                    Intent i = new Intent(MainActivity.this, ListActivity.class);
                    String kategori = "Lain-Lain";
                    i.putExtra("kategori", kategori);
                    startActivity(i);
                }



            }
        });
    }

}
