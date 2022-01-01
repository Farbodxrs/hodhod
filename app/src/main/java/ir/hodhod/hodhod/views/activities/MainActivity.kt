package ir.hodhod.hodhod.views.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint
import ir.hodhod.hodhod.R
import ir.hodhod.hodhod.viewmodels.BrokerSharedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // region of params
    private val brokerSharedViewModel by viewModels<BrokerSharedViewModel>()

    // END of region of params

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidThreeTen.init(this)

        subscribeViews()

        brokerSharedViewModel.connectToServer()
    }

    private fun subscribeViews() {
        brokerSharedViewModel.connectRespond.observe(this) {
//            Toast.makeText(this, "connected successfully", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.connectError.observe(this) {
            Toast.makeText(this, R.string.problem_occurred, Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.disconnectRespond.observe(this) {
//            Toast.makeText(this, "disconnected successfully", Toast.LENGTH_SHORT).show()
        }

        brokerSharedViewModel.disconnectError.observe(this) {
//            Toast.makeText(this, "disconnect failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        brokerSharedViewModel.disconnectFromServer()
        super.onDestroy()
    }
}