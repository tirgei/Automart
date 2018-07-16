package com.gelostech.automart.fragments


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.activities.ChatActivity
import com.gelostech.automart.adapters.ChatListAdapter
import com.gelostech.automart.callbacks.ChatListCallback
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.ChatItem
import kotlinx.android.synthetic.main.fragment_chat.view.*


class ChatFragment : BaseFragment(), ChatListCallback {
    private lateinit var chatListAdapter: ChatListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(v: View) {
        v.rv.setHasFixedSize(true)
        v.rv.layoutManager = LinearLayoutManager(activity!!)
        v.rv.itemAnimator = DefaultItemAnimator()

        chatListAdapter = ChatListAdapter(this)
        v.rv.adapter = chatListAdapter
        v.rv.showShimmerAdapter()

        Handler().postDelayed({
            v.rv.hideShimmerAdapter()
            loadSample()
        }, 2500)
    }

    private fun loadSample() {
        val chat1 = ChatItem()
        chat1.avatar = R.drawable.person
        chat1.message = "Is the Legacy still available?"
        chat1.time = System.currentTimeMillis()
        chat1.username = "Steve Rodgers"
        chatListAdapter.addChat(chat1)

        val chat2 = ChatItem()
        chat2.avatar = R.drawable.person
        chat2.message = "I'm looking for Toyota NZE 2007. Can I get it?"
        chat2.time = System.currentTimeMillis()
        chat2.username = "Mike Njuguna"
        chatListAdapter.addChat(chat2)

        val chat3 = ChatItem()
        chat3.avatar = R.drawable.person
        chat3.message = "Naweza pata gari ya 300K?"
        chat3.time = System.currentTimeMillis()
        chat3.username = "Juma Allan"
        chatListAdapter.addChat(chat3)

        val chat4 = ChatItem()
        chat4.avatar = R.drawable.person
        chat4.message = "How much is the Honda Airwave?"
        chat4.time = System.currentTimeMillis()
        chat4.username = "Peter Kiprotich"
        chatListAdapter.addChat(chat4)

        val chat5 = ChatItem()
        chat5.avatar = R.drawable.person
        chat1.message = "No.. I was looking for 2011 at least"
        chat1.time = System.currentTimeMillis()
        chat1.username = "George Omondi"
        chatListAdapter.addChat(chat1)
    }

    override fun onClick(chat: ChatItem) {
        val i = Intent(activity, ChatActivity::class.java)
        i.putExtra(K.CHAT_ID, chat.id)
        i.putExtra(K.CHAT_NAME, chat.username)
        activity!!.startActivity(i)
        AppUtils.animateFadein(activity!!)
    }
}
