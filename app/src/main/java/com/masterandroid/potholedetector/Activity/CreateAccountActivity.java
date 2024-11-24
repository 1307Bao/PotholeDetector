package com.masterandroid.potholedetector.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.masterandroid.potholedetector.API.ApiClient;
import com.masterandroid.potholedetector.API.ApiService;
import com.masterandroid.potholedetector.API.DTO.Request.ApiResponse;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByGmailRequest;
import com.masterandroid.potholedetector.API.DTO.Request.RegisterByUserRequest;
import com.masterandroid.potholedetector.API.DTO.Response.RegisterResponse;
import com.masterandroid.potholedetector.Helper.LocaleHelper;
import com.masterandroid.potholedetector.R;
import com.masterandroid.potholedetector.Security.SecureStorage;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends BaseActivity {

    private TextView tvPrivacy, tvSignIn;
    private TextInputEditText textInputPassword, textInputName, textInputMail;
    private AppCompatButton btnRegisterByUser, btnRegisterByEmail, btnRegisterByFB;
    private LocaleHelper localeHelper;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private static final String USER_FLAG = "USERNAME";
    private ApiService apiService;

    private void updateLanguage() {
        localeHelper = new LocaleHelper();

        String language =  localeHelper.getLanguage(this);
        if (language != null) {
            localeHelper.setLocale(this, language);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        updateLanguage();
        apiService = ApiClient.getClient().create(ApiService.class);

        tvPrivacy = findViewById(R.id.privacy);
        tvSignIn = findViewById(R.id.signInAccount);

        // Set up click handler for Sign In text
        String signInStr = getString(R.string.sign_in);
        setLinkText(signInStr, tvSignIn);

        textInputPassword = findViewById(R.id.editPassword);
        textInputMail = findViewById(R.id.editEmail);
        textInputName = findViewById(R.id.editName);

        btnRegisterByUser = findViewById(R.id.btnCreateAccountNext);
        btnRegisterByEmail = findViewById(R.id.btnCreateByGmail);
        btnRegisterByFB = findViewById(R.id.btnCreateByFacebook);

        setUpBtnRegisterByGoogle();
        setUpBtnRegisterByUser();

//      Create a link at a sequence
        setLinkText("Terms of Service", tvPrivacy);
        setLinkText("Privacy\n Policy", tvPrivacy);
        setLinkText("Sign In", tvSignIn);

//      Setting color for icon at the end of edit text
        TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);
        passwordLayout.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_sub_background_color_gray)));

    }

    private void setLinkText(String word, TextView tv) {
        String text = tv.getText().toString();
        int startIndex = text.indexOf(word);

        if (startIndex != -1) {
            int endIndex = startIndex + word.length();
            SpannableString spannableString = new SpannableString(text);

            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#3461FD")),
                    startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (word.equals(getString(R.string.sign_in))) {
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        Intent intent = new Intent(CreateAccountActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.parseColor("#3461FD")); // Đặt màu cho chữ
                        ds.setUnderlineText(false); // Nếu không muốn gạch chân chữ
                    }

                }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



                tv.setText(spannableString);
                tv.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
                return;
            }

            tv.setText(spannableString);
        }
    }


    private void setUpSignIn(TextView tvSignIn, String signIn) {
        String text = tvSignIn.getText().toString();
        int startIndex = text.indexOf(signIn);
        int endIndex = text.length();

        SpannableString spannableString = new SpannableString(text);

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#3461FD")),
                startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(CreateAccountActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#3461FD"));
                ds.setUnderlineText(false);
            }
        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignIn.setText(spannableString);
        tvSignIn.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    private void setUpBtnRegisterByUser() {
        btnRegisterByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textInputName.getText().toString().trim();
                String email = textInputMail.getText().toString().trim();
                String password = textInputPassword.getText().toString().trim();

                Log.e("Registration Data", "Username: " + username + ", Email: " + email + ", Password: " + password);

                RegisterByUserRequest request = new RegisterByUserRequest(username, email, password);

                registerByUserHandler(request);

            }
        });
    }

    private void registerByUserHandler(RegisterByUserRequest request) {
        Call<ApiResponse<RegisterResponse>> register = apiService.registerByUser(request);

        register.enqueue(new Callback<ApiResponse<RegisterResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RegisterResponse>> call, Response<ApiResponse<RegisterResponse>> response) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    handlerResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RegisterResponse>> call, Throwable throwable) {
                makeToast("Register Failed");
            }
        });
    }

    private void setUpBtnRegisterByGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnRegisterByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        registerByGoogleHandler(requestCode, resultCode, data);
    }

    private void registerByGoogleHandler(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    if (account.getIdToken() != null) {
                        Log.e("Google Token", account.getIdToken());
                        RegisterByGmailRequest request = new RegisterByGmailRequest(account.getIdToken());
                        sendRegisterByGoogleRequest(request);
                    } else {
                        makeToast("Register Failed");
                    }
                } else {
                    makeToast("Register Failed");
                }

            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendRegisterByGoogleRequest(RegisterByGmailRequest request) {
        Call<ApiResponse<RegisterResponse>> register = apiService.registerByGmail(request);

        register.enqueue(new Callback<ApiResponse<RegisterResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ApiResponse<RegisterResponse>> call,
                                   Response<ApiResponse<RegisterResponse>> response) {
                handlerResponse(response);
            }

            @Override
            public void onFailure(Call<ApiResponse<RegisterResponse>> call, Throwable t) {
                makeToast("Registration failed. Please try again later.");
            }
        });
    }

    private void saveUsername(String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_FLAG, value);
        editor.apply();
    }

    private void makeToast(String msg) {
        Toast.makeText(CreateAccountActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handlerResponse(Response<ApiResponse<RegisterResponse>> response) {
        ApiResponse<RegisterResponse> apiResponse = response.body();
        RegisterResponse registerResponse = (RegisterResponse) apiResponse.getResult();

        try {
            SecureStorage secureStorage = new SecureStorage(getApplicationContext());
            secureStorage.saveToken(registerResponse.getToken());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }


        Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
