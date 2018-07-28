package com.gelostech.automart.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.gelostech.automart.R
import com.gelostech.automart.adapters.ImagesAdapter
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Part
import com.gelostech.automart.utils.*
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.activity_add_part.*
import com.gelostech.automart.utils.PreferenceHelper.get
import kotlinx.android.synthetic.main.activity_add_part.view.*
import org.jetbrains.anko.toast
import timber.log.Timber

class AddPartActivity : BaseActivity() {
    private var pickedImages = mutableListOf<Uri>()
    private lateinit var imagesAdapter: ImagesAdapter
    private val images = mutableMapOf<String, String>()
    private lateinit var KEY: String
    private val part = Part()

    private lateinit var makes: Array<String>
    private lateinit var toyota: Array<String>
    private lateinit var mazda: Array<String>
    private lateinit var honda: Array<String>
    private lateinit var bmw: Array<String>
    private lateinit var subaru: Array<String>
    private lateinit var benz: Array<String>
    private lateinit var prefs: SharedPreferences

    companion object {
        private const val IMAGE_PICKER = 401
        private const val TOYOTA = "Toyota"
        private const val MAZDA = "Mazda"
        private const val HONDA = "Honda"
        private const val BMW = "BMW"
        private const val BENZ = "Mercedes Benz"
        private const val SUBARU = "Subaru"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_part)
        prefs = PreferenceHelper.defaultPrefs(this)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Post part"

        photosRv.setHasFixedSize(true)
        photosRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imagesAdapter = ImagesAdapter()
        photosRv.adapter = imagesAdapter

        addPhoto.setDrawable(AppUtils.setDrawable(this, Ionicons.Icon.ion_android_camera, R.color.colorPrimary, 15))
        addPhoto.setOnClickListener { pickPhotos() }

        initArrays()
        post.setOnClickListener { postPart() }
    }

    // Initial arrays for spinners
    private fun initArrays() {
        makes = resources.getStringArray(R.array.makes)
        toyota = resources.getStringArray(R.array.toyota)
        mazda = resources.getStringArray(R.array.mazda)
        honda = resources.getStringArray(R.array.honda)
        benz = resources.getStringArray(R.array.benz)
        bmw = resources.getStringArray(R.array.bmw)
        subaru = resources.getStringArray(R.array.subaru)

        make.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()

                setModels(selectedItem)
            }
        }
    }

    // Get selected vehicle make and choose models of selected make
    private fun setModels(make: String) {
        when(make) {
            TOYOTA -> setModelSpinner(toyota)
            MAZDA -> setModelSpinner(mazda)
            HONDA -> setModelSpinner(honda)
            BENZ -> setModelSpinner(benz)
            BMW -> setModelSpinner(bmw)
            SUBARU -> setModelSpinner(subaru)
        }
    }

    // Set selected make models
    private fun setModelSpinner(models: Array<String>) {
        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, models)
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item )

        model.adapter = spinnerArrayAdapter
    }

    // Pick photos from gallery
    private fun pickPhotos() {
        if (!storagePermissionGranted()) {
            requestStoragePermission()
            return
        }

        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(10)
                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(MyGlideEngine())
                .forResult(IMAGE_PICKER)

    }

    private fun setImages() {
        if (pickedImages.size < 1) return

        if (pickedImages.size == 1) {
            mainImage.setImageURI(pickedImages[0])
        } else {
            mainImage.setImageURI(pickedImages[0])
            photosRv.showView()

            for (i in 1..(pickedImages.size-1)) {
                imagesAdapter.addImage(pickedImages[i])
            }

        }

    }

    private fun postPart() {
        if (!isConnected()) {
            toast("Please connect to the internet!")
            return
        }

        if (pickedImages.size < 2) {
            toast("Please select atleast 2 images...")
            return
        }

        if(!AppUtils.validated(partTitle, location, price, desc, quantity)) {
            toast("Please fill all fields!")
            return
        }

        KEY = getFirestore().collection(K.PARTS).document().id

        showLoading("Uploading images...")
        uploadImages()

    }

    // Upload images to firebase storage
    private fun uploadImages() {
        Timber.e("Images to be uploaded ${pickedImages.size}")

        for (i in 0..(pickedImages.size-1)) {
            val ref = getStorageReference().child(K.PARTS).child(KEY).child(i.toString())

            val uploadTask = ref.putFile(pickedImages[i])
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    Timber.e("Error uploading images ${task.exception}}")
                    throw task.exception!!
                }
                // Continue with the task to get the download URL
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (i == 0) {
                        part.image = task.result.toString()
                        images["0"] = task.result.toString()
                        Timber.e("Uploaded image one $i url is ${task.result}")

                    } else {
                        images[i.toString()] = task.result.toString()
                        Timber.e("Uploaded image $i url is ${task.result}")

                        Handler().postDelayed({
                            if(i == (pickedImages.size-1)) {
                                part.images.putAll(images)
                                hideLoading()

                                setDetails()
                            }
                        }, 1500)
                    }

                }
            }
        }

    }

    // Set details to Firestore
    private fun setDetails() {
        Timber.e("Uploading details to Firestore")
        showLoading("Uploading part details...")

        part.id = KEY
        part.name = partTitle.text.toString().trim()
        part.sellerId = getUid()
        part.sellerName = prefs[K.NAME]
        part.time = System.currentTimeMillis()
        part.price = price.text.toString().trim()
        part.quantity = (quantity.text.toString()).toInt()
        part.make = make.selectedItem.toString()
        part.model = model.selectedItem.toString()
        part.location = location.text.toString().trim()
        part.number = number.text.toString().trim()
        part.category = category.selectedItem.toString()
        part.description = desc.text.toString().trim()

        getFirestore().collection(K.PARTS).document(KEY).set(part)
                .addOnSuccessListener {
                    Timber.e("Part successfully uploaded")
                    hideLoading()

                    toast("Part successfully uploaded")
                }
                .addOnFailureListener {
                    Timber.e("Error uploading: $it")
                    hideLoading()

                    toast("Error uploading part. Please try again")
                }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            pickedImages = Matisse.obtainResult(data)

            setImages()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.animateEnterLeft(this)

    }

}
