package li.sau.projectwork

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class GoforeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)
    }
}