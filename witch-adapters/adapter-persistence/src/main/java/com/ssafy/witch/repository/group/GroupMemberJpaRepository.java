package com.ssafy.witch.repository.group;

import com.ssafy.witch.entity.group.GroupMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberJpaRepository extends JpaRepository<GroupMemberEntity, String> {

}
