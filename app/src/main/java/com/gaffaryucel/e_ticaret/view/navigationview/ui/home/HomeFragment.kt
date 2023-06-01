package com.gaffaryucel.e_ticaret.view.navigationview.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.adapter.ProductAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentHomeBinding
import com.gaffaryucel.e_ticaret.model.User
import com.gaffaryucel.e_ticaret.viewmodel.Product
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private var adapter = ProductAdapter()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel : HomeViewModel
    private var myAllProducts = ArrayList<Product>()
    private var price1 = 0
    private var price2 = 0
    private var priceChanged = false
    private var myCategory = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getProducts()
        adapter = ProductAdapter()
        binding.recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        binding.cancell.setOnClickListener{
            myCategory = ""
            priceChanged = false
            homeViewModel.getProducts()
        }
        binding.apply.setOnClickListener {
            if (!myCategory.equals("")){
                println("click : "+myCategory)
                homeViewModel.orderByCategory(myCategory)
            }else{
                println("click : a")
                homeViewModel.getProducts()
            }
        }
        binding.categoryIcon.setOnClickListener{
            categoryPopUp()
        }
        binding.priceIcon.setOnClickListener{
            pricePopUp()
        }
        binding.ratingIcon.setOnClickListener{
            pointPopUp()
        }
        observeLiveData()
        val arg = arguments?.getString("from")
        if (arg != null){
            val from = arguments?.getString("from")
            var where = arguments?.getString("where")
            val customer = arguments?.getString("customer")
            val supplier = arguments?.getString("supplier")
            val id = arguments?.getString("id")
            if (where.equals("ask")){
                where = "order"
            }else{
                where = "ask"
            }
            val temp = TempModel()
            temp.from = from
            temp.where = where
            temp.customer = customer
            temp.supplier = supplier
            temp.id = id
            moveFromNotification(temp)
        }else{
            println("lşaskdfgilşdgjfdğkligjdfiklşgjkdfşlgk")
        }
    }
    fun observeLiveData(){
        homeViewModel.products.observe(viewLifecycleOwner,Observer{  myproductList ->
            println("observing")
            val newList = ArrayList<Product>()
            if (priceChanged) {
                println("true")
                myproductList.forEach{
                    println("myprice : "+it.price)
                    if (it.price!!.toInt() <= price2 && it.price.toInt() >= price1){
                        println("myprice 1 : "+it.price)
                        newList.add(it)
                    }
                }
                adapter.productList = newList
            }else{
                adapter.productList = myproductList as ArrayList
            }
            binding.recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        })
        homeViewModel.orderEdProduct.observe(viewLifecycleOwner, Observer {myproductList->
            val newList = ArrayList<Product>()
            if (priceChanged) {
                println("true")
                myproductList.forEach{
                    println("myprice : "+it.price)
                    if (it.price!!.toInt() <= price2 && it.price.toInt() >= price1){
                        println("myprice 1 : "+it.price)
                        newList.add(it)
                    }
                }
                adapter.productList = newList
            }else{
                adapter.productList = myproductList as ArrayList
            }
            binding.recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun pricePopUp() {
        val inflater = LayoutInflater.from(requireContext())
        val firstLayout = inflater.inflate(R.layout.price_layout, null)

        val popupWindow = PopupWindow(
            firstLayout,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_up_anim)
        popupWindow.animationStyle = android.R.style.Animation_Dialog
        popupWindow.contentView.startAnimation(animation)

        popupWindow.showAtLocation(
            view,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )

        val option1 = firstLayout.findViewById<TextView>(R.id.option_low)
        val option2 = firstLayout.findViewById<TextView>(R.id.option_medium)
        val option3 = firstLayout.findViewById<TextView>(R.id.option_high)

        option1.setOnClickListener {
            price1 = 0
            price2 = 50
            priceChanged = true
            popupWindow.dismiss()
        }
        option2.setOnClickListener {
            price1 = 50
            price2 = 100
            priceChanged = true
            popupWindow.dismiss()
        }
        option3.setOnClickListener {
            price1 = 100
            price2 = 1000000
            priceChanged = true
            popupWindow.dismiss()
        }
    }
    @SuppressLint("MissingInflatedId")
    fun pointPopUp() {
        val inflater = LayoutInflater.from(requireContext())
        val firstLayout = inflater.inflate(R.layout.point_layout, null)

        val popupWindow = PopupWindow(
            firstLayout,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_up_anim)
        popupWindow.animationStyle = android.R.style.Animation_Dialog
        popupWindow.contentView.startAnimation(animation)

        popupWindow.showAtLocation(
            view,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )

        val option1 = firstLayout.findViewById<TextView>(R.id.low_point)
        val option2 = firstLayout.findViewById<TextView>(R.id.mid_point)
        val option3 = firstLayout.findViewById<TextView>(R.id.high_point)


        option1.setOnClickListener {

            popupWindow.dismiss()
        }

        option2.setOnClickListener {

            popupWindow.dismiss()
        }
        option3.setOnClickListener {

            popupWindow.dismiss()
        }
    }

    fun categoryPopUp(){
        val inflater = LayoutInflater.from(requireContext())
        val firstLayout = inflater.inflate(R.layout.category_pop_up, null)
        val popupWindow = PopupWindow(
            firstLayout,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_up_anim)
        popupWindow.animationStyle = android.R.style.Animation_Dialog
        popupWindow.contentView.startAnimation(animation)
        popupWindow.showAtLocation(
            requireView(),
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )
        val option1 = firstLayout.findViewById<TextView>(R.id.option_rating_1)
        val option2 = firstLayout.findViewById<TextView>(R.id.option_rating_2)
        val option3 = firstLayout.findViewById<TextView>(R.id.option_rating_3)
        val option4 = firstLayout.findViewById<TextView>(R.id.option_rating_4)

        option1.setOnClickListener{
            myCategory ="Kategori 1"
            popupWindow.dismiss()
        }

        option2.setOnClickListener{
            myCategory ="Kategori 2"
            popupWindow.dismiss()
        }
        option3.setOnClickListener{
            myCategory ="Kategori 3"
            popupWindow.dismiss()
        }
        option4.setOnClickListener{
            myCategory ="Kategori 4"
            popupWindow.dismiss()
        }
    }
    private fun moveFromNotification(temp : TempModel){
        if (temp.from.equals("message")){
            val action = HomeFragmentDirections.actionNavHomeToMessageFragment(temp.customer.toString(),temp.supplier.toString(),temp.from.toString())
            Navigation.findNavController(requireView()).navigate(action)
        }else if(temp.from.equals("order")){
            println("ffffffffff : "+temp.id)
            val action = HomeFragmentDirections.actionNavHomeToOrderDetailsForCustomer(temp.id ?: "")
            Navigation.findNavController(requireView()).navigate(action)
        }
    }
}
class TempModel{
    var from   : String? = null
    var where : String? =null
    var customer : String? =null
    var supplier : String? = null
    var id : String? = null
    constructor()
    constructor(
        from :String,
        where :String,
        customer : String,
        supplier : String,
        id : String
    ){
        this.from  =from
        this.where= where
        this.customer =customer
        this.supplier = supplier
        this.id  =id
    }
}