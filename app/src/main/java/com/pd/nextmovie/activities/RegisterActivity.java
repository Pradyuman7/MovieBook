package com.pd.nextmovie.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pd.chocobar.ChocoBar;
import com.pd.nextmovie.R;
import com.pd.nextmovie.model.User;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private TextView forgotPassword;
    private final static int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener mAL;

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.forgot_password);
        Button signUp = findViewById(R.id.signup);
        Button signIn = findViewById(R.id.signin);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChocoBar.builder().setActivity(RegisterActivity.this).setText("Try signing in with Google").centerText().setDuration(ChocoBar.LENGTH_SHORT).setBackgroundColor(Color.TRANSPARENT).build().show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAL = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(RegisterActivity.this, MoviesActivity.MovieTabActivity.class));
                    finish();
                }
            }
        };

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonEffect(view);
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(!emailText.equals("") && !passwordText.equals("")){

                    mAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                startActivity(new Intent(RegisterActivity.this, MoviesActivity.MovieTabActivity.class));
                                finish();
                            }
                            else {
                                Log.w("task error:", Objects.requireNonNull(task.getException()).toString());
                                ChocoBar.builder().setActivity(RegisterActivity.this).setText("Something went wrong").setDuration(ChocoBar.LENGTH_SHORT).red().show();
                            }

                        }
                    });
                }

                else{
                    ChocoBar.builder().setActivity(RegisterActivity.this).setText("Please fill up all details").setDuration(ChocoBar.LENGTH_SHORT).red().show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonEffect(view);
                final String emailText = email.getText().toString();
                final String passwordText = password.getText().toString();

                if(!emailText.equals("") && !passwordText.equals("")){
                    if(checkEmailExists(emailText)){
                        ChocoBar.builder().setActivity(RegisterActivity.this).setText("Such email already exists, try logging in").setDuration(ChocoBar.LENGTH_SHORT).red().show();
                    }
                    else{
                        // no need to hash password with Firebase, it already hashes it

                        mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "signInWithEmail:success");

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                                    FirebaseUser fUser = mAuth.getCurrentUser();
                                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                                    assert fUser != null;

                                    User user = new User(fUser.getDisplayName(),"Currently not set");
                                    ref1.child("users").child(fUser.getUid()).setValue(user);

                                }

                                else {
                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    }
                                    catch (FirebaseAuthWeakPasswordException weakPassword) {
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        ChocoBar.builder().setActivity(RegisterActivity.this).setText("Password is weak, keep at least 6 characters").setDuration(ChocoBar.LENGTH_SHORT).orange().show();

                                    }
                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        ChocoBar.builder().setActivity(RegisterActivity.this).setText("Email not valid").setDuration(ChocoBar.LENGTH_SHORT).red().show();

                                    }
                                    catch (FirebaseAuthUserCollisionException existEmail) {
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        ChocoBar.builder().setActivity(RegisterActivity.this).setText("Email already exists, try signing in").setDuration(ChocoBar.LENGTH_SHORT).orange().show();

                                    } catch (Exception e) {
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        ChocoBar.builder().setActivity(RegisterActivity.this).setText("Authentication failed").setDuration(ChocoBar.LENGTH_SHORT).red().show();

                                    }

                                }
                            }
                        });


                    }
                }

                else{
                    ChocoBar.builder().setActivity(RegisterActivity.this).setText("Please fill up all details").setDuration(ChocoBar.LENGTH_SHORT).red().show();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        SignInButtonImpl register = findViewById(R.id.GButton1);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v);
                try {
                    signIn();
                }
                catch(NullPointerException e) {
                    Toast.makeText(RegisterActivity.this,"Null Reference ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                ChocoBar.builder().setActivity(RegisterActivity.this).setText("No such google account found").setDuration(ChocoBar.LENGTH_SHORT).red().show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

    }

    private void signIn(){
        Intent sIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(sIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result  = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();

                if(account == null){
                    ChocoBar.builder().setActivity(RegisterActivity.this).setText("No google account!").setDuration(ChocoBar.LENGTH_SHORT).red().show();
                }
                else
                    firebaseAuthWithGoogle(account);
            }
            else{
                Toast.makeText(RegisterActivity.this,"Auth went wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("Tag","SignInWithCredential: success");

                    final FirebaseUser fUser = mAuth.getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    assert fUser != null;

                    User user = new User(fUser.getDisplayName(),"Currently not set");
                    ref.child("users").child(fUser.getUid()).setValue(user);

                    startActivity(new Intent(RegisterActivity.this, MoviesActivity.MovieTabActivity.class));
                    finish();

                }
                else{
                    Log.w("TAG","SignInWithCredential: failure", task.getException());
                    Toast.makeText(RegisterActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkEmailExists(final String emailForCheck){
        final boolean[] ans = {false};

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(Objects.requireNonNull(ds.child("email").getValue()).toString().equals(emailForCheck)){
                        ans[0] = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return ans[0];
    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }


}
