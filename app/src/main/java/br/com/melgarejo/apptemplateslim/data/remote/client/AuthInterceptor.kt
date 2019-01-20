package br.com.melgarejo.apptemplateslim.data.remote.client

import br.com.melgarejo.apptemplateslim.data.storage.PreferencesCache
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Cache
import br.com.melgarejo.apptemplateslim.domain.entity.User
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    companion object {
        const val HEADER_TOKEN = "USER-TOKEN"
        const val HEADER_EMAIL = "USER-EMAIL"
    }

    private var user: User? = null

    override fun intercept(chain: Interceptor.Chain): Response {
//        user = GetCurrentUser().execute()
        val requestBuilder = chain.request().newBuilder()
        if (user != null) {
            try {
                requestBuilder.addHeader(HEADER_TOKEN, getTokenFromCache())
                requestBuilder.addHeader(HEADER_EMAIL, getClientFromCache())
            } catch (e: Cache.NotFoundException) {
                // user is not logged in
            }
        }
        val response = chain.proceed(requestBuilder.build())
        response.headers()?.get(HEADER_TOKEN)?.let { PreferencesCache.set(HEADER_TOKEN, it) }
        response.headers()?.get(HEADER_EMAIL)?.let { PreferencesCache.set(HEADER_EMAIL, it) }

        return response
    }

    fun hasHeaders() = try {
        getTokenFromCache()
        getClientFromCache()
        true
    } catch (e: Cache.NotFoundException) {
        false
    }

    private fun getTokenFromCache() =
        PreferencesCache.get<String>(HEADER_TOKEN, String::class.java)

    private fun getClientFromCache() =
        PreferencesCache.get<String>(HEADER_EMAIL, String::class.java)
}
