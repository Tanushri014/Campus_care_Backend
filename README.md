# 🎓 Campus Care - Backend

A Spring Boot REST API powering **Campus Care**, a full-stack complaint and campus management system for educational institutions. The backend provides secure authentication, complaint management, announcement services, lost & found management, file uploads, email notifications, and role-based access control through a clean layered architecture.

---

## 📖 Project Overview

Campus Care is a centralized platform designed to improve communication between students and college administrators by digitizing common campus services.

This repository contains the backend application built with **Spring Boot**, exposing secure REST APIs that power the frontend. It handles authentication, complaint workflows, announcements, lost & found management, file storage, and email notifications while integrating with multiple third-party services.

The application follows a layered architecture using Controllers, Services, Repositories, DTOs, and Entities to ensure maintainability, scalability, and separation of concerns.

---

## ✨ Features

### 🔐 Authentication & Security

- Student Registration
- Email OTP Verification
- Secure Login
- Google OAuth2 Login
- JWT Authentication
- Role-Based Authorization
- BCrypt Password Encryption

### 👨‍🎓 Student Services

- Register & Authenticate
- Submit Complaints
- Track Complaint Status
- Submit Feedback
- Lost & Found Management
- View Announcements
- Feedback

### 👨‍💼 Admin Services

- Manage Complaints
- Update Complaint Status
- Publish Announcements


### 📧 Notification Services

- Email OTP Verification
- Announcement Email Notifications
- Asynchronous Email Sending

### ☁️ File Management

- Image Uploads
- PDF Uploads
- Cloudinary Integration

---

## 🛠️ Tech Stack

| Category           | Technology                  |
|--------------------|-----------------------------|
| Language           | Java 21                     |
| Framework          | Spring Boot                 |
| Security           | Spring Security             |
| Authentication     | JWT + Google OAuth2         |
| ORM                | Spring Data JPA (Hibernate) |
| Database           | MySQL                       |
| Build Tool         | Maven                       |
| Email Service      | Brevo SMTP                  |
| File Storage       | Cloudinary                  |
| Deployment         | Render                      |


## 🏗️ System Architecture

The backend follows a layered architecture to ensure clear separation of responsibilities and maintainable code.

```text
                        +----------------------+
                        |   React Frontend     |
                        +----------+-----------+
                                   |
                              REST API (HTTPS)
                                   |
                                   ▼
                    +------------------------------+
                    |     Spring Boot Backend      |
                    +------------------------------+
                    |  Controllers                 |
                    |  Services                    |
                    |  Repositories                |
                    |  DTOs                        |
                    |  Security (JWT/OAuth2)       |
                    +--------------+---------------+
                                   |
        +--------------------------+--------------------------+
        |                          |                          |
        ▼                          ▼                          ▼
+----------------+        +----------------+        +----------------+
|     MySQL      |        |   Cloudinary   |        |     Brevo      |
|  Database      |        | Image Storage  |        | Email Service  |
+----------------+        +----------------+        +----------------+
```

### Architecture Overview

- **Controller Layer** – Exposes REST APIs and handles incoming HTTP requests.
- **Service Layer** – Implements business logic and coordinates application workflows.
- **Repository Layer** – Performs database operations using Spring Data JPA.
- **Entity Layer** – Represents database tables as JPA entities.
- **DTO Layer** – Transfers data between the client and server while hiding internal models.
- **Security Layer** – Handles JWT authentication, Google OAuth2 login, authorization, and request filtering.


## 🗄️ Database Design

The application uses **MySQL** as its relational database and **Spring Data JPA (Hibernate)** for object-relational mapping.

### Core Entities

| Entity                   | Description |
|--------------------------|-------------|
| `Student`                | Stores registered student information and authentication details. |
| `Admin`                  | Stores administrator accounts with predefined roles and permissions. |
| `Complaint`              | Stores complaint details, category, priority, status, and related information. |
| `ComplaintStatusHistory` | Maintains the history of complaint status updates for tracking purposes. |
| `Feedback`               | Stores feedback submitted by students after a complaint is resolved. |
| `Announcement`           | Stores announcements published by administrators. |
| `LostFound`              | Stores lost and found item records submitted by students. |
| `AuthorizedStudent`      | Contains pre-approved student records eligible for registration. |
| `PendingStudent`         | Temporarily stores registration details until email verification is completed. |
| `EmailOtp`               | Stores OTPs used for email verification and password recovery. |

### Entity Relationships

- A **Student** can submit multiple **Complaints**.
- A **Student** can create multiple **Lost & Found** entries.
- A **Complaint** maintains a **status history** through `ComplaintStatusHistory`.
- A **Complaint** can receive **feedback** once it is completed.
- An **Admin** manages complaints and publishes announcements.
- A **PendingStudent** becomes a **Student** after successful OTP verification.
- **EmailOtp** records are temporary and automatically removed after expiration.
- Note:-only Mani admin can create Announcement 



## ⚙️ Environment Variables

Before running the application, create a `.env` file in the project root.

You can use the provided `.envTemplate` file as a reference:

**Linux / macOS**

```bash
cp .envTemplate .env
```

**Windows PowerShell**

```powershell
Copy-Item .envTemplate .env
```

Configure the following environment variables:

| Variable                | Description |
|-------------------------|-------------|
| `DB_URL`                | MySQL database connection URL |
| `DB_USERNAME`           | MySQL username |
| `DB_PASSWORD`           | MySQL password |
| `GOOGLE_CLIENT_ID`      | Google OAuth Client ID |
| `GOOGLE_CLIENT_SECRET`  | Google OAuth Client Secret |
| `BREVO_SMTP_HOST`       | Brevo SMTP host |
| `BREVO_SMTP_PORT`       | Brevo SMTP port |
| `BREVO_SMTP_USERNAME`   | Brevo SMTP username |
| `BREVO_SMTP_PASSWORD`   | Brevo SMTP password |
| `BREVO_SENDER_EMAIL`    | Verified sender email |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY`    | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |
| `JWT_SECRET`            | Secret key used for JWT generation |
| `FRONTEND_URL`          | Frontend application URL |



## 🚀 Clone the Repository

Clone the backend repository:

```bash
git clone https://github.com/Tanushri014/Campus_care_Backend.git
```

Navigate to the project directory:

```bash
cd Campus_care_Backend
```


## ▶️ Running the Application

### Build the project

```bash
mvn clean install
```

### Run the application

```bash
mvn spring-boot:run
```

The backend server will start on:

```text
http://localhost:8080
```

You can verify the application is running by visiting:

```text
http://localhost:8080/swagger-ui/index.html
```
the current project has a configuration for deployment ...for swagger to run add it in security config when the project is using the dev envrionment 

## 📝 Development Notes

This project is currently configured for deployment.

If you want to use **Swagger UI** while running the application locally, temporarily allow the Swagger endpoints in your `SecurityConfig`:

```java
.requestMatchers(
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/swagger-ui.html"
).permitAll()
```

After restarting the application, Swagger UI will be available at:

```text
http://localhost:8080/swagger-ui/index.html
```

For production deployments, it is recommended to disable or secure Swagger endpoints.


## 🚀 Deployment

The application is deployed using multiple cloud services.

| Component     | Platform |
|---------------|----------|
| Frontend      | Vercel |
| Backend       | Render |
| Database      | Railway (MySQL) |
| File Storage  | Cloudinary |
| Email Service | Brevo |

Before deploying, ensure that:

- Environment variables are configured correctly.
- Railway MySQL database is accessible.
- Cloudinary credentials are valid.
- Brevo SMTP credentials are configured.
- Google OAuth redirect URIs are updated.


## ⚠️ Known Limitations

This project was developed primarily as a learning project to explore full-stack application development and cloud deployment.

Current limitations include:

- Uses free-tier cloud services, which may introduce startup delays.
- Render backend may take time to wake up after periods of inactivity.
- Email delivery through Brevo's free SMTP service may occasionally be delayed.
- Performance depends on the limitations of free hosting platforms.
