package com.myogardener.kotlin_realtime_database_7

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Array
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MainActivity : AppCompatActivity() {
    var googleSignClient: GoogleSignInClient? = null
    val RC_SIGN = 1000
    var callbackManger = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_signup.setOnClickListener {
            createEmailId()
        }

        var gsn = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignClient = GoogleSignIn.getClient(this, gsn)

        btn_google_login.setOnClickListener {
            var _signInIntent = googleSignClient?.signInIntent
            startActivityForResult(_signInIntent, RC_SIGN)
        }
        btn_facebook_login.setOnClickListener {
            facebookLogin()
        }

        btn_email_login.setOnClickListener {
            LoginEmail()
        }
        printHashKey(this)
    }

    override fun onResume() {
        super.onResume()
        moveNextPage()
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        var credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Sign in Success", Toast.LENGTH_LONG).show()
                println("Google Sign successful")
            }
        }
    }

    fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance()
            .registerCallback(callbackManger, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    firebaseAuthwithFacebook(result)
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException?) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun firebaseAuthwithFacebook(result: LoginResult?) {
        var credential = FacebookAuthProvider.getCredential(result?.accessToken?.token!!)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                moveNextPage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RC_SIGN) {
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var account = task.getResult(ApiException::class.java)
            callbackManger.onActivityResult(requestCode, resultCode, data)
            firebaseAuthWithGoogle(account)
        }

    }

    fun createEmailId() {
        var email = edt_email.text.toString()
        var password = edt_password.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    moveNextPage()
                }

            }

    }

    fun LoginEmail() {
        var email = edt_email.text.toString()
        var password = edt_password.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    moveNextPage()
                }
            }
    }

    fun printHashKey(pContext: Context) {
        try {
            val info: PackageInfo = pContext.getPackageManager()
                .getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                println("printHashKey() Hash Key:////////////////////////////////////// $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {

} catch (e: Exception) {
}
}

fun moveNextPage() {
    var currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        startActivity(Intent(this, NextActivity::class.java))
    }
}

}