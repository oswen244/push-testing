package com.example.pushtesting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        showConfirmation()
    }

    private fun showConfirmation(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Deseas ver tu clave digital?")
            .setPositiveButton("Ver clave"
            ) { dialog, id ->
                goToToken()
                dialog.dismiss()
                finish()
            }
            .setNegativeButton("Cancel"
            ) { dialog, id ->
                dialog.dismiss()
                finish()
            }
        builder.show()
    }

    private fun goToToken(){
        val intent = Intent(this, SoftTokenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}