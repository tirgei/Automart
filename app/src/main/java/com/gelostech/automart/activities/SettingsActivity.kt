package com.gelostech.automart.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.commoners.K
import com.gelostech.automart.utils.PreferenceHelper
import com.gelostech.automart.utils.PreferenceHelper.set
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var prefs:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = PreferenceHelper.defaultPrefs(this)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Settings"

        notifications()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.about -> about()
            R.id.terms -> tou()
            R.id.privacy -> privacy()
            R.id.rate ->  rate()
            R.id.feedback -> feedback()
        }
    }

    private fun notifications() {
        notifications.setOnCheckedChangeListener { _, isChecked ->
            prefs[K.NOTIFICATIONS] = isChecked
        }

        news.setOnCheckedChangeListener { _, isChecked ->
            prefs[K.NEWS] = isChecked
        }
    }

    private fun about() {

    }

    private fun tou() {

    }

    private fun privacy() {

    }

    @SuppressLint("InlinedApi")
    private fun rate() {
        val uri = Uri.parse(resources.getString(R.string.play_store_link) + this.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse(resources.getString(R.string.play_store_link) + this.packageName)))
        }
    }

    private fun feedback() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto: tirgeic@gmail.com")
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Automart")
        startActivity(Intent.createChooser(emailIntent, "Send feedback"))
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
