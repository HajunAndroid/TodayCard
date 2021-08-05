# TodayCardProject
"오늘의 카드" Android Application
### 1. 전체 요약
### **[오늘의 카드]**
##### 국민카드의 유료 SMS 서비스를 분석해 오늘 얼마를 사용했는지 한 눈에 볼 수 있습니다. 또한 당일 사용 한도를 넘기면 경고 알림을 띄웁니다.  
### **[Application 화면 설계]**
![전체화면](https://user-images.githubusercontent.com/87768226/128277348-0ede9d4e-de88-42cc-b431-7ae63832d431.PNG)
### **[사용 핵심 skill 및 지식]** 
![이미지2](https://user-images.githubusercontent.com/87768226/128277622-1abe4aa4-7592-41f9-adae-434994b3c0bc.PNG)  
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
![image3](https://user-images.githubusercontent.com/87768226/128278498-9a6d9e47-2c53-4d80-9a55-df28f923ff97.PNG)
##### -SplashActivity에서 finsh()를 하고 MainActivity로 startActivity() 호출(Activity Task 관리)
##### -AppBarLayout에 Toolbar와 TabLayout을 배치
##### -사용자 터치에 의한 알림 on/off 상태 SharedPreferences에 저장
##### -Menu의 검색/한도변경/직접입력은 새 Activity로 작업(Lifecycle을 활용한 화면 갱신을 위해)
##### -‘오늘’과 ‘상세내역’은 Fragment로 구현(ViewPager와 TabLayout 함께 사용)
##### -MainActivity에서 각 Fragment에 Bundle을 사용해 날짜 데이터 전달
##### -MainActivity의 Activity Task 관리 위해 singleTop 설정/onNewIntent() 구현
### 1-2)
![image5](https://user-images.githubusercontent.com/87768226/128278831-2e710d17-53c1-4855-b1f8-500d3be932cf.PNG)
##### -SearchActivity: DatePickerDialog로 날짜 입력, DB 조회 후 View에 출력
##### -ChangeLimitActivity: SharedPreferences로 한도 조회 및 변경
##### -PutSpend: 결제 정보 입력 후 DB 저장
### 2) Fragment
![image4](https://user-images.githubusercontent.com/87768226/128278681-3fb80d47-4934-4ecb-a2e9-ff4c53530ad8.PNG)
##### -Context 사용 위해 onAttach()에서 획득
##### -DB에서 데이터를 찾아 View에 그리는 작업은 onStart()에서 작업(Lifecycle을 활용한 화면 갱신을 위해)
##### -Fragement(1): CustomView로 사용자에 도넛 모양의 View를 제공
##### -Fragment(2): RecyclerView 사용 및 onClick()을 달아서 삭제 여부 묻는 AlterDialog 띄움
##### -AlterDialog의 ‘네’ 버튼 클릭 시 MainActivity 갱신
### 3) BroadcastReciever
![image6](https://user-images.githubusercontent.com/87768226/128278980-a078d523-2c7a-4597-9a54-cc180f159022.PNG)
##### -암시적 호출
![image7](https://user-images.githubusercontent.com/87768226/128279215-6247a0bb-782c-4d3f-8058-6fb01010ffc2.PNG)
##### -SMS parsing 후 국민카드 결제 문자라면, 문자 내용을 담은 startService() 호출
### 4)Service 및 Notification
![image8](https://user-images.githubusercontent.com/87768226/128279328-cc1ca79f-5266-4792-8ec9-02c366741c6a.PNG)
##### -IntentService 사용(SMS가 동시에 올 경우 대비)
##### -SMS 내용을 승인/사용취소로 구분해 DB 데이터 수정
##### -SMS 내용이 승인이고 당일 사용한도 초과라면 Notification 띄움
##### -targetSDKVersion은 30, minSDKVersion은 29이기 때문에 NotificationChannel 설정
##### -Notification에 PendingIntent를 설정해 클릭 시 MainActivity 실행
