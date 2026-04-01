# AI Chat Application

## Overview
This project is a real-time AI-powered chat application with user authentication.  
It uses a **Spring Boot backend** and a **React + TypeScript frontend** with WebSocket support for live messaging.

---

## Architecture

### Backend
- **Framework:** Spring Boot 3.x
- **Dependencies:** 
  - Spring Web (REST APIs)  
  - Spring Data JPA (Database access)  
  - Spring Security + JWT (Authentication & Authorization)  
  - Spring WebSocket + STOMP (Real-time messaging)  
  - Lombok (Boilerplate reduction)  
- **Database:** Postgres (or H2 for development)  
- **Structure:**