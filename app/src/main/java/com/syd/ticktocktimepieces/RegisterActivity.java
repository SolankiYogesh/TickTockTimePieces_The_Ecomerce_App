package com.syd.ticktocktimepieces;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.syd.ticktocktimepieces.models.CityModel;
import com.syd.ticktocktimepieces.models.CountryModel;
import com.syd.ticktocktimepieces.models.RegistrationResponseModel;
import com.syd.ticktocktimepieces.models.StateModel;
import com.syd.ticktocktimepieces.network.NetworkClient;
import com.syd.ticktocktimepieces.network.NetworkServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {

    Spinner countrySpinner, stateSpinner, citySpinner;
    EditText inputFirstName, inputLastName, inputMobile, inputEmail, inputPassword, inputConfirmPassword;
    RadioButton radioMale, radioFemale;
    TextView  createlogin;
    Button buttonRegister;
    boolean isGenderSelected;
    String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createlogin = findViewById(R.id.tvSignIn);
        createlogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
        inputFirstName = findViewById(R.id.atvfirstname);
        inputLastName = findViewById(R.id.atvlastname);
        inputMobile = findViewById(R.id.mobile);
        inputEmail = findViewById(R.id.atvEmailReg);
        inputPassword = findViewById(R.id.atvPasswordReg);
        inputConfirmPassword = findViewById(R.id.atvcPasswordReg);
        radioMale = findViewById(R.id.redio_male);
        radioFemale = findViewById(R.id.redio_female);
        buttonRegister = findViewById(R.id.btnSignUp);

        countrySpinner = findViewById(R.id.country_spinner);
        stateSpinner = findViewById(R.id.state_spinner);
        citySpinner = findViewById(R.id.city_spinner);

        radioMale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isGenderSelected = true;
                selectedGender = "Male";
            }
        });
        radioFemale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isGenderSelected = true;
                selectedGender = "Female";
            }
        });

        initCountrySpinner();
        ArrayList<StateModel> states = new ArrayList<>();
        states.add(new StateModel("Select State"));
        ArrayAdapter<StateModel> statesAdapter =
                new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, states);
        statesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        stateSpinner.setAdapter(statesAdapter);

        ArrayList<CityModel> cities = new ArrayList<>();
        cities.add(new CityModel("Select City"));


        ArrayAdapter<CityModel> citiesAdapter =
                new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, cities);
        citiesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        citySpinner.setAdapter(citiesAdapter);

        buttonRegister.setOnClickListener(v -> {
            if (inputFirstName.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
            } else if (inputLastName.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (!isGenderSelected) {
                Toast.makeText(RegisterActivity.this, "Select gender", Toast.LENGTH_SHORT).show();
            } else if (inputMobile.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter mobile no.", Toast.LENGTH_SHORT).show();
            } else if (inputMobile.getText().toString().length() < 10) {
                Toast.makeText(RegisterActivity.this, "Enter valid mobile no.", Toast.LENGTH_SHORT).show();
            } else if (countrySpinner.getSelectedItem().toString().equals("Select Country")) {
                Toast.makeText(RegisterActivity.this, "Select country.", Toast.LENGTH_SHORT).show();
            } else if (stateSpinner.getSelectedItem().toString().equals("Select State")) {
                Toast.makeText(RegisterActivity.this, "Select state.", Toast.LENGTH_SHORT).show();
            } else if (citySpinner.getSelectedItem().toString().equals("Select City")) {
                Toast.makeText(RegisterActivity.this, "Select city.", Toast.LENGTH_SHORT).show();
            } else if (inputEmail.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            } else if (!emailValidator(inputEmail.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
            } else if (inputPassword.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            } else if (inputConfirmPassword.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Confirm password", Toast.LENGTH_SHORT).show();
            } else if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Password must be same", Toast.LENGTH_SHORT).show();

            } else {
                HashMap<String, String> params = new HashMap<>();
                params.put("first_name", inputFirstName.getText().toString());
                params.put("last_name", inputLastName.getText().toString());
                params.put("gender", selectedGender);
                params.put("mobile", inputMobile.getText().toString());
                params.put("country", countrySpinner.getSelectedItem().toString());
                params.put("state", stateSpinner.getSelectedItem().toString());
                params.put("city", citySpinner.getSelectedItem().toString());
                params.put("email", inputEmail.getText().toString());
                params.put("password", inputPassword.getText().toString());


                register(params);

            }
        });
    }

    private void initCountrySpinner() {
        ArrayList<CountryModel> countries = new ArrayList<>();
        countries.add(new CountryModel("Select Country"));
        countries.add(new CountryModel("United State"));
        countries.add(new CountryModel("India"));

        ArrayAdapter<CountryModel> countriesAdapter =
                new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, countries);
        countriesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        countrySpinner.setAdapter(countriesAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    ArrayList<StateModel> states = new ArrayList<>();
                    states.add(new StateModel("->Select State<-"));
                    states.add(new StateModel("New South Wales"));
                    states.add(new StateModel("Queensland"));
                    states.add(new StateModel("South Australia"));
                    states.add(new StateModel("Tasmania"));
                    states.add(new StateModel("Victoria"));
                    states.add(new StateModel("Western Australia"));
                    states.add(new StateModel("Jervis Bay Territory"));
                    states.add(new StateModel("Northern Territory"));
                    states.add(new StateModel("Christmas Island"));
                    ArrayAdapter<StateModel> statesAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, states);
                    statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    stateSpinner.setAdapter(statesAdapter);

                } else if (position == 2) {
                    ArrayList<StateModel> states = new ArrayList<>();
                    states.add(new StateModel("->Select State<-"));
                    states.add(new StateModel("Gujarat"));
                    ArrayAdapter<StateModel> statesAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, states);
                    statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    stateSpinner.setAdapter(statesAdapter);
                    stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 1) {
                                ArrayList<CityModel> cities = new ArrayList<>();
                                cities.add(new CityModel("->Select City<-"));
                                cities.add(new CityModel("RAJKOT"));
                                cities.add(new CityModel("JUNAGADH"));
                                cities.add(new CityModel("AHAMDABAD"));
                                ArrayAdapter<CityModel> citiesAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, cities);
                                citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                citySpinner.setAdapter(citiesAdapter);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void register(HashMap<String, String> params) {
        AlertDialog alertDialog = new SpotsDialog(RegisterActivity.this, R.style.Custom4);
        alertDialog.show();
        NetworkServices networkService = NetworkClient.getClient().create(NetworkServices.class);
        Call<RegistrationResponseModel> registerCall = networkService.register(params);
        registerCall.enqueue(new Callback<RegistrationResponseModel>() {
            @Override
            public void onResponse(Call<RegistrationResponseModel> call, Response<RegistrationResponseModel> response) {
                RegistrationResponseModel responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        Toast.makeText(RegisterActivity.this, "You Are SuccessFully Registered", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed Register!", Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.cancel();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponseModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        });
    }

}

