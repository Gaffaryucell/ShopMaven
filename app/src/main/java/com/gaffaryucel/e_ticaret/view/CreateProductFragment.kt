package com.gaffaryucel.e_ticaret.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.FragmentCreateProductBinding
import com.gaffaryucel.e_ticaret.model.ProductModel
import com.gaffaryucel.e_ticaret.viewmodel.CreateProductViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.util.*

class CreateProductFragment : Fragment() {
    private lateinit var binding: FragmentCreateProductBinding
    private lateinit var viewModel: CreateProductViewModel

    private var imageUri : Uri? = null
    private var per = false
    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateProductBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            "a@gmail.com","123455"
        ).addOnSuccessListener {
            println("signUp")
        }
        viewModel = ViewModelProvider(requireActivity()).get(CreateProductViewModel::class.java)
        println("fragment")
        binding.productImage.setOnClickListener {
            println("imageClicked")
            if(per){
                showpopup()
            }else{
                requetPermission()
            }
        }
        binding.saveButton.setOnClickListener {
            if (binding.productName.text.isNotEmpty() &&
                binding.productDescription.text.isNotEmpty() &&
                binding.productPrice.text.isNotEmpty() &&
                binding.productQuantity.text.isNotEmpty()&&
                imageUri != null ){
                val myProduct = ProductModel(
                    binding.productName.text.toString(),
                    binding.productDescription.text.toString(),
                    binding.productCategorySpinner.selectedItem.toString(),
                    binding.productPrice.text.toString().toDouble(),
                    binding.productQuantity.text.toString().toInt(),
                    imageUri!!
                )
                binding.productName.setText("")
                binding.productDescription.setText("")
                binding.productCategorySpinner.selectedItem
                binding.productPrice.setText("")
                binding.productQuantity.setText("")
                binding.productImage.setImageResource(R.drawable.product    )
                viewModel.addProduct(
                    myProduct
                )
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
    private fun requetPermission() {
        if (allPermissionsGranted()) {
            // İzinler zaten kabul edildi
            println("T!")
            per = true
            showpopup()
            //intent yapılabilir
        } else {
            // İzinlerin istenmesi gerekiyor
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
            println("F!")
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                per = true
            } else {
                Toast.makeText(requireContext(), "İzinler kabul edilmedi", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PERMISSIONS && resultCode == RESULT_OK && data != null && data.data != null) {
            val myImageUri = data.data
            val bitmap = uriToBitmap(myImageUri!!)
            imageUri = myImageUri
            binding.productImage.setImageBitmap(bitmap)
        }else if (requestCode == REQUEST_CODE_PERMISSIONS && resultCode == RESULT_OK && data != null) {
            val bitmap = data.extras?.get("data") as Bitmap
            binding.productImage.setImageBitmap(bitmap)
            bitmapToFile(bitmap)
        }
    }

    fun showpopup(){
        //inlater kısmı
        println("inPopUp")
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.popup_menu, null)
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
        option1.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, REQUEST_CODE_PERMISSIONS)
            popupWindow.dismiss()
        }
        option2.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(cameraIntent, REQUEST_CODE_PERMISSIONS)
            }
            popupWindow.dismiss()
        }
    }
    fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireActivity().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun bitmapToFile(bitmap: Bitmap) {
        val filesDir = requireContext().filesDir
        val imageFile = File(filesDir, "image.jpg")
        val os = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.flush()
        os.close()
        imageUri =  Uri.fromFile(imageFile)
    }
}