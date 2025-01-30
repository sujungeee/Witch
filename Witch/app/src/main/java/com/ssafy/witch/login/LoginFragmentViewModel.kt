package com.ssafy.witch.login

import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.data.remote.RetrofitUtil
import kotlinx.coroutines.launch

class LoginFragmentViewModel: ViewModel() {

    //뷰모델에서만 쓰는 것
    private val _user = MutableLiveData<User>()

    //뷰모델 외 사용 가능한 public 선언
    val user:LiveData<User>
        get() = _user

    // 이메일, 닉네임, JWT 토큰
    fun login(email:String, nickname: String) {
        viewModelScope.launch {
            // login 위해 retrofit 으로 가야 함
            // userService에 있다
            runCatching {
//                RetrofitUtil.userService.login(User(email, nickname))

            }.onSuccess {
                //it 을 받아서 _User 에 담아주면 됨
//                _user.value = it
            }.onFailure {
                // 로그인 실패면 빈 User 보내주면 없는 정보 담을 수 있음
                _user.value = User()
            }
        }
    }

}