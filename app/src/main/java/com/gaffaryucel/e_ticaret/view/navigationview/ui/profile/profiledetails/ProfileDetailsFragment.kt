package com.gaffaryucel.e_ticaret.view.navigationview.ui.profile.profiledetails

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.EntryActivity
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.FragmentProfileBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentProfileDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class ProfileDetailsFragment : Fragment() {

    private lateinit var viewModel: ProfileDetailsViewModel
    private lateinit var binding : FragmentProfileDetailsBinding

    private var imageUri : Uri? = null
    private var per = false
    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private var profilechanged = false
    private var namechanged = false
    private var emailchanged = false
    private var userName = ""
    private var eMail = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileDetailsBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileDetailsViewModel::class.java)
        viewModel.getProfileDetails(FirebaseAuth.getInstance().currentUser!!.uid)
        observeLiveData()
        binding.profileImage.setOnClickListener{
            requetPermission()
            editforimage()
        }

        binding.editimage.setOnClickListener{
            edit()
        }
        binding.saveimage.setOnClickListener{
            val mymail = binding.changeEmailedittext.text.toString()
            val myusername = binding.changeusernameedittext.text.toString()
            save()
            if (profilechanged && imageUri != null){
                viewModel.changeProfilPhoto(imageUri!!)
            }
            if (mymail.isNotEmpty()){
                if (!mymail.equals(eMail)){
                    alert(mymail)
                }else{
                    println("eşit : "+mymail)
                }
            }
            if (myusername.isNotEmpty()){
                if (!myusername.equals(userName)){
                    viewModel.changeUserName(myusername)
                }else{
                    println("eşit : "+myusername)
                }
            }
        }
    }
    private fun editforimage(){
        binding.saveimage.visibility = View.VISIBLE
        binding.editimage.visibility = View.INVISIBLE
    }
    private fun edit(){
        binding.saveimage.visibility = View.VISIBLE
        binding.editimage.visibility = View.INVISIBLE
        binding.changeusernameedittext.visibility = View.VISIBLE
        binding.changeEmailedittext.visibility = View.VISIBLE
        binding.nameTextView.visibility = View.GONE
        binding.emailTextView.visibility = View.GONE
    }
    private fun save(){
        binding.editimage.visibility = View.VISIBLE
        binding.saveimage.visibility = View.INVISIBLE
        binding.changeusernameedittext.visibility = View.GONE
        binding.changeEmailedittext.visibility = View.GONE
        binding.nameTextView.visibility = View.VISIBLE
        binding.emailTextView.visibility = View.VISIBLE
    }
    private fun observeLiveData(){
        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding.apply {
                info = it
                userName = it.userName.toString()
                eMail = it.eMail.toString()
            }
            if(it.photo != null){
                Glide.with(requireContext()).load(it.photo).into(binding.profileImage)
            }else{
                binding.profileImage.setImageResource(R.drawable.profile)
            }
        })
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
    private fun requetPermission() {
        if (allPermissionsGranted()) {
            // İzinler zaten kabul edildi
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
        if (requestCode == REQUEST_CODE_PERMISSIONS && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val myImageUri = data.data
            val bitmap = uriToBitmap(myImageUri!!)
            imageUri = myImageUri
            binding.profileImage.setImageBitmap(bitmap)
            profilechanged = true
        }else if (requestCode == REQUEST_CODE_PERMISSIONS && resultCode == Activity.RESULT_OK && data != null) {
            val bitmap = data.extras?.get("data") as Bitmap
            binding.profileImage.setImageBitmap(bitmap)
            bitmapToFile(bitmap)
            profilechanged = true
        }
    }

    fun showpopup(){
        //inlater kısmı
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
        val imageFile = File(filesDir, "prfoilphoto.jpg")
        val os = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.flush()
        os.close()
        imageUri =  Uri.fromFile(imageFile)
    }
    fun verification(){
        val current = FirebaseAuth.getInstance().currentUser
        if (current != null) {
            current.sendEmailVerification().addOnCompleteListener {
                if (it.isSuccessful) {

                } else {
                    Toast.makeText(requireContext(), "Error1", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(requireContext(), "ERROR2", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun alert(newmail : String){
        val alert = AlertDialog.Builder(context)
            .setTitle("attention")
            .setMessage("Are you sure you want to change your e-mail address? \n new e-mail address : $newmail")
            .setPositiveButton("send verification mail", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val user = FirebaseAuth.getInstance().currentUser
                    viewModel.changeEmail(newmail)
                    user!!.updateEmail(newmail)
                    verification()
                    showverifypopup()
                }
            }).setNegativeButton("don't change e-mail", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            })
            .create()
        alert.show()
    }
    @SuppressLint("MissingInflatedId")
    fun showverifypopup(){
        //inlater kısmı
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.verification_popup, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setOnDismissListener {
            val intent = Intent(requireActivity(),EntryActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
            FirebaseAuth.getInstance().signOut()
        }

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
        val verified = popupView.findViewById<Button>(R.id.verifiedbutton)
        val resend = popupView.findViewById<Button>(R.id.resendbutton)
        val timer = popupView.findViewById<TextView>(R.id.timertext)
        verified.setOnClickListener {
            popupWindow.setOnDismissListener {

            }
            popupWindow.dismiss() 
        }
        resend.setOnClickListener {

        }
        val totalTime: Long = 180000 // 3 dakika = 180000 milisaniye
        val interval: Long = 1000 // 1 saniye = 1000 milisaniye

        val countDownTimer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                val timeLeft = String.format("%02d:%02d", minutes, seconds)

                // Zamanı güncelleyen işlemler burada yapılır
                timer.text = timeLeft
            }

            override fun onFinish() {
                // Süre tamamlandığında yapılacak işlemler burada yapılır
                timer.text = "Time Over"
            }
        }
        countDownTimer.start()
    }
    fun exitAlert(){
        val alert = AlertDialog.Builder(context)
            .setTitle("Alert")
            .setMessage("If you don't verify your e-mail you will be sign out")
            .setPositiveButton("I will verify, stay", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            }).setNegativeButton("Sign Out", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val intent = Intent(requireActivity(),EntryActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                    FirebaseAuth.getInstance().signOut()
                }
            })
            .create()
        alert.show()
    }

}