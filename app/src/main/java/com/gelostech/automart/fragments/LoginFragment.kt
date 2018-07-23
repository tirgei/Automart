package com.gelostech.automart.fragments


import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.activities.MainActivity
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.AppUtils.drawableToBitmap
import com.gelostech.automart.commoners.AppUtils.setDrawable
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.User
import com.gelostech.automart.utils.PreferenceHelper
import com.gelostech.automart.utils.replaceFragment
import com.gelostech.automart.utils.setDrawable
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import timber.log.Timber
import com.gelostech.automart.utils.PreferenceHelper.set

class LoginFragment : BaseFragment() {
    private lateinit var signupSuccessful: Bitmap
    private var isLoggingIn = false
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val successfulIcon = setDrawable(activity!!, Ionicons.Icon.ion_checkmark_round, R.color.white, 25)
        signupSuccessful = drawableToBitmap(successfulIcon)
        prefs = PreferenceHelper.defaultPrefs(activity!!)

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginEmail.setDrawable(setDrawable(activity!!, Ionicons.Icon.ion_ios_email, R.color.secondaryText, 18))
        loginPassword.setDrawable(setDrawable(activity!!, Ionicons.Icon.ion_android_lock, R.color.secondaryText, 18))

        loginRegister.setOnClickListener {
            if (!isLoggingIn)
                (activity as AppCompatActivity).replaceFragment(RegisterFragment(), R.id.authHolder)
            else
                activity!!.toast("Please wait...")
        }

        loginButton.setOnClickListener { signIn() }
        loginForgotPassword.setOnClickListener { if (!isLoggingIn) forgotPassword() else activity!!.toast("Please wait...")}
    }

    private fun signIn() {
        if (!AppUtils.validated(loginEmail, loginPassword)) return

        val email = loginEmail.text.toString().trim()
        val pw = loginPassword.text.toString().trim()

        isLoggingIn = true
        loginButton.startAnimation()
        getFirebaseAuth().signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        Timber.e("signingIn: Success!")

                        // update UI with the signed-in user's information
                        val user = task.result.user
                        updateUI(user!!)
                    } else {
                        try {
                            throw task.exception!!
                        } catch (wrongPassword: FirebaseAuthInvalidCredentialsException) {
                            isLoggingIn = false
                            loginButton.revertAnimation()
                            loginPassword.error = "Password incorrect"

                        } catch (userNull: FirebaseAuthInvalidUserException) {
                            isLoggingIn = false
                            loginButton.revertAnimation()
                            activity?.toast("Account not found. Have you signed up?")

                        } catch (e: Exception) {
                            isLoggingIn = false
                            loginButton.revertAnimation()
                            Timber.e("signingIn: Failure - ${e.localizedMessage}")
                            activity?.toast("Error signing in. Please try again.")
                        }
                    }
                }

    }

    private fun forgotPassword() {
        if (!AppUtils.validated(loginEmail)) return

        val email = loginEmail.text.toString().trim()

        activity?.alert("Instructions to reset your password will be sent to $email") {
            title = "Forgot password"

            positiveButton("SEND EMAIL") {

                getFirebaseAuth().sendPasswordResetEmail(email)
                        .addOnCompleteListener(activity!!) { task ->
                            if (task.isSuccessful) {
                                Timber.e("sendResetPassword: Success!")

                                activity?.toast("Email sent")
                            } else {
                                try {
                                    throw task.exception!!
                                } catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
                                    loginEmail.error = "Incorrect email format"
                                    activity?.toast("Email not sent. Please try again.")

                                } catch (e: Exception) {
                                    Timber.e("sendResetEmail: Failure - $e")
                                    activity?.toast("Email not sent. Please try again.")
                                }
                            }
                        }

            }

            negativeButton("CANCEL") {}
        }!!.show()
    }

    private fun updateUI(user: FirebaseUser) {
        FirebaseMessaging.getInstance().subscribeToTopic(K.TOPIC_GLOBAL)
        val ref = getFirestore().collection(K.USERS).document(user.uid)

        ref.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val userObject = it.result.toObject(User::class.java)

                prefs[K.NAME] = userObject!!.name
                prefs[K.EMAIL] = userObject.email
                prefs[K.PHONE] = userObject.phone

                Handler().postDelayed({
                    activity!!.toast("Welcome back ${userObject.name}")

                    startActivity(Intent(activity!!, MainActivity::class.java))
                    activity!!.overridePendingTransition(R.anim.enter_b, R.anim.exit_a)
                    activity!!.finish()
                }, 400)

            } else {
                Timber.e("Fetching user data failed with ${it.exception}")
            }
        }


    }

    // Check if user has initiated logging in process. If in process, disable back button
    fun backPressOkay(): Boolean = !isLoggingIn

    override fun onDestroy() {
        if (loginButton != null) loginButton.dispose()
        super.onDestroy()
    }

}
