package com.gaffaryucel.e_ticaret.view.navigationview.ui.address.newaddress

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.FragmentAddNewAddressBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentAddNewCardBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentAddressBinding
import com.gaffaryucel.e_ticaret.model.AddressModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.card.newcard.AddNewCardViewModel

class AddNewAddressFragment : DialogFragment() {

    private lateinit var binding: FragmentAddNewAddressBinding
    private lateinit var viewModel: AddNewAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewAddressBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddNewAddressViewModel::class.java)
        // TODO: Use the ViewModel
        binding.buttonSubmit.setOnClickListener {
            if (isFieldsValid()) {
                val street = binding.editTextStreet.text.toString()
                val neighborhood = binding.editTextNeighborhood.text.toString()
                val city = binding.editTextCity.text.toString()
                val district = binding.editTextDistrict.text.toString()
                val postalCode = binding.editTextPostalCode.text.toString()

                val address = AddressModel(street, neighborhood, city, district, postalCode)
                viewModel.addNewAddress(address)
                dialog!!.dismiss()
            }
        }
    }

    private fun isFieldsValid(): Boolean {
        val street = binding.editTextStreet.text.toString()
        val neighborhood = binding.editTextNeighborhood.text.toString()
        val city = binding.editTextCity.text.toString()
        val district = binding.editTextDistrict.text.toString()
        val postalCode = binding.editTextPostalCode.text.toString()
        return street.isNotEmpty() && neighborhood.isNotEmpty() && city.isNotEmpty() && district.isNotEmpty() && postalCode.isNotEmpty()
    }
    fun observeLiveData(){
        viewModel.success.observe(viewLifecycleOwner, Observer {
            dialog?.dismiss()
        })
    }
}