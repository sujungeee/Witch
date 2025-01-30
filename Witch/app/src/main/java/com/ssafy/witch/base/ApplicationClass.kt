package com.ssafy.witch.base

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.witch.data.local.SharedPreferencesUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

// 앱이 실행될때 1번만 실행이 됩니다.
class ApplicationClass : Application() {
    //ends with '/'
    val API_URL = ""

    // 코틀린의 전역변수 문법
    companion object {
        lateinit var instance: ApplicationClass
            private set

        // 만들어져있는 SharedPreferences 를 사용해야합니다. 재생성하지 않도록 유념해주세요
        lateinit var sharedPreferences: SharedPreferencesUtil


        // JWT Token Header 키 값
        const val ACCESS_TOKEN = "ACCESS-TOKEN"

        // Retrofit 인스턴스, 앱 실행시 한번만 생성하여 사용합니다.
        lateinit var retrofit: Retrofit
    }

    // 앱이 처음 생성되는 순간, SP를 새로 만들어주고, 레트로핏 인스턴스를 생성합니다.
    override fun onCreate() {
        super.onCreate()
        instance = this

        //shared preference 초기화
//        GlobalScope.launch(Dispatchers.IO) {
//            sharedPreferences = SharedPreferencesUtil(applicationContext)
//        }
        sharedPreferences = SharedPreferencesUtil(applicationContext)

        // 레트로핏 인스턴스 생성
        initRetrofitInstance()
    }

    // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
    // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
    private fun initRetrofitInstance() {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(AccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        // retrofit 이라는 전역변수에 API url, 인터셉터, Gson을 넣어주고 빌드해주는 코드
        // 이 전역변수로 http 요청을 서버로 보내면 됩니다.
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    //GSon은 엄격한 json type을 요구하는데, 느슨하게 하기 위한 설정. success, fail이 json이 아니라 단순 문자열로 리턴될 경우 처리..
    val gson : Gson = GsonBuilder()
        .setLenient()
        .create()

    class AccessTokenInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder: Request.Builder = chain.request().newBuilder()
            //Todo 로그인 구현 시 추가하기
            val jwtToken = ApplicationClass.sharedPreferences.getAccessToken()
            if (!jwtToken.isNullOrEmpty()) {
                builder.addHeader("Authorization", "Bearer $jwtToken")
            }
            return chain.proceed(builder.build())
        }
    }

//    class AccessTokenInterceptor(private val context: Context) : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val accessToken = sharedPreferences(context).getAccessToken()
//            val request = if (accessToken != null) {
//                chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $accessToken")
//                    .build()
//            } else {
//                chain.request()
//            }
//            return chain.proceed(request)
//        }
//    }

//    class TokenAuthenticator(private val context: Context) : Authenticator {
//        override fun authenticate(route: Route?, response: Response): Request? {
//            val refreshToken = SecureSharedPreferences(context).getRefreshToken() ?: return null
//
//            // Refresh Token으로 Access Token 갱신 요청
//            val newAccessToken = refreshAccessToken(refreshToken) ?: return null
//
//            // 새로운 Access Token 저장
//            SecureSharedPreferences(context).saveTokens(newAccessToken, refreshToken)
//
//            // 기존 요청을 새로운 Access Token으로 재시도
//            return response.request.newBuilder()
//                .header("Authorization", "Bearer $newAccessToken")
//                .build()
//        }
//
//        private fun refreshAccessToken(refreshToken: String): String? {
//            val response = RetrofitInstance.apiService.refreshToken(refreshToken).execute()
//            return if (response.isSuccessful) response.body()?.accessToken else null
//        }
//    }

}
