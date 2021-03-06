# TodayCard
"오늘의 카드" Android Application 소개 및 개발 과정 기록입니다.
### 1. 전체 요약
### **[오늘의 카드]**
##### 국민카드의 유료 SMS 서비스를 분석해 오늘 얼마를 사용했는지 한 눈에 볼 수 있습니다. 또한 당일 사용 한도를 넘기면 경고 알림을 띄웁니다.  
### **[Application 화면 설계]**
![전체화면](https://user-images.githubusercontent.com/87768226/131626265-6d06f29e-da8e-4ee8-bbf2-08079146b690.png)
### **[사용 핵심 skill 및 지식]** 
![캡처](https://user-images.githubusercontent.com/87768226/133249763-79aa7c33-289a-49c1-88f8-50e0abd461be.JPG) 
#### (사용 개념 정리 블로그: https://junnn183.tistory.com/27)
### 2. 프로젝트 설명
### **[프로젝트명]**
##### '오늘의 카드' 안드로이드 앱 개발 프로젝트
### **[프로젝트 소개]**
##### 국민카드의 유료 문자 서비스를 사용하면, 해당 카드로 결제 시 결제 내역을 SMS로 받을 수 있습니다. 이 SMS을 분석해 오늘 사용자가 얼마를 사용했는지 한 눈에 볼 수 있는 기능을 제공합니다. 또한 하루 사용 한도를 입력 받고 사용 한도를 넘기면 사용자에게 경고 알림을 띄우는 기능을 제공합니다. 앱의 알림은 종 모양 아이콘 클릭을 통해 on/off 가능합니다. 부가 기능으로 일별 사용 금액 조회, 사용 한도 조절, 결제 정보 직접 입력, 당일 결제 정보 삭제를 제공합니다.
### **[프로젝트 기간]**  
##### 21.07.28 ~ 21.08.04  
### **[개발 환경 및 언어]**
##### AndroidStudio/Java
### **[상세 업무]**
##### Activity/Fragment/BroadcastReciever/Service/Notification
### 1-1)Activity
![캡처051](https://user-images.githubusercontent.com/87768226/131626474-404ec206-1d8a-4208-82b1-0fc8b67f2c31.PNG)
##### -SplashActivity에서 finsh()를 하고 MainActivity로 startActivity() 호출(Activity Task 관리)
###### (부연설명: Activity는 Task에 쌓이기 때문에 종료하지 않으면 back버튼으로 다시 나오기 때문)
##### -AppBarLayout에 Toolbar와 TabLayout을 배치
##### -사용자 터치에 의한 알림 on/off 상태 SharedPreferences에 저장
##### -Menu의 검색/한도변경/직접입력은 새 Activity로 작업(Lifecycle을 활용한 화면 갱신을 위해)
###### (부연 설명: 다른 activity에서 현재 activity로 돌아오면 onRestart()->onStart()->onResume() 호출됩니다. 다른 activity에서는 한도 변경, 직접입력 즉, DB의 데이터가 변경되는 작업을 합니다. 이를 반영하기 위해 activity 생명주기를 활용해 view를 다시 그립니다.)
##### -‘오늘’과 ‘상세내역’은 Fragment로 구현(ViewPager와 TabLayout 함께 사용)
##### -MainActivity에서 각 Fragment에 Bundle을 사용해 날짜 데이터 전달
##### -MainActivity의 Activity Task 관리 위해 singleTop 설정/onNewIntent() 구현
###### (부연 설명: 앱의 MainActivity 사용 중에 알림이 왔고 이때 사용자가 알림을 터치하는 경우 MainActivity 2개가 task에 존재하게 됩니다(activity는 singleton이 아니기 때문). 따라서 알림 터치 후 새 activity가 보이고 이후 사용자가 back 버튼 누르면 이전 activity 다시 보이기 때문에 이를 방지하고자 위처럼 설정합니다.)
### 1-2)
![image5](https://user-images.githubusercontent.com/87768226/128278831-2e710d17-53c1-4855-b1f8-500d3be932cf.PNG)
##### -SearchActivity: DatePickerDialog로 날짜 입력, DB 조회 후 View에 출력
##### -ChangeLimitActivity: SharedPreferences로 한도 조회 및 변경
##### -PutSpend: 결제 정보 입력 후 DB 저장
### 2) Fragment
![캡처053](https://user-images.githubusercontent.com/87768226/131626523-b6cc8afa-df8d-4d27-a72a-8ebf166ebe31.PNG)
##### -Context 사용 위해 onAttach()에서 획득
##### -DB에서 데이터를 찾아 View에 그리는 작업은 onStart()에서 작업(Lifecycle을 활용한 화면 갱신을 위해)
###### (부연 설명: 한도 변경, 직접 입력을 위해 다른 Activity로 갔다가 다시 돌아오는 경우 수정된 DB를 View에 반영해야합니다. 이때 fragment 생명주기가 onStart()부터 다시 시작됨을 이용합니다.)
##### -Fragement(1): CustomView로 사용자에 도넛 모양의 View를 제공
##### -Fragment(2): RecyclerView 사용 및 onClick()을 달아서 삭제 여부 묻는 AlterDialog 띄움
##### -AlterDialog의 ‘네’ 버튼 클릭 시 MainActivity 갱신
### 3) BroadcastReciever
<img src = "https://user-images.githubusercontent.com/87768226/131626572-634985ea-7f8c-4813-ae64-4d18e97b0330.PNG" width="35%" height="35%">

##### -암시적 호출 (System에서 발생시키는 Intent이기 때문에 BroadcastReciever의 백그라운드 실행 제한에 해당하지 않습니다.)
![image7](https://user-images.githubusercontent.com/87768226/128279215-6247a0bb-782c-4d3f-8058-6fb01010ffc2.PNG)
##### -SMS parsing 후 국민카드 결제 문자라면, 문자 내용을 담은 startService() 호출
### 4)Service 및 Notification
![image8](https://user-images.githubusercontent.com/87768226/128279328-cc1ca79f-5266-4792-8ec9-02c366741c6a.PNG)
##### -IntentService 사용(SMS가 동시에 올 경우 대비)
##### -SMS 내용을 승인/사용취소로 구분해 DB 데이터 수정
##### -SMS 내용이 승인이고 당일 사용한도 초과라면 Notification 띄움
##### -targetSDKVersion은 30, minSDKVersion은 29이기 때문에 NotificationChannel 설정
##### -Notification에 PendingIntent를 설정해 클릭 시 MainActivity 실행
##### (SMS Broadcast 수신이기에 앱이 임시 허용 목록이 있습니다. 따라서 백그라운드 서비스 제한 없이 가능합니다.)
### 5) Others  
##### Permission  
##### uses-permission android:name="android.permission.RECEIVE_SMS"   
| |ProtectionLevel|사용자에게 권한 부여 요청|
|------|---|---|
|RECEIVE_SMS|Dangerous|O|
