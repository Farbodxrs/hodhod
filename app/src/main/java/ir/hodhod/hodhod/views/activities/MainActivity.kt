package ir.hodhod.hodhod.views.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.viewmodels.SharedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // region of params
    private val sharedViewModel by viewModels<SharedViewModel>()

    // END of region of params

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidThreeTen.init(this)

        subscribeViews()

        sharedViewModel.connectToServer()
    }

    private fun subscribeViews() {
        sharedViewModel.connectRespond.observe(this) {
            Toast.makeText(this, "connected successfully", Toast.LENGTH_SHORT).show()
        }

        sharedViewModel.connectError.observe(this) {
            Toast.makeText(this, "connect failed", Toast.LENGTH_SHORT).show()
        }

        sharedViewModel.disconnectRespond.observe(this) {
            Toast.makeText(this, "disconnected successfully", Toast.LENGTH_SHORT).show()
        }

        sharedViewModel.disconnectError.observe(this) {
            Toast.makeText(this, "disconnect failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        sharedViewModel.disconnectFromServer()
        super.onDestroy()
    }
}