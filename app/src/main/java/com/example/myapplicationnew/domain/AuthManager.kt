package com.example.myapplicationnew.domain

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.asDeferred
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
//Манежер авторизации
class AuthManager @Inject constructor() {

    private val auth by lazy {
        Firebase.auth
    }

    var lastVerificationId = ""


    suspend fun authRequest(
        phone:String,
        activity: Activity,
        onCodeSend:() -> Unit,
        onError:() -> Unit
    ) {
        Firebase.auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)


        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationFailed(p0: FirebaseException) {

                Log.d("MyLod",p0.stackTraceToString())
                onError()
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)

                lastVerificationId = id
                onCodeSend()
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setCallbacks(callback)
            .setActivity(activity)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun confirmCode(code:String) {
        val credential =  PhoneAuthProvider.getCredential(lastVerificationId,code)

        auth.signInWithCredential(credential).asDeferred().await()
    }

    val currentUser : FirebaseUser?
        get() = Firebase.auth.currentUser
}