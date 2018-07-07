package com.gelostech.automart.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import com.gelostech.automart.R
import com.gelostech.automart.adapters.ChatAdapter
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.AppUtils.setDrawable
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Chat
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.toast

class ChatActivity : BaseActivity(), View.OnClickListener {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatName: String
    private var hasTyped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatName = intent.getStringExtra(K.CHAT_NAME)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = chatName
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //toolbar.setLogo(R.drawable.person)

//        var image = BitmapFactory.decodeResource(resources, R.drawable.person)
//        image = Bitmap.createScaledBitmap(image, 100, 100, true)
//        supportActionBar?.setIcon(BitmapDrawable(resources, image))

        editTextListener()
        send.setImageDrawable(setDrawable(this, FontAwesome.Icon.faw_paper_plane, R.color.colorPrimaryLight, 22))

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = DefaultItemAnimator()

        chatAdapter = ChatAdapter()
        rv.adapter = chatAdapter

        loadSample()
    }

    // listens for changes on message edittext
    private fun editTextListener() {
        message.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty() || s.isBlank()) {
                    hasTyped = false
                    send.setImageDrawable(setDrawable(this@ChatActivity, FontAwesome.Icon.faw_paper_plane, R.color.colorPrimaryLight, 22))

                } else {
                    hasTyped = true
                    send.setImageDrawable(setDrawable(this@ChatActivity, FontAwesome.Icon.faw_paper_plane, R.color.colorPrimary, 22))

                }
            }
        })
    }

    private fun loadSample() {
        val chat1 = Chat("123", "123", "123", "123", "Hellow", System.currentTimeMillis(), false)
        chatAdapter.addChat(chat1)

        val chat2 = Chat("123", "123", "123", "123", "Hi.. How can i help you?", System.currentTimeMillis(), true)
        chatAdapter.addChat(chat2)

        val chat3 = Chat("123", "123", "123", "123", "I'm looking for BMW 325i series. Do you have any currently in stock?", System.currentTimeMillis(), false)
        chatAdapter.addChat(chat3)

        val chat4 = Chat("123", "123", "123", "123", "Yeah. We have 2 available, black and grey. When do you want to come see them?", System.currentTimeMillis(), true)
        chatAdapter.addChat(chat4)

        val chat5 = Chat("123", "123", "123", "123", "Any day next week", System.currentTimeMillis(), false)
        chatAdapter.addChat(chat5)

        val chat6 = Chat("123", "123", "123", "123", "Okay. You're welcome", System.currentTimeMillis(), true)
        chatAdapter.addChat(chat6)

        rv.smoothScrollToPosition(chatAdapter.lastPosition())
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            send.id -> if (hasTyped) {
                val chat = Chat("123", "123", "123", "123", "${message.text}", System.currentTimeMillis(), true)
                chatAdapter.addChat(chat)
                message.setText("")
            }
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
