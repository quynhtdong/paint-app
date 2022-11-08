package edu.tcu.quynhtdong.paint

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.core.view.get
import androidx.core.view.iterator
import com.bumptech.glide.Glide


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
            findViewById<ImageView>(R.id.background_iv).setImageURI(uri)
            Glide.with(this).load(uri).into(findViewById<ImageView>(R.id.background_iv));
        }
        findViewById<ImageView>(R.id.gallery_iv).setOnClickListener {

            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        val backgroundIv = findViewById<ImageView>(R.id.background_iv)
        if(backgroundIv.drawable == null){
            backgroundIv.setBackgroundColor(Color.WHITE)
        }

        val bitmap = findViewById<FrameLayout>(R.id.drawing_fl).drawToBitmap()
//        val values = ContentValues().apply {
//            put(
//                MediaStore.MediaColumns.DISPLAY_NAME,
//                System.currentTimeMillis().toString().substring(2, 11) + ".jpeg"
//            )
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
//        }
//        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//        uri?.let{
//            contentResolver.openOutputStream(it).use { it_->
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it_)
//            }
//        }



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