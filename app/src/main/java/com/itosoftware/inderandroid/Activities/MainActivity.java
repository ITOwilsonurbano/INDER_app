package com.itosoftware.inderandroid.Activities;

import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Fragments.DatePickerFragment;
import com.itosoftware.inderandroid.Fragments.DialogFragmentPqrFilter;
import com.itosoftware.inderandroid.Fragments.LinksInderFragment;
import com.itosoftware.inderandroid.Fragments.NewsFragment;
import com.itosoftware.inderandroid.Fragments.RecommendedFragment;
import com.itosoftware.inderandroid.Fragments.ReservationFragment;
import com.itosoftware.inderandroid.Fragments.SportActivitiesFragment;
import com.itosoftware.inderandroid.Fragments.SportDetailFragment;
import com.itosoftware.inderandroid.Fragments.SportsFragment;
import com.itosoftware.inderandroid.Fragments.UserAtentionFragment;
import com.itosoftware.inderandroid.R;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;
    private ActionBar actionBar;
    private TextView textView_TitleActionBar;
    public SportDetailFragment sportDetailFragment;
    private Globals globals;
    private ReservationFragment reservationFragment;
    Menu menu;
    MenuItem menuItemSearchReserva;
    MenuItem menuItemReloadMap;
    boolean siEscenarios = true;

    public ActionBar getActionBarActivity() {
        return this.actionBar;
    }

    public TextView getTextView_TitleActionBar() {
        return textView_TitleActionBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("MainActivity","MainActivityOnCreate");
        Intent intent = getIntent();
        Integer tabToOpen = intent.getIntExtra("openTab", -1);

        Bundle bundle = getIntent().getExtras();
        boolean show_news_tab = false;
        if(bundle != null){
            show_news_tab = bundle.getBoolean("show_news_tab");
        }

        if(show_news_tab == true){
            Log.d("MainActivity", "Show news tab");
            tabToOpen = 1;
        }
        else{
            Log.d("MainActivity", "Dont show news tab");
        }

        globals = (Globals) getApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globals = (Globals) getApplication();
        // Set up the action bar.
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Verde)));
//        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Blanco)));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Center the action bar title
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        textView_TitleActionBar = (TextView) viewActionBar.findViewById(R.id.title_actionbar);

        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true); // Se habilita custom para tomar el layout.
        actionBar.setDisplayShowTitleEnabled(false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                hydeSoftKeyboard();
            }
        });
        // Desabilita swiping.
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                Boolean value = false;
                if (globals.getSiSwiping()) {
                    value = false;
                } else {
                    value = true;
                }
                return value;
            }
        });

        mViewPager.setOffscreenPageLimit(7);

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {

            View tabView = getLayoutInflater().inflate(R.layout.tab, null);
            TextView tabText = (TextView) tabView.findViewById(R.id.tab_title);
            String title = (String) mSectionsPagerAdapter.getPageTitle(i);

            tabText.setText(title);
            ImageView tabImage = (ImageView) tabView.findViewById(R.id.tab_icon);
            tabImage.setImageDrawable(getResources().getDrawable(mSectionsPagerAdapter.getIcon(i)));
            actionBar.addTab(
                    actionBar.newTab()
                            .setCustomView(tabView)
                            .setTabListener(this));

        }

        Log.d("TabToOpen", tabToOpen.toString());
        if (tabToOpen != -1) {
            mViewPager.setCurrentItem(tabToOpen);
        }

        // Ocultar Botón Buscar Reserva.
        actualizarBotonSearchReservation(false);
        globals.setSiReservationFragment(false);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Log.i("Resume", "Fragment is really");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        // globals = (Globals) getApplication();
        isAuthenticatedUser(globals.getUserIsAuthenticated());
        // Mostrar u Ocultar Botón Buscar Reserva.
        verButtonSearchAdvancedReservation(globals.getVerButtonSearchReservation());
        verButtonReloadMap(globals.getVerButtonReloadMap());
        return super.onCreateOptionsMenu(menu);
    }

    public void isAuthenticatedUser(Boolean value) {
        MenuItem menuItem = this.menu.findItem(R.id.logout_action);
        if (value) {
            menuItem.setVisible(true);
            if (globals.getSiReservationFragment()) { // Si se autentico en Reservas, se muestra el boton de busqueda.
                actualizarBotonSearchReservation(true);
            }

        } else {
            menuItem.setVisible(false);
        }

    }

    public void verButtonSearchAdvancedReservation(Boolean value) {
        menuItemSearchReserva = menu.findItem(R.id.action_search_reservation);
        if (value) {
            menuItemSearchReserva.setVisible(true);
        } else {
            menuItemSearchReserva.setVisible(false);
        }
    }

    public void verButtonReloadMap(Boolean value) {
        menuItemReloadMap = menu.findItem(R.id.action_reload_map);
        if (value) {
            menuItemReloadMap.setVisible(true);
        } else {
            menuItemReloadMap.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_action:
                cerrarSesion();
                return true;
            case R.id.action_search_reservation:
                Fragment page2 = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 5);
                reservationFragment = (ReservationFragment) page2;
                reservationFragment.showDialogSearchAdvanced();
                return true;
            case R.id.action_settings:
                Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            case R.id.action_reload_map:
                if (siEscenarios) {
                    Fragment page0 = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 0);
                    SportsFragment reservationFragment = (SportsFragment) page0;
                    reservationFragment.reloadMap();
                    Log.i("reload", "sports");
                } else {
                    Fragment page3 = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 3);
                    SportActivitiesFragment reservationFragment = (SportActivitiesFragment) page3;
                    reservationFragment.reloadMap();
                    Log.i("reload", "activities");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

        // based on the current position you can then cast the page to the correct
        // class and call the method

        switch (tab.getPosition()) {
            case 0:
                //actionBar.setTitle(R.string.title_sports);
                textView_TitleActionBar.setText(R.string.title_sports);
                actionBar.setDisplayHomeAsUpEnabled(false);
                // Ocultar Botón Buscar Reserva.
                actualizarBotonSearchReservation(false);
                updateButtonReloadMap(true);
                globals.setSiReservationFragment(false);
                siEscenarios = true;
                break;
            case 1:
                //actionBar.setTitle(R.string.title_news);
                textView_TitleActionBar.setText(R.string.title_news);
                actionBar.setDisplayHomeAsUpEnabled(false);
                // Ocultar Botón Buscar Reserva.
                actualizarBotonSearchReservation(false);
                updateButtonReloadMap(false);
                globals.setSiReservationFragment(false);
                break;
            case 2:
                //actionBar.setTitle(R.string.title_user_atention);
                textView_TitleActionBar.setText(R.string.title_user_atention);
                actionBar.setDisplayHomeAsUpEnabled(false);
                // Ocultar Botón Buscar Reserva.
                actualizarBotonSearchReservation(false);
                updateButtonReloadMap(false);
                globals.setSiReservationFragment(false);
                break;
            case 3:
                //actionBar.setTitle(R.string.title_sports_activities);
                textView_TitleActionBar.setText(R.string.title_sports_activities);
                actionBar.setDisplayHomeAsUpEnabled(false);
                // Ocultar Botón Buscar Reserva.
                actualizarBotonSearchReservation(false);
                updateButtonReloadMap(true);
                globals.setSiReservationFragment(false);
                siEscenarios = false;
                break;
            case 4:
                //actionBar.setTitle(R.string.title_recommended);
                textView_TitleActionBar.setText(R.string.title_recommended);
                actionBar.setDisplayHomeAsUpEnabled(false);
                // Ocultar Botón Buscar Reserva.
                actualizarBotonSearchReservation(false);
                updateButtonReloadMap(false);
                globals.setSiReservationFragment(false);
                break;
            case 5:
                //actionBar.setTitle(R.string.title_reservation);
                textView_TitleActionBar.setText(R.string.title_reservation);
                actionBar.setDisplayHomeAsUpEnabled(false);
                // Mostrar Botón Buscar Reserva.
                if (globals.getUserIsAuthenticated()) { // Si esta autenticado que muestre el botón de lo contrario que no lo muestre
                    actualizarBotonSearchReservation(true);
                }
                updateButtonReloadMap(false);
                globals.setSiReservationFragment(true);
                break;
            case 6:
                //actionBar.setTitle(R.string.title_links_inder);
                textView_TitleActionBar.setText(R.string.title_links_inder);
                actionBar.setDisplayHomeAsUpEnabled(false);
                // Ocultar Botón Buscar Reserva.
                actualizarBotonSearchReservation(false);
                updateButtonReloadMap(false);
                globals.setSiReservationFragment(false);
                break;
            default:
                break;
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SportsFragment sportsFragment = new SportsFragment();
        private NewsFragment newsFragment = new NewsFragment();
        private RecommendedFragment recommendedFragment = new RecommendedFragment();
        private ReservationFragment reservationFragment = new ReservationFragment();
        private UserAtentionFragment userAtentionFragment = new UserAtentionFragment();
        private SportActivitiesFragment sportActivitiesFragment = new SportActivitiesFragment();
        private LinksInderFragment linksInderFragment = new LinksInderFragment();


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Integer getIcon(int i) {
            switch (i) {
                case 0:
                    return R.drawable.action_escenario;
                case 1:
                    return R.drawable.action_news;
                case 2:
                    return R.drawable.action_user_atention;
                case 3:
                    return R.drawable.ic_sports_activities;
                case 4:
                    return R.drawable.action_recomended;
                case 5:
                    return R.drawable.action_reservation;
                case 6:
                    return R.drawable.ic_inder_logo;
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0:
                    return sportsFragment;
                case 1:
                    return newsFragment;
                case 2:
                    return userAtentionFragment;
                case 3:
                    return sportActivitiesFragment;
                case 4:
                    return recommendedFragment;
                case 5:
                    return reservationFragment;
                case 6:
                    return linksInderFragment;
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_sports);
                case 1:
                    return getString(R.string.title_news);
                case 2:
                    return getString(R.string.title_user_atention);
                case 3:
                    return getString(R.string.title_sports_activities);
                case 4:
                    return getString(R.string.title_recommended);
                case 5:
                    return getString(R.string.title_reservation);
                case 6:
                    return getString(R.string.title_links_inder);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Activity Result: ", requestCode + " " + resultCode);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Integer value = (Integer) data.getExtras().getInt("valid");
                if (value == 1) {
                    String string = (String) data.getExtras().get("jsonSportScenario");
                    Intent newReservationActivity = new Intent(getApplicationContext(), NewReservationActivity.class);
                    newReservationActivity.putExtra("sportScenario", string);
                    startActivityForResult(newReservationActivity, 2);
                }
            }
        }
        if (requestCode == 2) {
            isAuthenticatedUser(globals.getUserIsAuthenticated());
            if (resultCode == RESULT_OK) {
                Integer value = (Integer) data.getExtras().getInt("valid");
                if (value == 1) {
                    this.sportDetailFragment.dismiss();
                }
            }
        }
        if (requestCode == 3) {
            try {
                if (resultCode == RESULT_OK) {
                    isAuthenticatedUser(globals.getUserIsAuthenticated());
                }
            } catch (RuntimeException e) {
                // Se revienta en Android 5.1.1
            }


        }
        // Cerrar Sesion
        if (requestCode == 4) {
            try {
                if (resultCode == RESULT_OK) {
                    cerrarSesion();
                }
            } catch (RuntimeException e) {
                // Se revienta en Android 5.1.1
            }
        }

    }

    // Función que muestra u oculta botón buscar reserva.
    public void actualizarBotonSearchReservation(Boolean value) {
        globals.setVerButtonSearchReservation(value);
        invalidateOptionsMenu();
    }

    // Función que muestra u oculta botón buscar reserva.
    public void updateButtonReloadMap(Boolean value) {
        globals.setVerButtonReloadMap(value);
        invalidateOptionsMenu();
    }


    public void cerrarSesion() {
        globals.logoutButton();
        isAuthenticatedUser(globals.getUserIsAuthenticated());
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 5);
        reservationFragment = (ReservationFragment) page;
        reservationFragment.removeList(globals.getUserIsAuthenticated());
        // Ocultar Botón Buscar Reserva.
        actualizarBotonSearchReservation(false);
    }

    public void hydeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
        if (MainActivity.this.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
            MainActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }


    private String dateSelected = "";
    public void setDateSelected(String dateSelect, EditText fieldId){
            dateSelected = dateSelect;
            fieldId.setText(dateSelected);
    }

}
