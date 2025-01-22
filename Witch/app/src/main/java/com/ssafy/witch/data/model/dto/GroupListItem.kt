package com.ssafy.witch.data.model.dto

data class GroupListItem(
    var group_id: Int,
    var name: String,
    var group_image_url: String,
    var group_leader: GroupLeader,
    var num_group_members: Int,
    var createdAt: String
) {
    data class GroupLeader(
        var user_id:Int,
        var nickname:String,
        var profile_image_url:String
    )
}