package com.gelostech.automart.activities

import android.app.Activity
import android.content.Intent
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
import com.gelostech.automart.R
import com.gelostech.automart.adapters.ImagesAdapter
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.AppUtils.setDrawable
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.utils.GifSizeFilter
import com.gelostech.automart.utils.MyGlideEngine
import com.gelostech.automart.utils.setDrawable
import com.gelostech.automart.utils.showView
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.activity_add_car.*

class AddCarActivity : BaseActivity() {
    private var pickedImages = mutableListOf<Uri>()
    private lateinit var imagesAdapter: ImagesAdapter

    private lateinit var makes: Array<String>
    private lateinit var toyota: Array<String>
    private lateinit var mazda: Array<String>
    private lateinit var honda: Array<String>
    private lateinit var bmw: Array<String>
    private lateinit var subaru: Array<String>
    private lateinit var benz: Array<String>

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
