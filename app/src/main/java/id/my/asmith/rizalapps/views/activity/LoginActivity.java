package id.my.asmith.rizalapps.views.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.my.asmith.rizalapps.R;

public class LoginActivity extends AppCompatActivity {
    private EditText TXTEmail;
    private EditText TXTPassword;
    private Button BTNLogin;
    private TextView registerCaption, forgotPassword;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //private LoginService loginService;
    //public LoginActivity() {} | retrofit item

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login Activity");

        /** if(isSessionLogin()) {
            MainActivity.start(this);
            LoginActivity.this.finish();
        } retrofit item **/

        //Progress dialog
        mAuth =  FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mProgressDialog.show();
                    mProgressDialog.setMessage("Please wait...");
                    mProgressDialog.setCancelable(false);
                    new Thread(new Runnable(){
                        public void run(){
                            Log.d("Login", "onAuthStateChanged:signed_in:" + user.getUid());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            mProgressDialog.dismiss();
                        }
                    }).start();

                } else {
                    // User is signed out
                    Log.d("Login", "onAuthStateChanged:signed_out");
                }

            }
        };

        TXTEmail = (EditText) findViewById(R.id.emailText);
        TXTPassword = (EditText) findViewById(R.id.passwordText);
        BTNLogin = (Button) findViewById(R.id.loginButton);
        registerCaption = (TextView) findViewById(R.id.register_caption);
        forgotPassword = (TextView) findViewById(R.id.forgot_pwd);
        BTNLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("user", "signIn");

                if (!validateForm()) {
                    return;
                }

                String email = TXTEmail.getText().toString();
                final String password = TXTPassword.getText().toString();
                //Progress singIn
                mProgressDialog.show();
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.setCancelable(false);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                mProgressDialog.dismiss();

                                if (!task.isSuccessful()) {

                                    // there was an error
                                    if (password.length() < 6) {
                                        TXTPassword.setError(getString(R.string.error_message_min_pwd));
                                    } else {
                                        Toast.makeText(LoginActivity.this,
                                                getString(R.string.error_message_mAuth_fail),
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Welcome back " +
                                                    TXTEmail.getText().toString(),
                                            Toast.LENGTH_SHORT).show();


                                    startActivity(new Intent(LoginActivity.this,
                                            MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });


        //Register activity
        String caption = "Dont have an account? <b>Register</b>";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(Html.fromHtml(caption));
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                RegisterActivity.start(LoginActivity.this);
            }
        }, caption.indexOf("Register") - 3, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), caption
                .indexOf("Register") - 3, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        registerCaption.setText(spannableStringBuilder);
        registerCaption.setMovementMethod(LinkMovementMethod.getInstance());

        String htmlString="<u>Forgot Password?</u>";
        forgotPassword.setText(Html.fromHtml(htmlString));
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(LoginActivity.this);
                    alertDialogBuilder.setTitle("Enter your registered email address!");
                    alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                    alertDialogBuilder.setCancelable(false);
                    //Showing EditText in alertDialog
                    final EditText inPwd = new EditText(LoginActivity.this);
                    alertDialogBuilder.setView(inPwd, 60, 60, 60, 0);
                    inPwd.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    inPwd.setSingleLine(true);
                    // set positive button: Send
                    alertDialogBuilder.setPositiveButton("Send",new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                            // go to a new activity of the app
                            // onClickEvent to reset the password
                            // Get string type data from inPwd
                            String email = inPwd.getText().toString().trim();
                            if (TextUtils.isEmpty(email)) {
                                Toast.makeText(getApplication(),
                                        "Enter your registered email address",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // Displaying Progress Dialog
                            mProgressDialog.show();
                            mProgressDialog.setMessage("Sending request!");
                            // Sending data to your registered email address
                            mAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mProgressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                // give notification if instructions reset
                                                // password was send to your email
                                                Toast.makeText(LoginActivity.this,
                                                        "We have sent you instructions" +
                                                                " to reset your password!",
                                                        Toast.LENGTH_SHORT).show();
                                                int finishTime = 3; //10 secs
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    public void run() {
                                                        dialog.cancel();
                                                    }
                                                }, finishTime * 300);
                                            } else {
                                                // notify to user if system cant send
                                                // instructions to reset the password
                                                Toast.makeText(LoginActivity.this,
                                                        "Failed to send an email!",
                                                        Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });
                        }
                    });
                    // set negative button: Cancel
                    alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // cancel the alert box and put a Toast to the user
                            dialog.cancel();
                            // notify to user hes press the cancel button
                            Toast.makeText(getApplicationContext(), "Canceled",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show alert
                    alertDialog.show();

                }
        });


    }


    //Retrofit Item
    /** void loginAct() {
        String email = TXTEmail.getText().toString();
        String password = TXTPassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            TXTEmail.setError("Email cannot be empty!");
            return;
        }

        if(TextUtils.isEmpty(password)) {
            TXTPassword.setError("Password cannot be empty");
            return;
        }

        loginService = new LoginService(this);
        loginService.doLogin(email, password, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Users user = (Users) response.body();

                if(user != null) {
                    if(!user.isError()) {
                        PrefUtil.putUser(LoginActivity.this, PrefUtil.USER_SESSION, user);
                        MainActivity.start(LoginActivity.this);
                        LoginActivity.this.finish();
                    }

                    Toast.makeText(LoginActivity.this, user.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(LoginActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

     boolean isSessionLogin() {
        return PrefUtil.getUser(this, PrefUtil.USER_SESSION) != null;
    }**/

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(TXTEmail.getText().toString())) {
            TXTEmail.setError("Required");
            result = false;
        } else {
            TXTEmail.setError(null);
        }

        if (TextUtils.isEmpty(TXTPassword.getText().toString())) {
            TXTPassword.setError("Required");
            result = false;
        } else {
            TXTPassword.setError(null);
        }

        return result;
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
