package com.itosoftware.inderandroid.Activities;


import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.itosoftware.inderandroid.Adapters.RegisterAdapter;
import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.UserContainer;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Fragments.DatePickerFragment;
import com.itosoftware.inderandroid.Interface.ApiUserListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Fragments.RegisterStepOneFragment;
import com.itosoftware.inderandroid.Fragments.RegisterStepTwoFragment;
import com.itosoftware.inderandroid.Fragments.RegisterStepThreeFragment;
import com.itosoftware.inderandroid.Fragments.RegisterStepFourFragment;

import org.json.JSONObject;

/**
 * The <code>ViewPagerFragmentActivity</code> class is the fragment activity hosting the ViewPager
 * @author mwho
 */
public class RegisterActivity extends ActionBarActivity implements ApiUserListener{

    public ActionBar getActionBars() {
        return actionBars;
    }

    public void setActionBars(ActionBar actionBar) {
        this.actionBars = actionBar;
    }

    public ActionBar actionBars;
    public TextView textView_TitleActionBar;
    public ViewPager pager;
    private String dateSelected = "";
    public RegisterStepOneFragment registerStepOneFragment;
    public RegisterStepTwoFragment registerStepTwoFragment;
    public RegisterStepThreeFragment registerStepThreeFragment;
    public RegisterStepFourFragment registerStepFourFragment;

    //Variables Form Register
    //Step One
    public String documentType;
    public String numDocument;
    public String firstNameOne;
    public String firstNameTwo;
    public String firstLastNameOne;
    public String firstLastNameTwo;
    public String bornDate;
    public String sex;
    public String etnia;
    public Boolean displaced = false;
    public Boolean declarant = false;
    public Boolean recognition = false;
    public Boolean handicapped = false;
    public String handicappedType;
    public String handicappedSubtype;
    public Boolean boosHeadFamily = false;

    //Step Two
    public String country;
    public String state;
    public String town;
    public String zone;
    public String commune;
    public String neighborhood;
    public String stratum;
    public String address;
    public String phone;
    public String phoneOptinal;
    public String celular;

    //Step Three
    public String eps;
    public String economyActivity;
    public String gradeLevel;
    public String lastYearApprobate;
    public String schoolType;
    public String school;

    //Step Four
    public String username;
    public String password;
    public String passwordRepeat;
    public String mail;
    public String newsletterInder;
    public String termConditions;


    //Start Getters and Setters Step One
    public String getDocumentType() {
        return documentType;
    }
    public void setDocumentType(String documentType) {
        if(!documentType.equals(null) && !documentType.equals("")) {
            this.documentType = documentType;
        }
    }

    public String getNumDocument() {
        return numDocument;
    }
    public void setNumDocument(String numDocument) {
        if(!numDocument.equals(null) && !numDocument.equals("")) {
            this.numDocument = numDocument;
        }
    }

    public String getFirstNameOne() {
        return firstNameOne;
    }
    public void setFirstNameOne(String firstNameOne) {
        if(!firstNameOne.equals(null) && !firstNameOne.equals("")) {
            this.firstNameOne = firstNameOne;
        }
    }

    public String getFirstNameTwo() {
        return firstNameTwo;
    }
    public void setFirstNameTwo(String firstNameTwo) {
        if(!firstNameTwo.equals(null) && !firstNameTwo.equals("")) {
            this.firstNameTwo = firstNameTwo;
        }
    }

    public String getFirstLastNameOne() {
        return firstLastNameOne;
    }
    public void setFirstLastNameOne(String firstLastNameOne) {
        if(!firstLastNameOne.equals(null) && !firstLastNameOne.equals("")) {
            this.firstLastNameOne = firstLastNameOne;
        }
    }

    public String getFirstLastNameTwo() {
        return firstLastNameTwo;
    }
    public void setFirstLastNameTwo(String firstLastNameTwo) {
        if(!firstLastNameTwo.equals(null) && !firstLastNameTwo.equals("")) {
            this.firstLastNameTwo = firstLastNameTwo;
        }
    }

    public String getBornDate() {
        return bornDate;
    }
    public void setBornDate(String bornDate) {
        if(!bornDate.equals(null) && !bornDate.equals("")) {
            this.bornDate = bornDate;
        }
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        if(!sex.equals(null) && !sex.equals("")) {
            this.sex = sex;
        }
    }

    public String getEtnia() {
        return etnia;
    }
    public void setEtnia(String etnia) {
        if(!etnia.equals(null) && !etnia.equals("")) {
            this.etnia = etnia;
        }
    }

    public Boolean getDisplaced() {
        return displaced;
    }
    public void setDisplaced(Boolean displaced) {
        if(!displaced.equals(null) && !displaced.equals(false)) {
            this.displaced = displaced;
        }
    }

    public Boolean getDeclarant() {
        return declarant;
    }
    public void setDeclarant(Boolean declarant) {
        if(!declarant.equals(null) && !declarant.equals(false)) {
            this.declarant = declarant;
        }
    }

    public Boolean getRecognition() {
        return recognition;
    }
    public void setRecognition(Boolean recognition) {
        if(!recognition.equals(null) && !recognition.equals(false)) {
            this.recognition = recognition;
        }
    }

    public Boolean getHandicapped() {
        return handicapped;
    }
    public void setHandicapped(Boolean handicapped) {
        if(!handicapped.equals(null) && !handicapped.equals(false)) {
            this.handicapped = handicapped;
        }
    }

    public String getHandicappedType() {
        return handicappedType;
    }
    public void setHandicappedType(String handicappedType) {
        if(!handicappedType.equals(null) && !handicappedType.equals("")) {
            this.handicappedType = handicappedType;
        }
    }

    public String getHandicappedSubtype() {
        return handicappedSubtype;
    }
    public void setHandicappedSubtype(String handicappedSubtype) {
        if(!handicappedSubtype.equals(null) && !handicappedSubtype.equals("")) {
            this.handicappedSubtype = handicappedSubtype;
        }
    }

    public Boolean getBoosHeadFamily() {
        return boosHeadFamily;
    }
    public void setBoosHeadFamily(Boolean boosHeadFamily) {
        if(!boosHeadFamily.equals(null) && !boosHeadFamily.equals(false)) {
            this.boosHeadFamily = boosHeadFamily;
        }
    }
    //End Getters and Setters Step One

    //Start Getters and Setters Step Two
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        if(!country.equals(null) && !country.equals("")) {
            this.country = country;
        }
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        if(!state.equals(null) && !state.equals("")) {
            this.state = state;
        }
    }

    public String getTown() {
        return town;
    }
    public void setTown(String town) {
        if(!state.equals(null) && !state.equals("")) {
            this.town = town;
        }
    }

    public String getZone() {
        return zone;
    }
    public void setZone(String zone) {
        if(!zone.equals(null) && !zone.equals("")) {
            this.zone = zone;
        }
    }

    public String getCommune() {
        return commune;
    }
    public void setCommune(String commune) {
        if(!commune.equals(null) && !commune.equals("")) {
            this.commune = commune;
        }
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        if(!address.equals(null) && !address.equals("")) {
            this.address = address;
        }
    }

    public String getStratum() {
        return stratum;
    }
    public void setStratum(String stratum) {
        if(!stratum.equals(null) && !stratum.equals("")) {
            this.stratum = stratum;
        }
    }

    public String getNeighborhood() {
        return neighborhood;
    }
    public void setNeighborhood(String neighborhood) {
        if(!neighborhood.equals(null) && !neighborhood.equals("")) {
            this.neighborhood = neighborhood;
        }
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        if(!phone.equals(null) && !phone.equals("")) {
            this.phone = phone;
        }
    }

    public String getPhoneOptinal() {
        return phoneOptinal;
    }
    public void setPhoneOptinal(String phoneOptinal) {
        if(!phoneOptinal.equals(null) && !phoneOptinal.equals("")) {
            this.phoneOptinal = phoneOptinal;
        }
    }

    public String getCelular() {
        return celular;
    }
    public void setCelular(String celular) {
        if(!celular.equals(null) && !celular.equals("")) {
            this.celular = celular;
        }
    }
    //End Getters and Setters Step Two

    //Start Getters and Setters Step Three
    public String getEps() {
        return eps;
    }
    public void setEps(String eps) {
        if(!eps.equals(null) && !eps.equals("")) {
            this.eps = eps;
        }
    }

    public String getEconomyActivity() {
        return economyActivity;
    }
    public void setEconomyActivity(String economyActivity) {
        if(!economyActivity.equals(null) && !economyActivity.equals("")) {
            this.economyActivity = economyActivity;
        }
    }

    public String getGradeLevel() {
        return gradeLevel;
    }
    public void setGradeLevel(String gradeLevel) {
        if(!gradeLevel.equals(null) && !gradeLevel.equals("")) {
            this.gradeLevel = gradeLevel;
        }
    }

    public String getLastYearApprobate() {
        return lastYearApprobate;
    }
    public void setLastYearApprobate(String lastYearApprobate) {
        if(!lastYearApprobate.equals(null) && !lastYearApprobate.equals("")) {
            this.lastYearApprobate = lastYearApprobate;
        }
    }

    public String getSchoolType() {
        return schoolType;
    }
    public void setSchoolType(String schoolType) {
        if(!schoolType.equals(null) && !schoolType.equals("")) {
            this.schoolType = schoolType;
        }
    }

    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        if(!school.equals(null) && !school.equals("")) {
            this.school = school;
        }
    }
    //End Getters and Setters Step Three

    //Start Getters and Setters Step Four

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        if(!username.equals(null) && !username.equals("")) {
            this.username = username;
        }
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        if(!password.equals(null) && !password.equals("")) {
            this.password = password;
        }
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }
    public void setPasswordRepeat(String passwordRepeat) {
        if(!passwordRepeat.equals(null) && !passwordRepeat.equals("")) {
            this.passwordRepeat = passwordRepeat;
        }
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        if(!mail.equals(null) && !mail.equals("")) {
            this.mail = mail;
        }
    }

    public String getNewsletterInder() {
        return newsletterInder;
    }
    public void setNewsletterInder(String newsletterInder) {
        if(!newsletterInder.equals(null) && !newsletterInder.equals("")) {
            this.newsletterInder = newsletterInder;
        }
    }

    public String getTermConditions() {
        return termConditions;
    }
    public void setTermConditions(String termConditions) {
        if(!termConditions.equals(null) && !termConditions.equals("")) {
            this.termConditions = termConditions;
        }
    }
    //End Getters and Setters Step Four


    /** maintains the pager adapter*/
    private PagerAdapter mPagerAdapter;
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_register);
        this.setActionBars(getSupportActionBar());

        // Center the action bar title
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        textView_TitleActionBar = (TextView) viewActionBar.findViewById(R.id.title_actionbar);

        actionBars.setCustomView(viewActionBar, params);
        actionBars.setDisplayShowCustomEnabled(true); // Se habilita custom para tomar el layout.
        actionBars.setDisplayShowTitleEnabled(false);

        textView_TitleActionBar.setText("Paso Uno");

        //actionBars.setTitle("Paso Uno");
        actionBars.setDisplayHomeAsUpEnabled(true);

        //initialsie the pager
        this.initialisePaging();
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        Fragment fragmentStepOne = Fragment.instantiate(this, RegisterStepOneFragment.class.getName());
        registerStepOneFragment = (RegisterStepOneFragment) fragmentStepOne;

        Fragment fragmentStepTwo = Fragment.instantiate(this, RegisterStepTwoFragment.class.getName());
        registerStepTwoFragment = (RegisterStepTwoFragment) fragmentStepTwo;

        Fragment fragmentStepThree = Fragment.instantiate(this, RegisterStepThreeFragment.class.getName());
        registerStepThreeFragment = (RegisterStepThreeFragment) fragmentStepThree;

        Fragment fragmentStepFour = Fragment.instantiate(this, RegisterStepFourFragment.class.getName());
        registerStepFourFragment = (RegisterStepFourFragment) fragmentStepFour;

        fragments.add(fragmentStepOne);
        fragments.add(fragmentStepTwo);
        fragments.add(fragmentStepThree);
        fragments.add(fragmentStepFour);
        this.mPagerAdapter  = new RegisterAdapter(getSupportFragmentManager(), fragments);

        this.pager = (ViewPager)super.findViewById(R.id.pager_register);
        this.pager.setAdapter(this.mPagerAdapter);

        this.pager.setOffscreenPageLimit(4);
        this.pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        textView_TitleActionBar.setText("Paso Uno");
                        break;
                    case 1:
                        textView_TitleActionBar.setText("Paso Dos");
                        break;
                    case 2:
                        textView_TitleActionBar.setText("Paso Tres");
                        break;
                    case 3:
                        textView_TitleActionBar.setText("Paso Cuatro");
                        break;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                if (this.pager.getCurrentItem() == 0){
                    finish();
                }else{
                    this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0){
            finish();
        }else{
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);
        }
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newdatePickerFragment = new DatePickerFragment();
        newdatePickerFragment.setRegisterActivity(this);
        FragmentManager fm = getSupportFragmentManager();
        newdatePickerFragment.show(fm, "datePicker");
    }

    public void setDateSelected(String dateSelect, Boolean show){
        if(show){
            dateSelected = dateSelect;
            registerStepOneFragment.setBornDate(dateSelected);
        }else{
            Toast.makeText(getBaseContext(), "La fecha de nacimiento no puede ser superior a la actual", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinishedAuthentication(UserContainer userContainer,Boolean authenticated) {
    }
    @Override
    public void onFinishedRegister(Boolean success,HashMap data){
        if(success){
            Globals globals = (Globals) getApplication();
            globals.setUserIsAuthenticated(true);
            globals.setToken(data.get("access_token").toString());
            globals.setRefreshToken(data.get("refresh_token").toString());
            SharedPreferences settings = getApplicationContext().getSharedPreferences("Preferences", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("access_token", data.get("access_token").toString());
            editor.putString("refresh_token", data.get("refresh_token").toString());
            editor.commit();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("authenticated", 1);
            setResult(Activity.RESULT_OK, resultIntent);

            registerStepFourFragment.progress.setBackgroundDrawable(null);
            registerStepFourFragment.progress.setVisibility(View.GONE);

            finish();
        }else{
            registerStepFourFragment.progress.setBackgroundDrawable(null);
            registerStepFourFragment.progress.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), data.get("message").toString(), Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onFinishedForgetPassword(){
    }
    @Override
    public void onFinishedForgetUser(){

    }
    public void onFinishedAddParticipant(Participant participant, Integer status) {

    }

    @Override
    public void onFinishedProfile(ApiUser.Info dataUser) {

    }

    @Override
    public void onFinishedProfilePqr(JSONObject response) {

    }
}