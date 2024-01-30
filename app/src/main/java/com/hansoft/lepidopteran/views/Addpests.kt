package com.hansoft.lepidopteran.views

//noinspection SuspiciousImport
import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.hansoft.lepidopteran.database.pestsdatabase.Pest
import com.hansoft.lepidopteran.database.pestsdatabase.PestViewModel
import com.hansoft.lepidopteran.databinding.ActivityAddpestsBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Suppress("DEPRECATION")
class Addpests : AppCompatActivity() {
    private lateinit var binding:ActivityAddpestsBinding
    private var impact = ""
    private val SELECT_IMAGE_REQUEST = 1
    private var imagepath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddpestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, com.hansoft.lepidopteran.R.color.greenColor)

        val items = arrayOf("High", "Medium", "low")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                impact = parentView?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                impact = "High"
            }
        }

        binding.capturePhotoButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST)
        }

        binding.selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST)
        }

        binding.save.setOnClickListener {
            if(binding.commonname.text.isEmpty() || binding.commonname.text.isEmpty() ||
                binding.commonname.text.isEmpty() || imagepath == "" || impact == ""){
                Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show()
            } else {
                val pestViewModel = ViewModelProvider(this).get(PestViewModel::class.java)
                val p = Pest(
                    0, imagepath, binding.commonname.text.toString(),
                    binding.ScientificName.text.toString(),
                    binding.BriefDescription.text.toString(), impact
                )
                pestViewModel.insert(p)
                finish()
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!(requestCode != SELECT_IMAGE_REQUEST || resultCode != Activity.RESULT_OK || data == null)) {
            if(data.data.toString() != "null") {
                val selectedImageUri: Uri? = data.data
                imagepath = selectedImageUri?.toString().toString()
                binding.imageView.setImageURI(selectedImageUri)
            } else {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val imageFile = saveBitmapToFile(imageBitmap)
                imagepath = imageFile?.absolutePath ?: ""
                binding.imageView.setImageBitmap(imageBitmap)
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File? {
        val imageFile = File(externalCacheDir, "temp_image.jpg")
        try {
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.close()
            return imageFile
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}