package com.gaffaryucel.e_ticaret.view.navigationview.ui.card

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.adapter.CardAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentCardBinding
import com.gaffaryucel.e_ticaret.model.CardModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.card.newcard.AddNewCardFragment
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder

class CardFragment : Fragment() {

    private lateinit var viewModel: CardViewModel
    private lateinit var binding : FragmentCardBinding
    private val adapter = CardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        binding.recyclerView3.layoutManager =  LinearLayoutManager(requireContext())
        binding.recyclerView3.adapter = adapter
        viewModel.getCardInfo()
        observeLiveData()
        binding.createcardbutton.setOnClickListener {
            val dialog = AddNewCardFragment()
            dialog.show(childFragmentManager, "MyDialogFragmentTag")
        }
    }
    fun observeLiveData(){
        viewModel.cards.observe(viewLifecycleOwner,Observer{
            adapter.cardlist = it as ArrayList<CardModel>
            adapter.notifyDataSetChanged()
        })
    }
}