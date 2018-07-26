package com.gelostech.automart.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import com.gelostech.automart.R
import com.gelostech.automart.adapters.ImagesAdapter
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.AppUtils.setDrawable
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Car
import com.gelostech.automart.utils.*
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.activity_add_car.*
import org.jetbrains.anko.toast
import com.gelostech.automart.utils.PreferenceHelper.get
import timber.log.Timber

class AddCarActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener {
    private var pickedImages = mutableListOf<Uri>()
    private lateinit var imagesAdapter: ImagesAdapter
    private val features = mutableMapOf<String, Boolean>()
    private val details = mutableMapOf<String, String>()
    private val images = mutableMapOf<String, String>()
    private lateinit var KEY: String
    private val car = Car()

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
        setContentView(R.layout.activity_add_car)
        prefs = PreferenceHelper.defaultPrefs(this)

        initViews()
    }

    // Initial views
    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Post car"

        photosRv.setHasFixedSize(true)
        photosRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imagesAdapter = ImagesAdapter()
        photosRv.adapter = imagesAdapter

        addPhoto.setDrawable(setDrawable(this, Ionicons.Icon.ion_android_camera, R.color.colorPrimary, 15))
        addPhoto.setOnClickListener { pickPhotos() }

        initArrays()
        initFeatures()
        post.setOnClickListener { postCar() }
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

    // Initialise features checkboxes
    private fun initFeatures() {
        na.setOnCheckedChangeListener(this)
        turbo.setOnCheckedChangeListener(this)
        supercharged.setOnCheckedChangeListener(this)
        leather.setOnCheckedChangeListener(this)
        electric.setOnCheckedChangeListener(this)
        cups.setOnCheckedChangeListener(this)
        ac.setOnCheckedChangeListener(this)
        windows.setOnCheckedChangeListener(this)
        abs.setOnCheckedChangeListener(this)
        airbags.setOnCheckedChangeListener(this)
        fogs.setOnCheckedChangeListener(this)
        camera.setOnCheckedChangeListener(this)
        sunroof.setOnCheckedChangeListener(this)
        keyless.setOnCheckedChangeListener(this)
        alloys.setOnCheckedChangeListener(this)
        power.setOnCheckedChangeListener(this)
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

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val NA = getString(R.string.naturally_aspirated)
        val TURBO = getString(R.string.turbo_charged)
        val SUPERCHARGED = getString(R.string.supercharged)
        val ELECTRIC = getString(R.string.electric_windows)
        val CUPS = getString(R.string.cups)
        val AIR_CONDITIONING = getString(R.string.air_conditioning)
        val TINTED = getString(R.string.tinted)
        val FOGS = getString(R.string.fog_lamps)
        val CAMERA = getString(R.string.reverse_camera)
        val SUNROOF = getString(R.string.sunroof)
        val KEYLESS = getString(R.string.keyless_entry)
        val ALLOYS = getString(R.string.alloys)
        val LEATHER = getString(R.string.leather_upholstery)
        val ABS = getString(R.string.abs)
        val AIRBAGS = getString(R.string.airbags)
        val POWER = getString(R.string.power)

        when(buttonView?.id) {
            na.id -> {
                if (isChecked) features[NA] = true
                else if (features.containsKey(NA)) features.remove(NA)
            }

            turbo.id -> {
                if (isChecked) features[TURBO] = true
                else if (features.containsKey(TURBO)) features.remove(TURBO)
            }

            supercharged.id -> {
                if (isChecked) features[SUPERCHARGED] = true
                else if (features.containsKey(SUPERCHARGED)) features.remove(SUPERCHARGED)
            }

            leather.id -> {
                if (isChecked) features[LEATHER] = true
                else if (features.containsKey(LEATHER)) features.remove(LEATHER)
            }

            electric.id -> {
                if (isChecked) features[ELECTRIC] = true
                else if (features.containsKey(ELECTRIC)) features.remove(ELECTRIC)
            }

            cups.id -> {
                if (isChecked) features[CUPS] = true
                else if (features.containsKey(CUPS)) features.remove(CUPS)
            }

            ac.id -> {
                if (isChecked) features[AIR_CONDITIONING] = true
                else if (features.containsKey(AIR_CONDITIONING)) features.remove(AIR_CONDITIONING)
            }

            windows.id -> {
                if (isChecked) features[TINTED] = true
                else if (features.containsKey(TINTED)) features.remove(TINTED)
            }

            abs.id -> {
                if (isChecked) features[ABS] = true
                else if (features.containsKey(ABS)) features.remove(ABS)
            }

            airbags.id -> {
                if (isChecked) features[AIRBAGS] = true
                else if (features.containsKey(AIRBAGS)) features.remove(AIRBAGS)
            }

            fogs.id -> {
                if (isChecked) features[FOGS] = true
                else if (features.containsKey(FOGS)) features.remove(FOGS)
            }

            camera.id -> {
                if (isChecked) features[CAMERA] = true
                else if (features.containsKey(CAMERA)) features.remove(CAMERA)
            }

            sunroof.id -> {
                if (isChecked) features[SUNROOF] = true
                else if (features.containsKey(SUNROOF)) features.remove(SUNROOF)
            }

            keyless.id -> {
                if (isChecked) features[KEYLESS] = true
                else if (features.containsKey(KEYLESS)) features.remove(KEYLESS)
            }

            alloys.id -> {
                if (isChecked) features[ALLOYS] = true
                else if (features.containsKey(ALLOYS)) features.remove(ALLOYS)
            }

            power.id -> {
                if (isChecked) features[POWER] = true
                else if (features.containsKey(POWER)) features.remove(POWER)
            }

        }
    }

    // Get vehicle details
    private fun getDetails() {
        details["Body Type"] = bodyType.selectedItem.toString()
        details["Fuel Type"] = fuelType.selectedItem.toString()
        details["Transmission"] = transmission.selectedItem.toString()
        details["Drive Type"] = driveType.selectedItem.toString()
        details["Engine Capacity"] = cc.text.toString().trim()
        details["Mileage"] = mileage.text.toString().trim()
    }

    private fun postCar() {
        if (!isConnected()) {
            toast("Please connect to the internet!")
            return
        }

        if (pickedImages.size < 5) {
            toast("Please select atleast 5 images...")
            return
        }

        if (!AppUtils.validated(cc, mileage, location, price, desc)) {
            toast("Please fill all fields!")
            return
        }

        KEY = getFirestore().collection(K.CARS).document().id

        showLoading("Uploading images...")
        uploadImages()
        getDetails()

    }

    // Upload images to firebase storage
    private fun uploadImages() {
        Timber.e("Images to be uploaded ${pickedImages.size}")

        for (i in 0..(pickedImages.size-1)) {
            val ref = getStorageReference().child(K.CARS).child(KEY).child(i.toString())

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
                        car.image = task.result.toString()
                        images[i.toString()] = task.result.toString()
                        Timber.e("Uploaded image $i url is ${task.result}")

                    } else {
                        images[i.toString()] = task.result.toString()
                        Timber.e("Uploaded image $i url is ${task.result}")

                        if(i == (pickedImages.size-1)) {
                            car.images.putAll(images)
                            hideLoading()

                            setDetails()
                        }
                    }

                }
            }
        }

    }

    // Set detail in the database
    private fun setDetails() {
        Timber.e("Uploading details to Firestore")

        showLoading("Uploading car details...")
        car.id = KEY
        car.sellerId = getUid()
        car.sellerName = prefs[K.NAME]
        car.phone = prefs[K.PHONE]
        car.email = prefs[K.EMAIL]
        car.time = System.currentTimeMillis()
        car.make = make.selectedItem.toString()
        car.model = model.selectedItem.toString()
        car.description = desc.text.toString()
        car.location = location.text.toString().trim()
        car.mileage = mileage.text.toString().trim()
        car.year = year.selectedItem.toString()
        car.condition = condition.selectedItem.toString()
        car.transmission = transmission.selectedItem.toString()
        car.price = price.text.toString().trim()
        car.features = features
        car.details= details

        getFirestore().collection(K.CARS).document(KEY).set(car)
                .addOnSuccessListener {
                    Timber.e("Car successfully uploaded")
                    hideLoading()

                    toast("Car successfully uploaded")
                }
                .addOnFailureListener {
                    Timber.e("Error uploading: $it")
                    hideLoading()

                    toast("Error uploading vehicle. Please try again")
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
