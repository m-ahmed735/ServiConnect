# ServiConnect - On-Demand Service Marketplace

ServiConnect is a professional Android application built to bridge the gap between local service providers (Plumbers, Electricians, etc.) and customers. 

## 🚀 Key Features
- **Dual User Roles:** Specialized interfaces for 'Seekers' (Customers) and 'Service Providers'.
- **Dynamic Onboarding:** Role-based registration and real-time navigation routing.
- **Service Discovery:** Category-based browsing with filtered Firestore queries.
- **Job Management:** Real-time service request system with Accept/Reject functionality.
- **Secure Auth:** Firebase Authentication with persistent login sessions.

## 🛠 Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **UI:** Material Design 3 (Components, Google Fonts)
- **Database/Auth:** Firebase (Firestore & Auth)
- **Navigation:** Jetpack Navigation Component (Single Activity Architecture)
- **Asynchronous Logic:** Kotlin Coroutines & Flow

## 📂 Project Structure & Architecture
The project follows clean architecture principles to ensure scalability:
- **Data:** Contains Models, Repositories, and Firebase Data Sources.
- **UI:** Divided by feature (Auth, Seeker, Provider) using ViewBinding.
- **ViewModel:** Handles business logic and state management via Sealed Classes (`Resource`).

## ⚙️ Setup Instructions
1. Clone the repository.
2. Add your `google-services.json` to the `app/` directory.
3. Build and Run on an Android Device (API 24+).

---
Developed by Muhammad Ahmed - Software Engineering Graduate (2025).
