package com.gelostech.automart.fragments


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.activities.MainActivity
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.AppUtils.drawableToBitmap
import com.gelostech.automart.commoners.AppUtils.getColor
import com.gelostech.automart.commoners.AppUtils.setDrawable
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.User
import com.gelostech.automart.utils.*
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.toast
import com.gelostech.automart.utils.PreferenceHelper.set
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import timber.log.Timber

class RegisterFragment : BaseFragment() {
    private lateinit var registerSuccessful: Bitmap
    private var imageUri: Uri? = null
    private var imageSelected = false
    private var isCreatingAccount = false
    private lateinit var prefs: SharedPreferences

    companion object {
        private const val AVATAR_REQUEST = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val successfulIcon = setDrawable(activity!!, Ionicons.Icon.ion_checkmark_round, R.color.white, 25)
        registerSuccessful = drawableToBitmap(successfulIcon)
        prefs = PreferenceHelper.defaultPrefs(activity!!)

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerFirstname.setDrawable(setDrawable(activity!!, Ionicons.Icon.ion_person, R.color.secondaryText, 18))
        registerLastname.setDrawable(setDrawable(activity!!, Ionicons.Icon.ion_person, R.color.secondaryText, 18))
        registerPhone.setDrawable(setDrawable(activity!!, Ionicons.Icon.ion_android_call, R.color.secondaryText, 18))
        registerEmail.setDrawable(setDrawable(activity!!, Ionicons.Icon.ion_ios_email, R.color.secondaryText, 18))
        registerPassword.setDrawable(setDrawable(activity!!, Ionicons.Icon.ion_android_lock, R.color.secondaryText, 18))
        registerConfirmPassword.setDrawable(setDrawable(activity!!, Ionicons.Icon.ion_android_lock, R.color.secondaryText, 18))

        registerAvatar.setOnClickListener {
            if (!isCreatingAccount) {
                if (storagePermissionGranted()) {
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, AVATAR_REQUEST)

                    //pickImageFromGallery()
                } else {
                    requestStoragePermission()
                }
            }
        }

        registerLogin.setOnClickListener {
            if (!isCreatingAccount) {
                if (activity!!.supportFragmentManager.backStackEntryCount > 0)
                    activity!!.supportFragmentManager.popBackStackImmediate()
                else
                    (activity as AppCompatActivity).replaceFragment(LoginFragment(), R.id.authHolder)
            } else activity!!.toast("Please wait...")
        }

        registerTerms.setOnClickListener {
            if (!isCreatingAccount) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://sites.google.com/view/dankmemesapp/terms-and-conditions")
                startActivity(i)
            } else activity!!.toast("Please wait...")
        }

        registerButton.setOnClickListener { signUp() }
    }

    private fun signUp() {
        // Check if all fields are filled
        if (!AppUtils.validated(registerFirstname, registerLastname, registerEmail, registerPassword, registerConfirmPassword)) return

        val name = "${registerFirstname.text.toString().trim()} ${registerLastname.text.toString().trim()}"
        val email = registerEmail.text.toString().trim()
        val pw = registerPassword.text.toString().trim()
        val confirmPw = registerConfirmPassword.text.toString().trim()

        // Check if password and confirm password match
        if (pw != confirmPw) {
            registerConfirmPassword.error = "Does not match password"
            return
        }

        // Check if user has agreed to terms and conditions
        if (!registerCheckBox.isChecked) {
            activity?.toast("Please check the terms and conditions")
            return
        }

        // Check if user has selected avatar
        if (!imageSelected) {
            activity?.toast("Please select a profile picture")
            return
        }

        // Create new user
        isCreatingAccount = true
        registerButton.startAnimation()
        getFirebaseAuth().createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        registerButton.doneLoadingAnimation(getColor(activity!!, R.color.pink), registerSuccessful)
                        Timber.e("signingIn: Success!")

                        // update UI with the signed-in user's information
                        val user = task.result.user
                        updateUI(user)

                        prefs[K.NAME] = name
                        prefs[K.EMAIL] = email
                        prefs[K.PHONE] = registerPhone.text.toString().trim()

                    } else {
                        try {
                            throw task.exception!!
                        } catch (weakPassword: FirebaseAuthWeakPasswordException){
                            isCreatingAccount = false
                            registerButton.revertAnimation()
                            registerPassword.error = "Please enter a stronger password"

                        } catch (userExists: FirebaseAuthUserCollisionException) {
                            isCreatingAccount = false
                            registerButton.revertAnimation()
                            activity?.toast("Account already exists. Please log in.")

                        } catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
                            isCreatingAccount = false
                            registerButton.revertAnimation()
                            registerEmail.error = "Incorrect email format"

                        } catch (e: Exception) {
                            isCreatingAccount = false
                            registerButton.revertAnimation()
                            Timber.e( "signingIn: Failure - $e}")
                            activity?.toast("Error signing up. Please try again.")
                        }
                    }
                }


    }

    private fun updateUI(user: FirebaseUser) {
        val id = user.uid

        val newUser = User()
        newUser.name = "${registerFirstname.text.toString().trim()} ${registerLastname.text.toString().trim()}"
        newUser.email = user.email
        newUser.dateCreated = TimeFormatter().getNormalYear(System.currentTimeMillis())
        //newUser.token = getToken()
        newUser.id = id
        newUser.phone = registerPhone.text.toString().trim()

        val ref = getStorageReference().child("avatars").child(id)
        val uploadTask = ref.putFile(imageUri!!)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            Timber.e("Image uploaded")

            // Continue with the task to getBitmap the download URL
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                newUser.avatar =  task.result.toString()

                user.sendEmailVerification()
                FirebaseMessaging.getInstance().subscribeToTopic(K.TOPIC_GLOBAL)

                getFirestore().collection(K.USERS).document(id).set(newUser).addOnSuccessListener {
                    Timber.e("Adding user: $newUser")
                    registerButton.doneLoadingAnimation(getColor(activity!!, R.color.pink), registerSuccessful)

                    activity!!.toast("Welcome ${registerFirstname.text.toString().trim()} ${registerLastname.text.toString().trim()}")
                    startActivity(Intent(activity!!, MainActivity::class.java))
                    AppUtils.animateEnterRight(activity!!)
                    activity!!.finish()
                }

            } else {
                registerButton.revertAnimation()
                activity?.toast("Error signing up. Please try again.")
                Timber.e("Error signing up: ${task.exception}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AVATAR_REQUEST && resultCode == RESULT_OK) {
            data.let { imageUri = it!!.data }

            startCropActivity(imageUri!!)
            Timber.e("Avatar uri: ${data.toString()}\"")
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                imageSelected = true
                val resultUri = result.uri
                Timber.e("Avatar: $resultUri")

                registerAvatar?.loadUrl(resultUri.toString())
                imageUri = resultUri

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
               Timber.e( "Cropping error: ${result.error.message}")
            }
        }

    }

    private fun startCropActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(context!!, this)
    }

    // Check if user has initiated signing up process. If in process, disable back button
    fun backPressOkay(): Boolean = !isCreatingAccount

    override fun onDestroy() {
        if (registerButton != null) registerButton.dispose()
        super.onDestroy()
    }

}
