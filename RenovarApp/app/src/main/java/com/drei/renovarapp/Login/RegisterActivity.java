package com.drei.renovarapp.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.drei.renovarapp.Models.RegisterAccount;
import com.drei.renovarapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText txtFullname, txtPassword, txtConfirmPassword, txtEmailAddress;
    private TextInputLayout layoutConfirmPassword, layoutEmailAddress;
    private MaterialButton btnSignup,btnViewTerms;
    private boolean isPasswordSame = false;
    private CheckBox chkPrivacy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        layoutConfirmPassword = findViewById(R.id.layoutConfirmPassword);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        layoutEmailAddress = findViewById(R.id.layoutEmailAddress);
        btnSignup = findViewById(R.id.btnSignup);
        txtFullname = findViewById(R.id.txtFullname);
        btnViewTerms = findViewById(R.id.btnViewTerms);
        chkPrivacy = findViewById(R.id.chkPrivacy);

        txtEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEmailValid(s.toString())) {
                    Drawable img = getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
                    img.setTint(Color.GREEN);
                    txtEmailAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    layoutEmailAddress.setErrorEnabled(false);
                } else {
                    Drawable img = getResources().getDrawable(R.drawable.ic_error_black_24dp);
                    img.setTint(Color.RED);
                    txtEmailAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    layoutEmailAddress.setError("Not a valid email");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtPassword.getText().toString().equals(s.toString())) {
                    Drawable img = getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
                    img.setTint(Color.GREEN);
                    txtConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    layoutConfirmPassword.setErrorEnabled(false);
                    isPasswordSame = true;
                } else {
                    Drawable img = getResources().getDrawable(R.drawable.ic_error_black_24dp);
                    img.setTint(Color.RED);
                    txtConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    layoutConfirmPassword.setError("Password does not match");
                    isPasswordSame = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkPrivacy.isChecked()) {
                    if (isPasswordSame && !TextUtils.isEmpty(txtConfirmPassword.getText()) && !TextUtils.isEmpty(txtEmailAddress.getText())
                            && !TextUtils.isEmpty(txtFullname.getText())) {
                        RegisterAccount registerAccount = new RegisterAccount(new RegisterAccount.AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                if (output.contains("success")) {
                                    Toast.makeText(RegisterActivity.this, "Registration success", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        registerAccount.execute(txtFullname.getText().toString(), txtEmailAddress.getText().toString(), txtPassword.getText().toString());
                    } else {
                        Toast.makeText(RegisterActivity.this, "Please fill up all the information above", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "You must agree to privacy policy of Renovar, before proceeding...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Privacy Policy");
                builder.setMessage(R.string.privacy);
                builder.setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chkPrivacy.setChecked(true);
                    }
                });
                builder.setNegativeButton("DISAGREE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chkPrivacy.setChecked(false);
                    }
                });
                builder.show();
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
