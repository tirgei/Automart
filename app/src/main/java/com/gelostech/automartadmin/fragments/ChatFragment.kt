package com.gelostech.automartadmin.fragments


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automartadmin.R
import com.gelostech.automartadmin.activities.ChatActivity
import com.gelostech.automartadmin.adapters.ChatListAdapter
import com.gelostech.automartadmin.commoners.AppUtils
import com.gelostech.automartadmin.commoners.BaseFragment
import com.gelostech.automartadmin.commoners.K
import com.gelostech.automartadmin.models.ChatItem
import kotlinx.android.synthetic.main.fragment_chat.view.*


class ChatFragment : BaseFragment(), ChatListAdapter.OnItemClickListener {
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
        chat2.message = "Is the Legacy still available?"
        chat2.time = System.currentTimeMillis()
        chat2.username = "Steve Rodgers"
        chatListAdapter.addChat(chat2)

        val chat3 = ChatItem()
        chat3.avatar = R.drawable.person
        chat3.message = "Is the Legacy still available?"
        chat3.time = System.currentTimeMillis()
        chat3.username = "Steve Rodgers"
        chatListAdapter.addChat(chat3)

        val chat4 = ChatItem()
        chat4.avatar = R.drawable.person
        chat4.message = "Is the Legacy still available?"
        chat4.time = System.currentTimeMillis()
        chat4.username = "Steve Rodgers"
        chatListAdapter.addChat(chat4)

        val chat5 = ChatItem()
        chat5.avatar = R.drawable.person
        chat1.message = "Is the Legacy still available?"
        chat1.time = System.currentTimeMillis()
        chat1.username = "Steve Rodgers"
        chatListAdapter.addChat(chat1)
    }

    override fun onItemClickListener(chat: ChatItem) {
        val i = Intent(activity, ChatActivity::class.java)
        i.putExtra(K.CHAT_ID, chat.id)
        i.putExtra(K.CHAT_NAME, chat.username)
        activity!!.startActivity(i)
        AppUtils.animateFadein(activity!!)
    }
}
