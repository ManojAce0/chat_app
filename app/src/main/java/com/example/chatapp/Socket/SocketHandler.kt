package com.example.chatapp.Socket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.Model.Chat
import io.socket.client.IO
import com.google.gson.Gson
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SocketHandler:ViewModel() {
    private val _onNewChat = MutableLiveData<Chat>()
    val onNewChat: LiveData<Chat> get() = _onNewChat
    private  var socket:Socket?=null

    init{
        CoroutineScope(Dispatchers.IO).launch {
            connectSocket()
        }
    }
    private suspend fun connectSocket(){
        try{
            withContext(Dispatchers.IO){
                socket= IO.socket(SOCKET_URL)
                socket?.connect()
            }
            registerOnNewChat()
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }
    private fun registerOnNewChat() {
        socket?.on(CHAT_KEYS.BROADCAST) { args ->
            args?.let { d ->
                if (d.isNotEmpty()) {
                    val data = d[0]
                    if (data.toString().isNotEmpty()) {
                        val chat = Gson().fromJson(data.toString(), Chat::class.java)
                        _onNewChat.postValue(chat)
                    }
                }
            }
        }
    }

    fun disconnectSocket() {
        CoroutineScope(Dispatchers.IO).launch {
            socket?.disconnect()
            socket?.off()
        }
    }

    fun emitChat(chat: Chat) {
        val jsonStr = Gson().toJson(chat, Chat::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            socket?.emit(CHAT_KEYS.NEW_MESSAGE, jsonStr)
        }
    }



    private object CHAT_KEYS {
        const val NEW_MESSAGE = "new_message"
        const val BROADCAST = "broadcast"
    }

    companion object{
        private const val SOCKET_URL = "https://backend-tnn2.onrender.com/"
    }

}