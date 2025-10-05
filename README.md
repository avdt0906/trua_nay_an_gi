# Trưa Nay Ăn Gì — Food Ordering System

**Short description**  
Server-side rendered food ordering web application built with Spring Boot and Thymeleaf. Features user authentication, role-based access, CRUD management for restaurants/menus/orders/users, Google Maps integration for restaurant locations, and order tracking.

> Repository: https://github.com/avdt0906/trua_nay_an_gi

---

## Demo / Screenshots
<div align="center">
  <p><strong>1. User Screenshots</strong></p>
  <table>
    <tr>
      <td align="center">
        <img src="uploads/screenshots/homepage.png" alt="Homepage" width="300"><br>
        <sub>Homepage</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/login.png" alt="Login" width="300"><br>
        <sub>Login</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/user_address.png" alt="Add New Address" width="300"><br>
        <sub>Add New Address</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/cart.png" alt="Cart" width="300"><br>
        <sub>Cart</sub>
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="uploads/screenshots/dish_detail.png" alt="Dish Detail" width="300"><br>
        <sub>Dish Detail</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/order.png" alt="Order" width="300"><br>
        <sub>Order</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/order_history.png" alt="Order History" width="300"><br>
        <sub>Order</sub>
      </td><td align="center">
        <img src="uploads/screenshots/restaurant.png" alt="Restaurant" width="300"><br>
        <sub>Restaurant</sub>
      </td>
    </tr>
  </table>
</div>

<div align="center">
  <p><strong>2. Restaurant Owner Screenshots</strong></p>
  <table>
    <tr>
      <td align="center">
        <img src="uploads/screenshots/homepage.png" alt="Homepage" width="300"><br>
        <sub>Homepage</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/login.png" alt="Login" width="300"><br>
        <sub>Login</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/cart.png" alt="Cart" width="300"><br>
        <sub>Cart</sub>
      </td>
    </tr>
  </table>
</div>

<div align="center">
  <p><strong>3. Admin Screenshots</strong></p>
  <table>
    <tr>
      <td align="center">
        <img src="uploads/screenshots/admin_dashboard.png" alt="Admin Dashboard" width="300"><br>
        <sub>Admin Dashboard</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/admin_account_list.png" alt="Account List" width="300"><br>
        <sub>Account List</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/admin_shipper_list.png" alt="Shipper List" width="300"><br>
        <sub>Shipper List</sub>
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="uploads/screenshots/admin_restaurant_list.png" alt="Restaurant List" width="300"><br>
        <sub>Restaurant List</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/admin_dish_list.png" alt="Dish List" width="300"><br>
        <sub>Dish List</sub>
      </td>
      <td align="center">
        <img src="uploads/screenshots/admin_order_list.png" alt="Order List" width="300"><br>
        <sub>Order List</sub>
      </td>
    </tr>
  </table>
</div>
---

## Features
- Server-side rendered UI using **Thymeleaf**.
- Authentication & authorization with **Spring Security** (role-based).
- CRUD for **Users**, **Restaurants**, **Menus**, **Orders**, etc.
- Order lifecycle and delivery assignment / order tracking.
- Google Maps integration to show restaurant locations.
- Database scripts for initial data (see `du_lieu_hinh_anh.sql`).

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
