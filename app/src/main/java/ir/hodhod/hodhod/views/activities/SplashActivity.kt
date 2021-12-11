package ir.hodhod.hodhod.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import ir.hodhod.hodhod.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    // region of params
    private lateinit var handler: Handler

    // END of region of params

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))

            // in order to prevent remaining of unrelated activity stack
            finishAffinity()
        }, 3000)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)

        super.onDestroy()
    }
}