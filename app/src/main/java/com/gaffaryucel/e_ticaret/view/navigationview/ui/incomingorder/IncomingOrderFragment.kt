package com.gaffaryucel.e_ticaret.view.navigationview.ui.incomingorder

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.adapter.IncomingOrdersAdapter
import com.gaffaryucel.e_ticaret.adapter.OrderAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentGalleryBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentIncomingOrderBinding
import com.gaffaryucel.e_ticaret.model.Notification
import com.gaffaryucel.e_ticaret.model.NotificationBody
import com.gaffaryucel.e_ticaret.retrofit.FcmInterface
import com.gaffaryucel.e_ticaret.view.navigationview.ui.gallery.GalleryViewModel
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

class IncomingOrderFragment : Fragment() {

    private var _binding: FragmentIncomingOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var  viewModel : IncomingOrderViewModel
    private var adapter = IncomingOrdersAdapter()
    private var orderlist = ArrayList<CustomerOrder>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomingOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(IncomingOrderViewModel::class.java)
        viewModel.showIncomingOrder()
        binding.ordersRecyclerView.layoutManager =  LinearLayoutManager(requireContext())
        binding.ordersRecyclerView.adapter = adapter
        observeLiveData()
    }

    fun observeLiveData(){
        viewModel.orders.observe(viewLifecycleOwner, Observer {
            if (it.size == 0){
                binding.ordertext.visibility = View.VISIBLE
            }else {
                binding.ordertext.visibility = View.INVISIBLE
                adapter.orderList = it as ArrayList<CustomerOrder>
                adapter.notifyDataSetChanged()
                orderlist = it
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}