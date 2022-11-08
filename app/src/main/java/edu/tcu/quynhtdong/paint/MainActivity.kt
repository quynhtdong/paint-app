package edu.tcu.quynhtdong.paint

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawingView = findViewById<DrawingView>(R.id.drawing_view)
        setUpPallet(drawingView)

        findViewById<ImageView>(R.id.brush_iv).setOnClickListener{
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.path_width_selector)
            dialog.show()

            val smallBtn = dialog.findViewById<ImageView>(R.id.small_width_iv)
            smallBtn.setOnClickListener {
                drawingView.setPathWidth(10)
                dialog.dismiss()
            }
            val mediumBtn = dialog.findViewById<ImageView>(R.id.medium_width_iv)
            mediumBtn.setOnClickListener {
                drawingView.setPathWidth(15)
                dialog.dismiss()
            }
            val largeBtn = dialog.findViewById<ImageView>(R.id.large_width_iv)
            largeBtn.setOnClickListener {
                drawingView.setPathWidth(20)
                dialog.dismiss()
            }

        }

        findViewById<ImageView>(R.id.undo_iv).setOnClickListener {
            drawingView.undoPath()
        }



        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            var background = findViewById<ImageView>(R.id.background_iv)
            val dialog = showInProgress()
            if(uri != null) {
                lifecycleScope.launch{
                    var request: RequestBuilder<Drawable>
                    withContext(Dispatchers.IO) {
                        delay(1000)
                        request = Glide.with(this@MainActivity).load(uri.toString())
                    }
                    dialog.dismiss()
                    request.into(background)
                }

            }
            Glide.with(this).load(uri).into(findViewById<ImageView>(R.id.background_iv));
        }
        findViewById<ImageView>(R.id.gallery_iv).setOnClickListener {

            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        val backgroundIv = findViewById<ImageView>(R.id.background_iv)
        if(backgroundIv.drawable == null){
            backgroundIv.setBackgroundColor(Color.WHITE)
        }

        findViewById<ImageView>(R.id.save_iv).setOnClickListener{
            setUpSave()
        }



    }

    private fun setUpSave() {

        val bitmap = findViewById<FrameLayout>(R.id.drawing_fl)
        val values = ContentValues().apply {
            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                System.currentTimeMillis().toString().substring(2, 11) + ".jpeg"
            )
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        }
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val dialog = showInProgress()
            lifecycleScope.launch{
                withContext(Dispatchers.IO) {
                    delay(1000)
                    uri?.let {
                        contentResolver.openOutputStream(it).use { it_->

                            val temp = bitmap.drawToBitmap()
                            temp.compress(Bitmap.CompressFormat.JPEG, 90, it_)

                        }
                    }

                }
                dialog.dismiss()
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "image/jpeg"
                }

                startActivity(Intent.createChooser(shareIntent, null))
            }




    }

    private fun showInProgress(): Dialog {
        var dialog = Dialog(this)
        dialog.setContentView(R.layout.in_progress)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }

    private fun setUpPallet(drawingView: DrawingView) {
        val palletLl = findViewById<LinearLayout>(R.id.pallet_ll)

        palletLl[0].setOnClickListener {
            for(iv in palletLl){
                (iv as ImageView).setImageResource(R.drawable.path_color_normal)
            }
            (palletLl[0] as ImageView).setImageResource(R.drawable.path_color_selected)
            drawingView.setPathColor(R.color.black)
        }
        palletLl[1].setOnClickListener {
            for(iv in palletLl){
                (iv as ImageView).setImageResource(R.drawable.path_color_normal)
            }
            (palletLl[1] as ImageView).setImageResource(R.drawable.path_color_selected)
            drawingView.setPathColor(R.color.red_color)
        }
        palletLl[2].setOnClickListener {
            for(iv in palletLl){
                (iv as ImageView).setImageResource(R.drawable.path_color_normal)
            }
            (palletLl[2] as ImageView).setImageResource(R.drawable.path_color_selected)
            drawingView.setPathColor(R.color.green_color)
        }
        palletLl[3].setOnClickListener {
            for(iv in palletLl){
                (iv as ImageView).setImageResource(R.drawable.path_color_normal)
            }
            (palletLl[3] as ImageView).setImageResource(R.drawable.path_color_selected)
            drawingView.setPathColor(R.color.blue_color)
        }
        palletLl[4].setOnClickListener {
            for(iv in palletLl){
                (iv as ImageView).setImageResource(R.drawable.path_color_normal)
            }
            (palletLl[4] as ImageView).setImageResource(R.drawable.path_color_selected)
            drawingView.setPathColor(R.color.purple_color)
        }
        palletLl[5].setOnClickListener {
            for(iv in palletLl){
                (iv as ImageView).setImageResource(R.drawable.path_color_normal)
            }
            (palletLl[5] as ImageView).setImageResource(R.drawable.path_color_selected)
            drawingView.setPathColor(R.color.white)
        }

    }
}



/*
Paint: store style (including width) and color
Path: store footprint

CustomPath(color, width): Path
 */