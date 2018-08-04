package com.gelostech.automart.activities

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.AppUtils.setDrawable
import com.gelostech.automart.commoners.ChartLabelsFormatter
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.commoners.K
import com.gelostech.automart.utils.PreferenceHelper.get
import com.gelostech.automart.utils.TimeFormatter
import com.gelostech.automart.utils.hideView
import com.gelostech.automart.utils.random
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.activity_sales.*
import com.github.mikephil.charting.components.XAxis
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment
import okhttp3.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import timber.log.Timber
import java.io.IOException
import java.util.*


class SalesActivity : BaseActivity(), SmoothDateRangePickerFragment.OnDateRangeSetListener {
    private lateinit var dateRangePicker: SmoothDateRangePickerFragment
    private var startMonth = 0
    private var endMonth = 0

    var cars = 0
    var parts = 0
    var amnt = 0
    var toyota = 0
    var mazda = 0
    var bmw = 0
    var subaru = 0
    var benz = 0
    var honda = 0

    var carmonth = emptyArray<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales)

        initViews()

        showLoading("Loading records...")
        Handler().postDelayed({hideLoading()}, 2000)

        totalSales()
        carSalesBar()
        partSales()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Sales Reports"

        dateFilter.setImageDrawable(setDrawable(this, Ionicons.Icon.ion_android_calendar, R.color.textGray, 27))
        date.text = "Report for: Jan 01, 2018 - ${TimeFormatter().getReportTime(System.currentTimeMillis())}"
        dateFilter.hideView()

        dateRangePicker = SmoothDateRangePickerFragment.newInstance(this)
        dateRangePicker.maxDate = Calendar.getInstance()

        dateFilter.setOnClickListener { dateRangePicker.show(fragmentManager, "") }
    }

    private fun totalSales() {
        val salesEntries = mutableListOf<Entry>()

        val toy1 = Entry(0f,800000f)
        salesEntries.add(toy1)
        val toy2 = Entry(1f,950000f)
        salesEntries.add(toy2)
        val toy3 = Entry(2f,1200000f)
        salesEntries.add(toy3)
        val toy4 = Entry(3f,750000f)
        salesEntries.add(toy4)
        val nis1 = Entry(4f,1300000f)
        salesEntries.add(nis1)
        val nis2 = Entry(5f,670000f)
        salesEntries.add(nis2)
        val nis3 = Entry(6f,840000f)
        salesEntries.add(nis3)

        val salesDataSet = LineDataSet(salesEntries, "SALES (KES 25,900,000)")
        salesDataSet.axisDependency = YAxis.AxisDependency.LEFT
        salesDataSet.color = ColorTemplate.COLORFUL_COLORS.asList().random()!!

        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(salesDataSet)

        val lineData = LineData(dataSets)
        totalSales.data = lineData
        totalSales.description = null
        totalSales.setDrawGridBackground(false)
        totalSales.setDrawBorders(false)
        totalSales.setDrawMarkers(false)
        totalSales.axisRight.setDrawLabels(false)
        totalSales.axisLeft.setDrawLabels(false)

        val labels = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun","Jul")

        val xAxis = totalSales.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.valueFormatter = ChartLabelsFormatter(labels)

        val yAxisRight = totalSales.axisRight
        yAxisRight.setDrawLabels(false)
        yAxisRight.setDrawGridLines(false)

        totalSales.invalidate()
    }

    private fun carSalesBar() {
        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(0f, 15f))
        entries.add(BarEntry(1f, 9f))
        entries.add(BarEntry(2f, 5f))
        entries.add(BarEntry(3f, 1f))
        entries.add(BarEntry(4f, 2f))
        entries.add(BarEntry(5f, 3f))

        val labels = arrayOf("Toyota", "Mazda", "Honda", "BMW", "Merc","Subaru")

        val set = BarDataSet(entries, "Sales (Units)")
        set.colors = ColorTemplate.COLORFUL_COLORS.asList()

        val data = BarData(set)
        carSalesBar.data = data
        carSalesBar.setFitBars(true)
        carSalesBar.description = null

        val xAxis = carSalesBar.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = ChartLabelsFormatter(labels)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        val yAxisRight = carSalesBar.axisRight
        yAxisRight.setDrawLabels(false)
        yAxisRight.setDrawGridLines(false)

        carSalesBar.invalidate()
    }

    private fun partSales() {
        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(0f, 32f))
        entries.add(BarEntry(1f, 87f))
        entries.add(BarEntry(2f, 42f))
        entries.add(BarEntry(3f, 51f))

        val labels = arrayOf("Brakes", "Rear door", "Headlight", "Engine oil")

        val set = BarDataSet(entries, "Sales (Quantity)")
        set.colors = ColorTemplate.COLORFUL_COLORS.asList()

        val data = BarData(set)
        partSalesBar.data = data
        partSalesBar.setFitBars(true)
        partSalesBar.description = null

        val xAxis = partSalesBar.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = ChartLabelsFormatter(labels)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        val yAxisRight = partSalesBar.axisRight
        yAxisRight.setDrawLabels(false)
        yAxisRight.setDrawGridLines(false)

        partSalesBar.invalidate()
    }

    override fun onDateRangeSet(view: SmoothDateRangePickerFragment?, yearStart: Int, monthStart: Int, dayStart: Int, yearEnd: Int, monthEnd: Int, dayEnd: Int) {
        var months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

        date.text = "${months[monthStart]} $dayStart, $yearStart - ${months[monthEnd]} $dayEnd, $yearEnd"
        startMonth = monthStart
        endMonth = monthEnd

        for (i in startMonth..endMonth) {
            carmonth.set(carmonth.size, months[i])
        }

        //loadData()
    }

    private fun loadData() {
        showLoading("Loading reports...")

        val client = OkHttpClient()

        val urlBuilder = HttpUrl.parse(K.REPORTS_URL)?.newBuilder()
        urlBuilder?.addQueryParameter("minDate", startMonth.toString())
        urlBuilder?.addQueryParameter("maxDate", endMonth.toString())
        urlBuilder?.addQueryParameter("uid", getUid())

        val url = urlBuilder?.build().toString()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
                runOnUiThread {
                    hideLoading()
                    toast("Error fetching reports. Please try again")
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                runOnUiThread {
                    hideLoading()
                }

                val res = response?.body()?.string()
                Timber.e("RESULTS: $res")
                if (response!!.isSuccessful) {
                    try {
                        val gson = Gson()
                        val json = gson.fromJson(res, JsonObject::class.java)
                        val resultCode = json["resultCode"].asInt
                        runOnUiThread {
                            when(resultCode){
                                -1 -> {
                                    alert(json["description"].asString, "Failed").show()
                                }
                                0 -> {
                                    alert(json["description"].asString, "Success").show()
                                }
                            }
                        }
                    }catch (e:Exception){}
                }
            }
        })
    }

    private fun janSample() {
        val cars = 9
        val parts = 6
        val amnt = 2750000

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.animateEnterLeft(this)
    }
}
