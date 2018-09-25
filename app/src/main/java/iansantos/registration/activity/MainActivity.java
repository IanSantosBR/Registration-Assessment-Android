package iansantos.registration.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

import iansantos.registration.R;
import iansantos.registration.model.User;
import iansantos.registration.utils.Mask;

import static iansantos.registration.utils.Mask.CPF_MASK;

public class MainActivity extends AppCompatActivity {
    public static final String filename = "contacts.txt";
    private static final String TAG = "MainActivity";
    public static InterstitialAd mInterstitialAd;
    private EditText name;
    private EditText email;
    private EditText cpf;
    private EditText password;
    private EditText passwordConfirmation;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        viewGroup = findViewById(R.id.constraintLayout);
        name = findViewById(R.id.name_editText);
        email = findViewById(R.id.email_editText);
        cpf = findViewById(R.id.cpf_editText);
        password = findViewById(R.id.password_editText);
        passwordConfirmation = findViewById(R.id.password_confirmation_editText);
        cpf.addTextChangedListener(Mask.insert(CPF_MASK, cpf));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            @Override
            public void onAdOpened() {
                super.onAdOpened();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    public void signup(View view) {
        hideKeyboard();
        if (areValidFields() && passwordsMatch()) {
            User user = new User(name.getText().toString(), email.getText().toString(), cpf.getText().toString(), password.getText().toString());
            File outputFile = new File(getFilesDir(), filename);
            try {
                if (!outputFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    outputFile.createNewFile();
                }
                FileOutputStream outputStream = new FileOutputStream(outputFile, true);
                outputStream.write(user.toString().getBytes());
                outputStream.write("\n\n".getBytes());
                outputStream.close();
                Toast.makeText(MainActivity.this, "Sucesso", Toast.LENGTH_SHORT).show();
                showAdvertising();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();
            }
            clear();
        } else {
            Toast.makeText(MainActivity.this, "Verifique os campos obrigatórios", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean areValidFields() {
        boolean areValidFields = true;
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            name.setError("Digite o nome");
            areValidFields = false;
        }
        if (TextUtils.isEmpty(cpf.getText().toString().trim())) {
            cpf.setError("Digite o CPF");
            areValidFields = false;
        } else if (cpf.getText().toString().length() < 14) {
            cpf.setError("CPF não é válido");
        }
        if (TextUtils.isEmpty(email.getText().toString().trim())) {
            email.setError("Digite o email");
            areValidFields = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            email.setError("Email não é válido");
            areValidFields = false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            areValidFields = true;
        }
        if (TextUtils.isEmpty(password.getText().toString().trim())) {
            password.setError("Digite a senha");
            areValidFields = false;
        }
        if (TextUtils.isEmpty(passwordConfirmation.getText().toString().trim())) {
            passwordConfirmation.setError("Re-digite a senha");
            areValidFields = false;
        }
        return areValidFields;
    }

    public boolean passwordsMatch() {
        boolean passwordsMatch = true;
        if (!password.getText().toString().equals(passwordConfirmation.getText().toString())) {
            passwordsMatch = false;
            password.setError("Senhas não correspondem");
            passwordConfirmation.setError("Senhas não correspondem");
        }
        return passwordsMatch;
    }

    public void showAdvertising() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(TAG, "The interstitial wasn't loaded yet.");
        }
    }

    public void clear() {
        ViewGroup group = findViewById(R.id.constraintLayout);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                Objects.requireNonNull((EditText) view).getText().clear();
            }
        }
        viewGroup.requestFocus();
        hideKeyboard();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            Objects.requireNonNull(inputManager).hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
