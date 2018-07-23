package com.gelostech.automart.commoners

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.gelostech.automart.utils.Connectivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

open class BaseActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
    }

    fun isConnected(): Boolean {
        return Connectivity.isConnected(this)
    }

    // User hasn't requested storage permission; request them to allow
    fun requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    // Show progress dialog
    fun showLoading(message: String) {
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    fun hideLoading() {
        progressDialog.dismiss()
    }

    // Check if user has granted storage permission
    fun storagePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    // Get root database reference
    fun getDatabaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference

    // Get root firestore reference
    fun getFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // Get Firebase Storage reference
    fun getStorageReference(): StorageReference = FirebaseStorage.getInstance().reference

    // Get FirebaseAuth instance
    fun getFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // Get user
    fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    // Get user ID
    fun getUid(): String {
        val user = FirebaseAuth.getInstance().currentUser

        return user!!.uid
    }

    fun getToken(): String {
        var token:String? = null

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(object : OnSuccessListener<InstanceIdResult> {
            override fun onSuccess(p0: InstanceIdResult?) {
                token = p0!!.token
            }
        })

        return token!!
    }

    fun refreshToken() {
        getDatabaseReference().child("users").child(getUid()).child("userToken").setValue(getToken())
    }
}
