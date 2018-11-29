package li.sau.projectwork.utils.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import li.sau.projectwork.utils.DEFAULT_DISK_CACHE_SIZE
import li.sau.projectwork.utils.MEGA
import java.io.InputStream


@GlideModule
class GlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Set cache size
        builder.setMemoryCache(LruResourceCache(10L * MEGA))
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(
                context,
                "glide-cache",
                DEFAULT_DISK_CACHE_SIZE))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Use OkHttp client
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
    }

}