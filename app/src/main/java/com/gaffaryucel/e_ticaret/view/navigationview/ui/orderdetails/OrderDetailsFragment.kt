package com.gaffaryucel.e_ticaret.view.navigationview.ui.orderdetails

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.adapter.OrderAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentIncomingOrderBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentOrderDetailsBinding
import com.gaffaryucel.e_ticaret.model.Notification
import com.gaffaryucel.e_ticaret.model.NotificationBody
import com.gaffaryucel.e_ticaret.retrofit.FcmInterface
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

class OrderDetailsFragment : Fragment() {

    private lateinit var viewModel: OrderDetailsViewModel
    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!
    private var order = CustomerOrder()
    private var myToken = ""
    private var myid = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater,container,false)
        val root : View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderDetailsViewModel::class.java)
        // TODO: Use the ViewModel
        myid =arguments?.getString("id").toString()
        viewModel.getOrder(myid)
        observeLiveData()
        binding.changestatusbutton.setOnClickListener {
            showpopup()
        }
    }
    private fun observeLiveData() {
        viewModel.incomingorder.observe(viewLifecycleOwner, Observer {
            binding.apply {
                product = it
                executePendingBindings()
            }
            order = it
            viewModel.getToken(it.customer.toString())
            println("c : "+order.customer)
            Glide.with(requireContext()).load(it.imageUrl).into(binding.productImage)
        })
        viewModel.userToken.observe(viewLifecycleOwner, Observer {
            myToken = it
        })
    }
    fun showpopup(){
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.popup_change_status, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.popup_animation)
        popupWindow.animationStyle = android.R.style.Animation_Dialog
        popupWindow.contentView.startAnimation(animation)

        popupWindow.showAtLocation(
            requireView(),
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )
        val option1 = popupView.findViewById<TextView>(R.id.option1)
        val option2 = popupView.findViewById<TextView>(R.id.option2)
        val option3 = popupView.findViewById<TextView>(R.id.option3)
        val option4 = popupView.findViewById<TextView>(R.id.option4)
        val option5 = popupView.findViewById<TextView>(R.id.option5)
        val option6 = popupView.findViewById<TextView>(R.id.option6)
        option1.setOnClickListener {
            viewModel.updateStatusAndSyncWithCustomer(order.id.toString(),"Ordered",order.customer.toString())
            popupWindow.dismiss()
            viewModel.sendToUser("Ordered",order.name.toString(),order.imageUrl.toString(),myToken.toString(),myid)
        }
        option2.setOnClickListener {
            viewModel.updateStatusAndSyncWithCustomer(order.id.toString(),"getting ready",order.customer.toString())
            popupWindow.dismiss()
            viewModel.sendToUser("getting ready",order.name.toString(),order.imageUrl.toString(),myToken.toString(),myid)
        }
        option3.setOnClickListener {
            viewModel.updateStatusAndSyncWithCustomer(order.id.toString(),"given to the shipping company",order.customer.toString())
            popupWindow.dismiss()
            viewModel.sendToUser("given to the shipping company",order.name.toString(),order.imageUrl.toString(),myToken,myid)
        }
        option4.setOnClickListener {
            viewModel.updateStatusAndSyncWithCustomer(order.id.toString(),"set off",order.customer.toString())
            popupWindow.dismiss()
            viewModel.sendToUser("set off",order.name.toString(),order.imageUrl.toString(),myToken,myid)
        }
        option5.setOnClickListener {
            viewModel.updateStatusAndSyncWithCustomer(order.id.toString(),"at the delivery branch",order.customer.toString())
            popupWindow.dismiss()
            viewModel.sendToUser("at the delivery branch",order.name.toString(),order.imageUrl.toString(),myToken,myid)
        }
        option6.setOnClickListener {
            viewModel.updateStatusAndSyncWithCustomer(order.id.toString(),"delivered",order.customer.toString())
            popupWindow.dismiss()
            viewModel.sendToUser("delivered",order.name.toString(),order.imageUrl.toString(),myToken,myid)
        }
    }
}