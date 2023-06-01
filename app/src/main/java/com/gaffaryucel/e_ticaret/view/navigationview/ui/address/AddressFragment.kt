package com.gaffaryucel.e_ticaret.view.navigationview.ui.address

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.adapter.AddressAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentAddNewCardBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentAddressBinding
import com.gaffaryucel.e_ticaret.model.AddressModel
import com.gaffaryucel.e_ticaret.model.CardModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.address.newaddress.AddNewAddressFragment
import com.gaffaryucel.e_ticaret.view.navigationview.ui.card.CardViewModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.card.newcard.AddNewCardFragment

class AddressFragment : Fragment() {

    private lateinit var binding : FragmentAddressBinding
    private lateinit var viewModel: AddressViewModel
    private var adapter = AddressAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        binding.addressrecyclerview.layoutManager =  LinearLayoutManager(requireContext())
        binding.addressrecyclerview.adapter = adapter
        viewModel.getAddress()
        observeLiveData()
        binding.addaddressbutton.setOnClickListener {
            val dialog = AddNewAddressFragment()
            dialog.show(childFragmentManager, "MyDialogFragmentTag")
        }
    }
    fun observeLiveData(){
        viewModel.address.observe(viewLifecycleOwner, Observer{
            adapter.addressList = it as ArrayList<AddressModel>
            adapter.notifyDataSetChanged()
        })
    }
}