package com.gaffaryucel.e_ticaret.view.navigationview.ui.card.newcard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.get
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.FragmentAddNewCardBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentCardBinding
import com.gaffaryucel.e_ticaret.model.CardModel

class AddNewCardFragment : DialogFragment() {

    private lateinit var viewModel: AddNewCardViewModel
    private lateinit var binding: FragmentAddNewCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewCardBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isfrontvisible = false
        viewModel = ViewModelProvider(this).get(AddNewCardViewModel::class.java)
        binding.cardrowback.setBackgroundResource(R.drawable.card1)
        binding.cardnumtext.visibility = View.VISIBLE
        binding.nameoncardtext.visibility = View.VISIBLE

        binding.mounthoncardtext.visibility = View.INVISIBLE
        binding.yearoncardtext.visibility = View.INVISIBLE
        binding.cvv.visibility = View.INVISIBLE
        binding.slashtext.visibility = View.INVISIBLE
        binding.cardrowback.setOnClickListener{
            if (isfrontvisible) {
                binding.cardrowback.setBackgroundResource(R.drawable.card1)
                binding.cardnumtext.visibility = View.VISIBLE
                binding.nameoncardtext.visibility = View.VISIBLE

                binding.mounthoncardtext.visibility = View.INVISIBLE
                binding.yearoncardtext.visibility = View.INVISIBLE
                binding.cvv.visibility = View.INVISIBLE
                binding.slashtext.visibility = View.INVISIBLE
                isfrontvisible = false
            }else{
                binding.cardrowback.setBackgroundResource(R.drawable.car2)
                binding.cardnumtext.visibility = View.INVISIBLE
                binding.nameoncardtext.visibility = View.INVISIBLE

                binding.mounthoncardtext.visibility = View.VISIBLE
                binding.yearoncardtext.visibility = View.VISIBLE
                binding.cvv.visibility = View.VISIBLE
                binding.slashtext.visibility = View.VISIBLE
                isfrontvisible = true
            }
        }
        binding.savecard.setOnClickListener {
            if (binding.cardnametext.text.isNotEmpty()
                &&binding.cardnumtext.text.isNotEmpty()&&binding.nameoncardtext.text.isNotEmpty()
                &&binding.mounthoncardtext.text.isNotEmpty() && binding.yearoncardtext.text.isNotEmpty()
                && binding.cvv.text.isNotEmpty()
            ){
                var card  = CardModel()
                val cardnum = binding.cardnumtext.text.toString()
                val cardowner = binding.nameoncardtext.text.toString()
                val mountht = binding.mounthoncardtext.text.toString()
                val year = binding.yearoncardtext.text.toString()
                val cvv = binding.cvv.text.toString()
                val cardname = binding.cardnametext.text.toString()
                card.cardname = cardname
                card.name = cardowner
                card.num = cardnum
                card.mounth = mountht
                card.year = year
                card.cvv = cvv
                card.main = false
                viewModel.addNewCard(card)
                dialog?.dismiss()
            }else{
                Toast.makeText(requireContext(), "You have to enter all info", Toast.LENGTH_SHORT).show()
            }
        }
    }
}