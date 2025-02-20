# Witch 백엔드 포팅 매뉴얼

## 포팅 커맨드

```sh
cd ~
git clone https://lab.ssafy.com/s12-webmobile4-sub1/S12P11D211.git
cd S12P11D211
git fetch origin backend-develop
git switch origin/backend-develop
docker-compose -f ./infrastructure/docker-compose.yml up -d
docker-compose -f ./infrastructure/docker-compose-kafka.yml up -d
./gradlew clean build test asciidoctor
cd ..
nohup env AWS_ACCESS_KEY=test \
          AWS_SECRET_KEY=test \
          GMAIL_PASSWORD=${GMAIL_PASSWORD} \
          GMAIL_USERNAME=${GMAIL_USERNAME} \
          JWT_SECRET=0c5eab3e8f3c226f2446c598b79845879a6d368bffc66e1cb6ad8d70829448ee \
     java -Daws.s3.endpoint="${SERVER_URL}$:4566" \
          -jar ./S12P11D211/witch-apps/app-api/build/libs/app-api-1.0-SNAPSHOT-boot.jar \
     > nohup.out 2>&1 &
```

`GMAIL_USERNAME` 및 `GMAIL_PASSWORD`, `SERVER_URL` 환경변수 설정 필수입니다. GMAIL 환경 변수 설정은 [링크](https://adjh54.tistory.com/597) 참고하여 설정합니다.

## schema

초기 데이터 불필요

```sql
CREATE TABLE `user`
(
    `user_id`           VARCHAR(36)         NOT NULL PRIMARY KEY,
    `email`             VARCHAR(320) UNIQUE NOT NULL,
    `nickname`          VARCHAR(12) UNIQUE  NOT NULL,
    `password`          VARCHAR(60)         NOT NULL,
    `profile_image_url` VARCHAR(2000)       NULL,
    `created_at`        TIMESTAMP           NOT NULL,
    `modified_at`       TIMESTAMP           NOT NULL
);

CREATE TABLE `group`
(
    `group_id`        VARCHAR(36)        NOT NULL PRIMARY KEY,
    `name`            VARCHAR(20) UNIQUE NOT NULL,
    `group_image_url` VARCHAR(2000)      NULL,
    `created_at`      TIMESTAMP          NOT NULL,
    `modified_at`     TIMESTAMP          NOT NULL
);

CREATE TABLE `appointment`
(
    `appointment_id`   VARCHAR(36)  NOT NULL PRIMARY KEY,
    `group_id`         VARCHAR(36)  NOT NULL,
    `name`             VARCHAR(20)  NOT NULL,
    `summary`          TEXT         NULL,
    `appointment_time` TIMESTAMP    NOT NULL,
    `status`           VARCHAR(10)  NOT NULL comment 'PENDING/ACTIVE/INACTIVE',
    `latitude`         DOUBLE       NOT NULL,
    `longitude`        DOUBLE       NOT NULL,
    `created_at`       TIMESTAMP    NOT NULL,
    `modified_at`      TIMESTAMP    NOT NULL,
    `address`          VARCHAR(255) NOT NULL
);

CREATE TABLE `group_member`
(
    `group_member_id`  VARCHAR(36) NOT NULL PRIMARY KEY,
    `user_id`          VARCHAR(36) NOT NULL,
    `group_id`         VARCHAR(36) NOT NULL,
    `is_leader`        TINYINT(1)  NOT NULL,
    `created_at`       TIMESTAMP   NOT NULL,
    `modified_at`      TIMESTAMP   NOT NULL,
    `cnt_late_arrival` INT         NOT NULL
);

CREATE TABLE `appointment_member`
(
    `appointment_member_id` VARCHAR(36) NOT NULL PRIMARY KEY,
    `user_id`               VARCHAR(36) NOT NULL,
    `appointment_id`        VARCHAR(36) NOT NULL,
    `is_leader`             TINYINT(1)  NOT NULL,
    `final_latitude`        DOUBLE      NULL comment '약속 종료 후 최종 위도',
    `final_longitude`       DOUBLE      NULL comment '약속 종료 후 최종 경도',
    `created_at`            TIMESTAMP   NOT NULL,
    `modified_at`           TIMESTAMP   NOT NULL
);

CREATE TABLE `group_join_request`
(
    `group_join_request_id` VARCHAR(36) NOT NULL PRIMARY KEY,
    `user_id`               VARCHAR(36) NOT NULL,
    `group_id`              VARCHAR(36) NOT NULL,
    `created_at`            TIMESTAMP   NOT NULL,
    `modified_at`           TIMESTAMP   NOT NULL
);

CREATE TABLE `snack`
(
    `snack_id`        VARCHAR(36)   NOT NULL PRIMARY KEY,
    `appointment_id`  VARCHAR(36)   NOT NULL,
    `user_id`         VARCHAR(36)   NOT NULL,
    `latitude`        DOUBLE        NOT NULL,
    `longitude`       DOUBLE        NOT NULL,
    `snack_image_url` VARCHAR(2000) NULL,
    `snack_sound_url` VARCHAR(2000) NULL,
    `created_at`      TIMESTAMP     NOT NULL,
    `modified_at`     TIMESTAMP     NOT NULL
);

CREATE TABLE `fcm_token`
(
    `fcm_token_id` BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`      VARCHAR(36)  NOT NULL,
    `fcm_token`    VARCHAR(300) NOT NULL
);

```

## 실행환경

- JVM : Zulu 17.0.13
- IDE : Intellij Ultimate 2024.2.4

## 앱 사용 시나리오

- 회원 가입/로그인
    - 회원 가입 및 로그인을 통해 사용자는 서비스에 접근할 수 있습니다.
    - 회원 가입 시 이름, 이메일, 비밀번호 등 필수 정보를 입력하며, 이메일을 입력한 후 해당 이메일로 인증코드를 발송, 인증 성공 시 회원가입을 완료합니다
    - 로그인 시 이메일과 비밀번호로 본인 인증을 진행합니다.
- 월별 약속 일정 확인
    - 사용자는 월별 약속 일정 확인 기능을 통해 달력에서 해당 월의 약속들을 한눈에 볼 수 있습니다.
    - 달력에서 날짜 클릭 시 약속 목록을 보여주며, 현재 진행 중인 약속은 진행중 태그로 구분합니다.
    - 약속을 클릭하면 지도와 시간, 장소, 참석자 등의 세부 정보를 확인할 수 있습니다.
- 모임 생성 및 조회
    - 또한 사용자는 모임 생성 기능을 이용해 새로운 모임을 만들 수 있습니다. 모임 생성 시 모임 이름과 사진을 설정하며, 생성된 모임은 초대 링크를 통해 구성원을 추가할 수 있습니다.
        - 모임 이름 옆 모임 초대 링크 복사를 통해 초대 링크를 복사하여 이를 친구에게 공유 - 가입 신청을 보낼 수 있습니다.
    - 모임장은 모임 내 가입 신청자 목록을 확인하여 수락 또는 거절할 수 있습니다.
    - 모임 조회 기능을 통해 모임의 구성원을 확인할 수 있으며, 해당 모임에서의 지각 횟수를 확인할 수 있습니다.
    - 모임의 진행 중, 예정, 종료 약속 목록을 확인할 수 있습니다.
    - 모임 목록과 모임 내 약속 목록은 스와이프 시 새로고침 기능을 제공합니다.
- 약속 생성
    - 약속 생성 기능을 통해 사용자는 모임 내에서 새로운 약속을 추가할 수 있습니다. 약속 생성 시 제목, 날짜, 시간, 장소 등을 입력하면 관련 모임과 연결되며, 참여자에게 알림이 전송됩니다.
- 실시간 약속 위치 확인
    - 실시간 약속 위치 확인 기능을 통해 약속 당일 참석자들의 위치를 지도에서 확인할 수 있습니다. 위치 공유 동의를 기반으로 약속 시간 전후 일정 시간 동안 활성화됩니다.
- 스낵 생성 및 조회
    - 사용자는 약속 1시전 전부터 약속 시간까지 스낵 생성 및 조회 기능을 통해 약속 구성원들과 업로드한 스토리 형태의 콘텐츠를 공유할 수 있습니다.
    - 약속 장소와 스낵을 올린 거리보다 약속 장소와 현재 사용자의 위치가 더 짧을 경우에만 스낵을 확인할 수 있습니다.
    - 스낵에는 사진, 음성 녹음, 텍스트가 포함될 수 있으며, 약속 시간이 지나면 사라집니다.
    - 스낵 생성 기능을 통해 사용자는 자신의 일상이나 모임 관련 내용을 스토리 형태로 공유할 수 있습니다. 사진과 텍스트, 음성 녹음 등을 업로드하면 스낵 목록에 표시됩니다.
- 로그아웃/회원 탈퇴
    - 마지막으로 로그아웃/회원 탈퇴 기능을 통해 사용자는 계정에서 안전하게 로그아웃하거나, 원할 경우 계정을 삭제하여 모든 개인 데이터를 제거할 수 있습니다.