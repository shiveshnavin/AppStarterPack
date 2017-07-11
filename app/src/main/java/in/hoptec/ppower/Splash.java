package in.hoptec.ppower;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import in.hoptec.ppower.utils.Rotate3dAnimation;
import in.hoptec.ppower.views.SplashView;

import static in.hoptec.ppower.utl.TAG;

public class Splash extends AppCompatActivity {

    private Context ctx;
    private Activity act;
    private SplashView splashView;
    private View logins;
    private View activity_splash;
    private TextView app;
    FirebaseUser firebaseUser;

    private FirebaseAuth mAuth;

    String phone = null;

    public View fb_login, g_login, m_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
          utl.fullScreen(act);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash);

        checkPermission();
        utl.setShared(this);
        bindViews();
        setUpAccent();
        initAnims();
        initFbLogin();
        initMLogin();
        initGLogin();

        g_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        loginViaG();

            }
        });
        m_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginViaM();
            }
        });
        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViaFb();


            }
        });


    }


    public static String TAG = "Splash";


    void bindViews() {
        splashView = (SplashView) findViewById(R.id.splash_view);
        app = (TextView) findViewById(R.id.app);
        logins = findViewById(R.id.logins);

        image = (ImageView) findViewById(R.id.logo);

        g_login = findViewById(R.id.g_login);
        m_login = findViewById(R.id.m_login);
        fb_login = findViewById(R.id.fb_login);

        activity_splash = findViewById(R.id.activity_splash);
    }

    void initAnims() {

        splashView.splashAndDisappear(new SplashView.ISplashListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onUpdate(float completionFraction) {

                if (completionFraction > 0.5) {

                    zoom();
                }
            }

            @Override
            public void onEnd() {


            }
        });


    }


    void setUpAccent() {
        ImageView fbi = (ImageView) findViewById(R.id.fb_small);
        ImageView gi = (ImageView) findViewById(R.id.g_small);
        ImageView mi = (ImageView) findViewById(R.id.m_small);


        utl.ctx = this;
        utl.changeColorDrawable(fbi, R.color.color_splash_accent);
        utl.changeColorDrawable(gi, R.color.color_splash_accent);
        utl.changeColorDrawable(mi, R.color.color_splash_accent);


    }


    ImageView image;

    public void zoom() {
        Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_anim);

        // set animation listener
        animZoomIn.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {


                                            }

                                            @Override
                                            public void onAnimationEnd(Animation animation) {

                                               // startRotation(0,360);

                                             /*   ImageView img = (ImageView) findViewById(R.id.logo);
                                                RotateAnimation rotateAnimation = new RotateAnimation(30, 90,
                                                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                                rotateAnimation.setRepeatMode(Animation.REVERSE);
                                                rotateAnimation.setRepeatCount(1000);
                                                rotateAnimation.setDuration(5000);
                                                img.startAnimation(rotateAnimation);

*/
                                                if (utl.getUser() != null) {
                                                    updateUI(utl.getUser());
                                                    return;
                                                }

                                                utl.logout();

                                                if (Constants.IS_ANIMATED_BG_SPLASH)
                                                    utl.animateBackGround(activity_splash, "#FF5722", "#009688", true, 10000);
/*
                                                logins.setVisibility(View.VISIBLE);
                                                logins.setAlpha(0.0f);
                                                logins.animate()
                                                        .translationY(logins.getHeight())
                                                        .alpha(1.0f);

                                                */


                                                logins.setVisibility(View.VISIBLE);
                                                //logins.setAlpha(1f);
                                             /*   logins.animate()
                                                        .translationY(logins.getHeight())
                                                        .alpha(1.0f);
*/
                                                utl.slideUP(logins,ctx);


                                            }

                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        }
        );
        findViewById(R.id.logo).startAnimation(animZoomIn);

        View l = findViewById(R.id.logo);
        l.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(ctx, "Developer : Shivesh Navin \n http://thehoproject.co.nf/", Toast.LENGTH_LONG).show();
                ;
                return false;
            }
        });

    }






    private Rotate3dAnimation rotation;
    private StartNextRotate startNext;

    private void startRotation(float start, float end) {
        // Calculating center point
        final float centerX = image.getWidth() / 2.0f;
        final float centerY = image.getHeight() / 2.0f;
        Log.d(TAG, "centerX="+centerX+", centerY="+centerY);
        // Create a new 3D rotation with the supplied parameter
        // The animation  listener is used to trigger the next animation
        //final Rotate3dAnimation rotation =new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        //Z axis is scaled to 0
        rotation =new Rotate3dAnimation(start, end, centerX, centerY, 0f, true);
        rotation.setDuration(6000);
        rotation.setFillAfter(true);
        //rotation.setInterpolator(new AccelerateInterpolator());
        //Uniform rotation
        rotation.setInterpolator(new LinearInterpolator());
        //Monitor settings
        startNext = new StartNextRotate();
        rotation.setAnimationListener(startNext);
        image.startAnimation(rotation);
    }

private class StartNextRotate implements Animation.AnimationListener {

    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onAnimationEnd......");
        image.startAnimation(rotation);
    }

    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

}





    //************************FB LOGIN***********************/

    LoginButton loginButton;
    CallbackManager callbackManager;
    void initFbLogin()
    {
        callbackManager = CallbackManager.Factory.create();
        loginButton= (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    utl.l(TAG, "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    utl.l(TAG, "facebook:onCancel");

                }

                @Override
                public void onError(FacebookException error) {
                    utl.l(TAG, "facebook:onError" );
                    error.printStackTrace();;

                }
            });


    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {

                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(Splash.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }


                        }
                    });
    }


    void loginViaFb()
    {
        loginButton.callOnClick();
    }



    //************************FB LOGIN***********************/

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
     //   updateUI(firebaseUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            try {
                handleSignInResult(result.getSignInAccount());
            } catch (Exception e) {
                utl.toast(ctx,"Google SignIn not supported by your device !");
                e.printStackTrace();
            }
        }
        else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }






    public boolean askedForName=false;
    public void updateUI(final FirebaseUser firebaseUser)
    {
        if(firebaseUser==null)
            return;

        user=new GenricUser(firebaseUser);
        utl.writeUserData(user,ctx);

        utl.l("Provider " ,FirebaseAuth.getInstance().getCurrentUser().getProviderId());

        String provider=FirebaseAuth.getInstance().getCurrentUser().getProviderId();



        for (UserInfo users: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {

              System.out.println("User is signed in with  "+users.getProviderId());
              provider=users.getProviderId();
            if(provider.contains("facebook")||provider.contains("google"))
            {
                break;
            }

        }



        if(provider.contains("firebase"))
        {
            if(firebaseUser.getDisplayName()==null)
            {

                if(diag!=null)
                    if(diag.isShowing())
                        diag.dismiss();

                if(!askedForName)
                diag=utl.inputDialog(ctx, "Login Via Phone", "Enter Your Name", utl.TYPE_DEF,new utl.InputDialogCallback() {
                    @Override
                    public void onDone(String text) {

                        askedForName=true;
                        UserProfileChangeRequest request=new UserProfileChangeRequest.Builder().setDisplayName(text).build();
                        firebaseUser.updateProfile(request);

                        updateUI(firebaseUser);

                    }
                });


            }
        }



        if(firebaseUser.getDisplayName()!=null)
            utl.toast(ctx,"Welcome ! "+firebaseUser.getDisplayName());
        else
            utl.toast(ctx,"Welcome ! You are Logged In !");



        if(firebaseUser.getDisplayName()!=null||askedForName)
        {

            if(phone!=null)
            {
                user.user_phone=phone;
            }
            register(user);

            if(diag!=null)
                if(diag.isShowing())
                    diag.dismiss();



            Intent intent=new Intent(ctx,Home.class);
            View v;
            if(provider.contains("facebook"))
            {
                v=fb_login;
            }
            else if(provider.contains("google")) {

                v=g_login;
            }
            else
            {
                v=m_login;
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(act, v, getString(R.string.activity_image_trans));
                startActivity(intent, options.toBundle());
            }
            else {
                startActivity(intent);
            }

            //finish();




        }


    }




    //************************G LOGIN***********************/

    public static GoogleApiClient mGoogleApiClient;
    public int RC_SIGN_IN=99;
    void initGLogin()
    {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
		.requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



    }
    void loginViaG()
    {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void handleSignInResult(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in firebaseUser's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the firebaseUser.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(ctx, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }



    //************************G LOGIN***********************/



    //************************M LOGIN***********************/

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);


    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in firebaseUser's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                            // [START_EXCLUDE]
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            // [END_EXCLUDE]
                        }
                    }
                });
    }


    void initMLogin()
    {


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                } else if (e instanceof FirebaseTooManyRequestsException) {

                }

            }

            @Override
            public void onCodeSent(final String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the firebaseUser to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

                diag=utl.inputDialog(ctx, "Login Via Phone", "Enter OTP", utl.TYPE_DEF,new utl.InputDialogCallback() {
                    @Override
                    public void onDone(String text) {
                        verifyPhoneNumberWithCode(verificationId,text);
                    }
                });
            }
        };

    }


    AlertDialog diag;
    void loginViaM()
    {

        utl.showDig(true,ctx);
        AndroidNetworking.get("http://www.thehoproject.co.nf/status.php?action=check_phone_login&app="+
                URLEncoder.encode(getResources().getString(R.string.app_name))).build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        utl.showDig(false,ctx);
                        utl.l(response);

                        if(response.contains("notcool"))
                        {
                            utl.snack(act,"Phone Login is Disabled !");
                            return;
                        }
                        firebaseUser =utl.getUser();
                        if(firebaseUser !=null)
                        {
                            updateUI(firebaseUser);
                            return;
                        }

                        utl.inputDialog(ctx, "Login Via Phone", "Enter Phone No :",utl.TYPE_PHONE,new utl.InputDialogCallback() {
                            @Override
                            public void onDone(String text) {

                                if(!utl.isValidMobile(text)) {
                                    utl.toast(ctx, "Invalid Phone !");
                                    return;
                                }

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        text,        // Phone number to verify
                                        120,                 // Timeout duration
                                        TimeUnit.SECONDS,   // Unit of timeout
                                        act,               // Activity (for callback binding)
                                        mCallbacks);


                            }
                        });



                    }

                    @Override
                    public void onError(ANError ANError) {

                        utl.showDig(false,ctx);
                        utl.l(ANError.getErrorDetail());
                    }
                });








    }

    //************************M LOGIN***********************/



    public void register( final GenricUser tmpusr)
    {


        String url=Constants.HOST+Constants.API_USER_REG_GET+"?"+
                "user_name="+ URLEncoder.encode(""+tmpusr.user_name) +
                "&user_email="+ URLEncoder.encode(""+tmpusr.user_email) +
                "&user_fname="+ URLEncoder.encode(""+tmpusr.user_fname) +
                "&user_password="+ URLEncoder.encode(""+tmpusr.user_password) +
                "&user_image="+ URLEncoder.encode(""+tmpusr.user_image) +
                "&user_phone="+ URLEncoder.encode(""+tmpusr.user_phone) +
                "&auth="+ URLEncoder.encode(""+tmpusr.suid);



        utl.l("reg url : "+url);

        AndroidNetworking.get(url).build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        response=""+response;
                        utl.l("reg resp "+response);
                        if(response.contains("error"))
                        {

                           // emailLogin(false,true);

                        }
                        else {

                           // startlogin(tmpusr.user_email,tmpusr.suid,1);
                        }

                    }

                    @Override
                    public void onError(ANError ANError) {

                        utl.e("err 566"+ANError.getErrorBody());
                    }
                });

    }



    /*************************EMAIL LOGIN**************************************/
    View v=null;


    GenricUser user;
    public void emailLogin(final boolean isInvalid,final boolean alreadyRegistered){

        AlertDialog.Builder di;
        if(!isInvalid||v==null){

            if(dig!=null)
            {
                if(dig.isShowing())
                    dig.dismiss();
            }
            v=getLayoutInflater().inflate(R.layout.dig_reg,null);
            di = new AlertDialog.Builder(ctx);
            di.setView(v);
            dig=di.create();
            dig.show();

        }
        if(v!=null) {
            final TextView register = (TextView) v.findViewById(R.id.register);
            final TextView log = (TextView) v.findViewById(R.id.log);
            final EditText username = (EditText) v.findViewById(R.id.username);
            final EditText email = (EditText) v.findViewById(R.id.email);
            final EditText fname = (EditText) v.findViewById(R.id.fname);
            final EditText password = (EditText) v.findViewById(R.id.password);
            final EditText password2 = (EditText) v.findViewById(R.id.password2);
            final Button login = (Button) v.findViewById(R.id.login);

            email.setText(user.user_email);
            fname.setText(user.user_fname);


            final View reg = v.findViewById(R.id.reg);
            final View scrl = v.findViewById(R.id.scrl);
            final View load = v.findViewById(R.id.load);

            scrl.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);
            reg.setVisibility(View.GONE);



            register.setVisibility(View.VISIBLE);

            reg.setVisibility(View.VISIBLE);
            login.setText("REGISTER");
            log.setText("REGISTER");
            register.setText(" ");
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emailLogin(false,false);

                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uname=username.getText().toString();
                    String em=email.getText().toString();
                    String fn=fname.getText().toString();
                    String ps1=password.getText().toString();
                    String ps2=password2.getText().toString();

                    user.user_fname=fn;
                    user.user_name=uname;
                    user.user_password=ps1;
                    user.user_email=em;
                    if(user.user_password.equals(ps2))
                    {
                        register(user);
                        scrl.setVisibility(View.GONE);
                        load.setVisibility(View.VISIBLE);
                    }
                    else {
                        password.setError("Password donot match !");
                    }




                }
            });


            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

           /* login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String ps1=password.getText().toString();
                    String em=email.getText().toString();

                    scrl.setVisibility(View.GONE);
                    load.setVisibility(View.VISIBLE);

                    startlogin(em,ps1,0);

                }
            });

*/
            Dialog dig;

            if(isInvalid)
            {
                password.setError("Invalid Credentials !");
            }


            if(alreadyRegistered)
            {
                email.setError("Username or E-Mail already registered !");
            }





        }




    }




    AlertDialog dig;

    Gson js=new Gson();
    public void startlogin(String email,String auth,int isFacebook)
    {

        //http://35.163.210.177/api/login.php?user_email=shiveshnavin@gmail.com&auth=643045572566298


        String url=Constants.HOST+"/api/login.php?" +
                "user_email="+email +
                "&auth="+auth+
                "&facebook="+isFacebook
                ;


        utl.l("login url : "+url);
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {   utl.l("login resp "+response);

                            if(response.contains("error"))
                            {
                                JSONObject jsonObject=new JSONObject(response);
                                emailLogin(false,false);
                                // utl.snack(act,""+jsonObject.getString("error"));
                            }
                            else {
                                if(dig!=null)
                                {
                                    dig.dismiss();;
                                }
                                GenricUser oUser;
                                oUser=js.fromJson(response, GenricUser.class);
                                utl.writeUserData(oUser,ctx);
                                startActivity(new Intent(ctx,Home.class));
                            }

                        } catch (Exception e) {

                            if(dig!=null)
                            {
                                dig.dismiss();;
                            }

                            //utl.snack(  act,"Invalid Credentials !"  );
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError ANError) {
                        utl.l("reg err "+ANError.getErrorDetail());
                        if(dig!=null)
                        {
                            dig.dismiss();;
                        }
                        emailLogin(false,false);
                    }
                });


    }





    public void checkPermission()
    {

        String permissionss []={
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        ActivityCompat.requestPermissions(this,
                permissionss,
                1);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                 if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                } else {

                    Toast.makeText(ctx, "Please Grant Permissions for app to work properly !", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }






}
