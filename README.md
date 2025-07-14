
# 🏃‍♀️ Fitnesstracker – Smart Fitness & Health Monitoring App

An advanced Android app built with **Kotlin** and **Jetpack Compose** that helps users track their fitness activities, view progress, manage personal data, and interact with trainers via video calls.

## 🚀 Features

- 📈 **Step Tracking**  
  - Real-time tracking with SensorManager  
  - Historical sync via Health Connect API  
  - Calorie estimation & step goal progress  

- 🧠 **BMI Calculator**  
  - Instantly calculate BMI with Compose UI  
  - Supports metric & imperial units  

- 🏋️ **Workout Log**  
  - Add, edit, and delete workout sessions  
  - Stores data using Room DB with Live UI  

- 📊 **Weekly Trends**  
  - Visual step history via MPAndroidChart  
  - Tracks goal completion over time  

- 👤 **User Profile**  
  - Stores dynamic height, weight, gender using Jetpack DataStore  

- 📹 **Video Calling with Trainers**  
  - Built using Agora WebRTC 
  - Peer-to-peer camera & mic streaming  

- 🌙 **Modern UI**  
  - Jetpack Compose & Material 3  
  - Bottom navigation bar with animated transitions  
  - Dark mode support  

## 📦 Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Data:** Room DB, Jetpack DataStore, Firebase  
- **APIs:** Google Fit API, WebRTC  
- **Charts:** MPAndroidChart  
- **Architecture:** MVVM + Clean Architecture  

## 🔐 Permissions Required

- `ACTIVITY_RECOGNITION`  
- `CAMERA`  
- `INTERNET`  
- `RECORD_AUDIO`

## 🔧 Setup Instructions

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/Fitnesstracker.git
   ```

2. Open in **Android Studio Arctic Fox or later**

3. Connect your Firebase project and sync Gradle

4. Set your desired step goal and profile in-app

5. Build & run the app on a device with Google Fit installed

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Made By- Bhumika 
