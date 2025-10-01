# Trưa Nay Ăn Gì — Food Ordering System

**Short description**  
Server-side rendered food ordering web application built with Spring Boot and Thymeleaf. Features user authentication, role-based access, CRUD management for restaurants/menus/orders/users, Google Maps integration for restaurant locations, and order tracking.

> Repository: https://github.com/avdt0906/trua_nay_an_gi. :contentReference[oaicite:1]{index=1}

---

## Demo / Screenshots
*(Add screenshots in `docs/` or the repo root and reference them here)*

---

## Features
- Server-side rendered UI using **Thymeleaf**.
- Authentication & authorization with **Spring Security** (role-based).
- CRUD for **Users**, **Restaurants**, **Menus**, **Orders**, etc.
- Order lifecycle and delivery assignment / order tracking.
- Google Maps integration to show restaurant locations.
- Database scripts for initial data (see `du_lieu_hinh_anh.sql`). :contentReference[oaicite:2]{index=2}

---

## Tech Stack
- Java + Spring Boot  
- Thymeleaf (server-side templates)  
- Spring Security  
- JPA / Hibernate  
- MySQL  
- Gradle (build system). See `build.gradle`. :contentReference[oaicite:3]{index=3}

---

## Prerequisites
- Java 11 or higher (JDK installed)
- MySQL (or compatible database)
- Git
- (Optional) IDE: IntelliJ IDEA / Eclipse
- (Optional) Google Maps API key (if you want maps in local run)

---

## Quick start — Run locally

1. **Clone repository**
```bash
git clone https://github.com/avdt0906/trua_nay_an_gi.git
cd trua_nay_an_gi
