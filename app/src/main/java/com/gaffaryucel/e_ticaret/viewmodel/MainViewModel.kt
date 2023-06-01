package com.gaffaryucel.e_ticaret.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class MainViewModel : ViewModel() {
    private val disposables = CompositeDisposable()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val chatResponse = MutableLiveData<String>()

}