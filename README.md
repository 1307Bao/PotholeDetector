# 📱 Pothole Detection App
![Logo](./demo/logo.png)

## Project Introduction
We have built a mobile application that detects potholes using sensor data from smartphones, including the accelerometer, gyroscope, and GPS. The app automatically collects motion and location data while users are driving and analyzes it in real-time to identify potholes. When a pothole is detected, the app records its location and sends instant alerts to the user.
In addition to automatic detection, the app allows users to manually report potholes and view all pothole locations on an interactive Mapbox-based map. Users can also review historical reports, customize detection sensitivity, and switch between languages. The application supports offline data storage and can optionally sync with a backend server for data sharing and analysis.

[Watch the Intro](https://github.com/1307Bao/PotholeDetector/blob/master/demo/video/Demo.mp4)


<video controls>
  <source src="Intro.mp4" type="video/mp4">
  <source src="Detail.webm" type="video/webm">
  <source src="Demo.ogv" type="video/ogg">
  Your browser does not support the video tag.
</video>


## Team Members
| ID        | Name         |
| :-------- | :----------- |
| 22520112  | Nguyễn Thái Bảo |
| 22520031  | Trần Thúy An |

## Database Schema 
![Schema](./demo/schema.png)

## Main Features for Users
> * 📍 Pothole Detection using real-time accelerometer and GPS data
> * 🗺️ Map Integration to visualize pothole locations
> * 🔔 Real-time Notifications to warn users of detected potholes
> * 🧠 Data Analysis to identify unusual vibrations
> * 📦 Local Data Storage for offline access
> * 📊 Statistics & Reports for road quality analysis
> * 🌐 Multilingual Support (English & Vietnamese)
> * ⚙️ Custom Settings for sensitivity, theme, and language

## Data Flow & Architecture
- Real-time sensor data → local service → anomaly detection algorithm → map update + notification
- Optionally syncs to backend for long-term storage and analytics

## 📷 Demo of Some Interfaces
- Splash & Select Language
<div float="left" align="center" >
  <img src="./demo/image/splash-light.jpg" width="23%" />
  <img src="./demo/image/splash-dark.jpg" width="23%" />
  <img src="./demo/image/select-language-light.jpg" width="23%" /> 
  <img src="./demo/image/select-language-dark.jpg" width="23%" /> 
</div>

 - 🔐 Authentication 
<div float="left" align="center" >
  <img src="./demo/image/sign-in-light.jpg" width="23%" />
  <img src="./demo/image/create-account-light.jpg" width="23%" /> 
  <img src="./demo/image/forget-password-light.jpg"width="23%" />
  <img src="./demo/image/enter-otp-light.jpg"width="23%" />
</div>
<div float="left" align="center" >
  <img src="./demo/image/sign-in-dark.jpg" width="23%" />
  <img src="./demo/image/create-account-dark.jpg" width="23%" /> 
  <img src="./demo/image/forget-password-dark.jpg"width="23%" />
  <img src="./demo/image/enter-otp-dark.jpg"width="23%" />
</div>

- 🏠 Home Screen
<div float="left" align="center" >
  <img src="./demo/image/home-light.jpg" width="23%" />
  <img src="./demo/image/home-dark.jpg" width="23%" /> 
  <img src="./demo/image/new-pothole-light.jpg"width="23%" />
  <img src="./demo/image/new-pothole-dark.jpg"width="23%" />
</div>

- 🗺️ Map View & 📝 Report
<div float="left" align="center" >
  <img src="./demo/image/map-view-light.jpg" width="23%" />
  <img src="./demo/image/map-view-dark.jpg" width="23%" /> 
  <img src="./demo/image/report-light.jpg"width="23%" />
  <img src="./demo/image/report-dark.jpg"width="23%" />
</div>

- ⚙️ Settings
<div float="left" align="center" >
  <img src="./demo/image/setting-light.jpg" width="23%" />
  <img src="./demo/image/setting-dark.jpg" width="23%" /> 
  <img src="./demo/image/appearance-light.jpg"width="23%" />
  <img src="./demo/image/change-name-light.jpg"width="23%" />
</div>


[![Watch the Intro](https://github.com/1307Bao/PotholeDetector/tree/master/demo/thumbail/Intro.png)](https://github.com/1307Bao/PotholeDetector/tree/master/demo/Intro.mp4)
[![Watch the Detail](https://github.com/1307Bao/PotholeDetector/tree/master/demo/thumbail/Detail.png)](https://github.com/1307Bao/PotholeDetector/tree/master/demo/Detail.mp4)
[![Watch the Demo](https://github.com/1307Bao/PotholeDetector/tree/master/demo/thumbail/Demo.png)](https://github.com/1307Bao/PotholeDetector/tree/master/demo/Demo.mp4)


## 🧪 Technologies Used
- **Languages:** Java (Android & Backend)
- **Backend:** Spring Boot, MySQL
- **Frontend:** Android (Java), Mapbox SDK
- **IDE:** Android Studio, IntelliJ IDEA
- **Networking:** Retrofit
- **Security:** JWT, Spring Security
- **Version Control:** Git, GitHub



## 🛠 Installation Guide

### Prerequisites
- Android Studio (latest version)
- JDK 11 or above
- MySQL 8.0+
- Android device (Android 7.0+)

### 📥 Clone the Project
```git clone https://github.com/1307Bao/PotholeDetector.git```

### 📱 Setup Frontend
1. **Clone the repository and navigate to the frontend directory:**
- Open Android Studio → File > Open → select the frontend folder.
- Android Studio will sync dependencies automatically.
- Update ApiClient.java with your backend IP: ```String BASE_URL = "http://<your_local_ip>:8080";```

### 🖥️ Setup Backend
1. Open the backend folder in IntelliJ.

2. In application.yaml, update your MySQL config:
   ```
    spring:
        datasource:
            url: jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME
            username: YOUR_USERNAME
            password: YOUR_PASSWORD
    ```

### 📲 Run the App
- Connect your Android device or launch an emulator.
- In Android Studio, press Shift + F10 to build and run.

>* ***📝Note:** Refer to the installation guide file below for a clearer understanding: [Detailed Guide File](./demo/Huong-dan-cai-dat-Pothole-Detector.docx)*
