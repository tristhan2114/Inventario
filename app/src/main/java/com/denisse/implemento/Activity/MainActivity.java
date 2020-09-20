package com.denisse.implemento.Activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.denisse.implemento.Fragment.HomeFragment;
import com.denisse.implemento.R;
import com.denisse.implemento.Utils.ActivityFragmentUtils;

public class MainActivity extends AppCompatActivity {

    /**
     proyectotesisapp@gmail.com
     proyectotesis1234

     https://blog.usejournal.com/firebase-email-and-password-authentication-for-android-e335c81a1dad

     https://www.youtube.com/watch?v=8FJkt4ZwAnQ
     https://github.com/PhilJay/MPAndroidChart

     login
     https://blog.mindorks.com/firebase-login-and-authentication-android-tutorial

     https://www.pinterest.com/pin/81557443238879658/

     https://www.pinterest.com/pin/651685008559307019/

     https://dribbble.com/shots/5666659-Stor-Mobile-App-UX-UI-Design-and-Branding?utm_source=Pinterest_Shot&utm_campaign=sergiikova&utm_content=Stor+%7C+Mobile+App+UX%2FUI+Design+and+Branding&utm_medium=Social_Share

     https://www.pinterest.com/pin/779333910499570251/
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseEmpleado.auth(this, "proyectotesisapp@gmail.com","proyectotesis1234");
        ActivityFragmentUtils.permisosGaleria(this);

        ActivityFragmentUtils.changeFragment(false, getSupportFragmentManager(), new HomeFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //FirebaseEmpleado.auth(this, "proyectotesisapp@gmail.com","proyectotesis1234");
    }
}