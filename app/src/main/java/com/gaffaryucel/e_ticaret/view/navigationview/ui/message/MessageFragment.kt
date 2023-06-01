package com.gaffaryucel.e_ticaret.view.navigationview.ui.message

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.adapter.IncomingOrdersAdapter
import com.gaffaryucel.e_ticaret.adapter.MessageAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentIncomingOrderBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentMessageBinding
import com.gaffaryucel.e_ticaret.model.*
import com.gaffaryucel.e_ticaret.retrofit.FcmInterface
import com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.CustomerMessagesFragment
import com.gaffaryucel.e_ticaret.view.navigationview.ui.incomingorder.IncomingOrderViewModel
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var  viewModel : MessageViewModel
    private lateinit var adapter : MessageAdapter
    private var spl : Boolean? =null
    private var senderName : String? =null
    private var myToken : String? = null
    private var userToken = ""
    val from = ""
    val customer = ""
    val supplier = ""
    companion object{
        var isMessagesOpen = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val from =     arguments?.getString("from").toString()
        val customer = arguments?.getString("cid").toString()
        val supplier = arguments?.getString("sid").toString()
        var mymodel = NotificationModel(from,customer,supplier)
        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        if (userid == customer){
            viewModel.getToken(supplier)
        }else if (userid == supplier){
            viewModel.getToken(customer)
        }
        viewModel.isSupplierSaved(supplier.toString(),customer.toString())
        if(from.equals("ask")){
            //gören müşteri
            viewModel.getSenderName(customer.toString())
            spl = false
            adapter = MessageAdapter("customer")
        }else{
            //gören satıcı
            viewModel.getSenderName(supplier.toString())
            spl= true
            adapter = MessageAdapter("supplier")
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getAllMessages(supplier.toString(),customer.toString())
        binding.btnSend.setOnClickListener{
            val customermessage = binding.etMessageInput.text.toString()
            val currentTime = getCurrentTime()
            val messageObject = MessageModel()
            if (!customermessage.isNullOrEmpty()){
                messageObject.timestamp = currentTime
                messageObject.messageContent = customermessage
                messageObject.sender = customer
                messageObject.supplier = spl
                messageObject.sendername = senderName
                val messageId = UUID.randomUUID().toString()
                messageObject.messageId = messageId
                viewModel.sendmessage(messageObject,supplier,customer)
                binding.etMessageInput.setText("")
                viewModel.sendToUser(senderName.toString(),customermessage,userToken,mymodel)
            }
        }
        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.messages.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                adapter.messagelist = it as ArrayList
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.senderName.observe(viewLifecycleOwner, Observer {
            senderName = it.toString()
        })
        viewModel.token.observe(viewLifecycleOwner, Observer {
            userToken = it
        })
    }
    fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    override fun onStart() {
        super.onStart()
        isMessagesOpen = true
    }
    override fun onStop() {
        super.onStop()
        isMessagesOpen = false
    }
    // Bildirim gösterme izni isteği için fonksiyon
}