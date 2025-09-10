-- Script thêm dữ liệu mẫu cho database casestudy
-- Chạy script này để có dữ liệu test trang chi tiết món ăn
create database `casestudy`;
USE casestudy;

-- 1. Thêm dữ liệu cho bảng roles
INSERT INTO roles (name) VALUES 
('ADMIN'),
('OWNER'), 
('CUSTOMER');

-- 2. Thêm dữ liệu cho bảng users
INSERT INTO user (username, password, email, phone, full_name, avatar_url) VALUES
('admin', '$2a$12$/NmbZs9tWCi7dPaF46ynseIsAaR6STGCA87SYHV3Ln.TdKJ/DZlVC', 'admin@codegym.vn', '0987654321', 'Admin System', NULL),
('owner1', '$2a$12$/NmbZs9tWCi7dPaF46ynseIsAaR6STGCA87SYHV3Ln.TdKJ/DZlVC', 'owner1@restaurant.com', '0123456789', 'Chủ nhà hàng 1', NULL),
('owner2', '$2a$12$/NmbZs9tWCi7dPaF46ynseIsAaR6STGCA87SYHV3Ln.TdKJ/DZlVC', 'owner2@restaurant.com', '0123456790', 'Chủ nhà hàng 2', NULL),
('customer1', '$2a$12$/NmbZs9tWCi7dPaF46ynseIsAaR6STGCA87SYHV3Ln.TdKJ/DZlVC', 'customer1@gmail.com', '0123456791', 'Khách hàng 1', NULL);

-- 3. Thêm dữ liệu cho bảng user_roles
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin có role ADMIN
(2, 2), -- owner1 có role OWNER
(3, 2), -- owner2 có role OWNER
(4, 3); -- customer1 có role CUSTOMER

-- 4. Thêm dữ liệu cho bảng restaurants
INSERT INTO restaurant (name, user_id, address, phone, logo_url, description, is_long_term_partner, is_open, is_locked, is_approved) VALUES
('Nhà hàng Hải Sản Biển Đông', 2, '123 Đường Trần Hưng Đạo, Quận 1, TP.HCM', '0901234567', '/images/logo.jpg', 'Chuyên về các món hải sản tươi ngon, được chế biến theo công thức truyền thống', true, true, false, true),
('Quán Cơm Tấm Sài Gòn', 3, '456 Đường Lê Lợi, Quận 3, TP.HCM', '0901234568', '/images/logo.jpg', 'Quán cơm tấm nổi tiếng với hương vị đặc trưng của Sài Gòn', true, true, false, true),
('Trà Sữa Milktea House', 1, '123 Nguyễn Trãi, Hà Nội', '0988123456', 'https://example.com/logo_milktea.png', 'Chuỗi cửa hàng chuyên trà sữa, trà chanh và đồ uống giới trẻ', TRUE, TRUE, FALSE, TRUE);

-- 5. Thêm dữ liệu cho bảng categories
INSERT INTO category (name) VALUES
('Cơm'),
('Phở'),
('Bún'),
('Hải sản'),
('Gà'),
('Rau củ'),
('Trà Sữa'), 
('Trà Chanh'),
('Topping');

-- 6. Thêm dữ liệu cho bảng tags
INSERT INTO tag (name, restaurant_id) VALUES
('Ngon', 1),
('Rẻ', 2),
('Đặc biệt', 1),
('Mới', 1),
('Bán chạy', 2),
('Khuyến mãi', 2),
('Truyền thống', 1),
('Hiện đại', 2),
('Ngọt nhẹ', 3),
('Best seller', 3),
('Giới trẻ yêu thích', 3);

-- 7. Thêm dữ liệu cho bảng dishes
INSERT INTO dish (name, restaurant_id, price, description, picture_url, tag_id, is_available, category_id) VALUES
-- Nhà hàng Hải Sản Biển Đông
('Cua rang me', 1, 250000, 'Cua biển tươi rang với me chua ngọt, thịt cua săn chắc, nước sốt đậm đà', '/images/logo.jpg', 1, true, 4),
('Tôm hùm nướng muối ớt', 1, 450000, 'Tôm hùm tươi nướng với muối ớt, thịt ngọt béo, hương vị đặc trưng', '/images/logo.jpg', 3, true, 4),
('Cá hồi nướng lá chuối', 1, 180000, 'Cá hồi tươi nướng trong lá chuối, giữ nguyên hương vị tự nhiên', '/images/logo.jpg', 1, true, 4),
('Mực xào chua ngọt', 1, 120000, 'Mực tươi xào với nước sốt chua ngọt, giòn sần sật', '/images/logo.jpg', 2, true, 4),
('Súp hải sản', 1, 80000, 'Súp nóng với tôm, mực, cá và rau củ, nước dùng đậm đà', '/images/logo.jpg', 1, true, 4),
('Gỏi cá trích', 1, 95000, 'Gỏi cá trích tươi với rau sống, nước mắm chua ngọt', '/images/logo.jpg', 2, true, 4),

-- Quán Cơm Tấm Sài Gòn
('Cơm tấm sườn nướng', 2, 55000, 'Cơm tấm với sườn heo nướng thơm lừng, bì chả, chả trứng', '/images/logo.jpg', 1, true, 1),
('Cơm tấm gà nướng', 2, 50000, 'Cơm tấm với gà nướng da giòn, thịt mềm, nước mắm đặc biệt', '/images/logo.jpg', 2, true, 1),
('Cơm tấm chả cá', 2, 60000, 'Cơm tấm với chả cá thơm ngon, bì chả, chả trứng', '/images/logo.jpg', 3, true, 1),
('Phở bò tái', 2, 65000, 'Phở bò với nước dùng đậm đà, bò tái mềm, bánh phở dai', '/images/logo.jpg', 1, true, 2),
('Bún chả cá', 2, 70000, 'Bún với chả cá tươi, nước dùng ngọt, rau sống tươi', '/images/logo.jpg', 2, true, 3),
('Gà nướng lá chanh', 2, 180000, 'Gà ta nướng với lá chanh, thịt mềm, da giòn', '/images/logo.jpg', 3, true, 5),
('Trà Sữa Trân Châu Đường Đen', 3, 39000, 'Trà sữa đậm vị, trân châu đường đen dẻo thơm', 'https://example.com/trasua_duongden.jpg', 10, TRUE, 7),
('Trà Sữa Matcha', 3, 42000, 'Matcha Nhật Bản, vị ngọt dịu', 'https://example.com/trasua_matcha.jpg', 9, TRUE, 7),
('Trà Sữa Oolong Kem Cheese', 3, 45000, 'Trà oolong thơm nhẹ, phủ kem cheese mặn mà', 'https://example.com/oolong_cheese.jpg', 10, TRUE, 7),
('Trà Sữa Thái Xanh', 3, 35000, 'Trà Thái xanh đặc trưng, béo thơm', 'https://example.com/tra_thai_xanh.jpg', 11, TRUE, 7),
('Trà Sữa Socola', 3, 37000, 'Kết hợp giữa vị cacao và sữa', 'https://example.com/tra_socola.jpg', 9, TRUE, 7),
('Trà Sữa Hồng Trà', 3, 36000, 'Hồng trà đậm đà, kết hợp sữa ngọt nhẹ', 'https://example.com/tra_hong.jpg', 10, TRUE, 7),

('Trà Chanh Sả Tắc', 3, 25000, 'Trà chanh thanh mát kết hợp sả và tắc', 'https://example.com/trachanh_sata.jpg', 11, TRUE, 8),
('Trà Chanh Cam Quế', 3, 28000, 'Trà chanh kèm cam tươi và hương quế', 'https://example.com/trachanh_camque.jpg', 9, TRUE, 8),
('Trà Chanh Đào', 3, 30000, 'Trà chanh kết hợp miếng đào giòn ngọt', 'https://example.com/trachanh_dao.jpg', 10, TRUE, 8),
('Trà Chanh Bạc Hà', 3, 27000, 'Trà chanh thanh mát với bạc hà', 'https://example.com/trachanh_bac_ha.jpg', 11, TRUE, 8),
('Trà Chanh Gừng Mật Ong', 3, 32000, 'Tốt cho sức khỏe, ấm nóng vị gừng', 'https://example.com/trachanh_gung.jpg', 9, TRUE, 8),
('Trà Chanh Kiwi', 3, 34000, 'Trà chanh pha kiwi chua ngọt lạ miệng', 'https://example.com/trachanh_kiwi.jpg', 10, TRUE, 8);

-- 8. Thêm dữ liệu cho bảng coupons
INSERT INTO coupon (name, restaurant_id, fixed_discount, percent_discount, min_order, max_discount, description, is_available) VALUES
-- Coupon cho Nhà hàng Hải Sản Biển Đông
('Giảm 50K cho đơn từ 500K', 1, 50000, NULL, 500000, NULL, 'Áp dụng cho tất cả món ăn', true),
('Giảm 15% cho đơn từ 1 triệu', 1, NULL, 15, 1000000, 200000, 'Giảm tối đa 200K', true),
('Giảm 100K cho đơn từ 800K', 1, 100000, NULL, 800000, NULL, 'Chỉ áp dụng vào thứ 2-4', true),

-- Coupon cho Quán Cơm Tấm Sài Gòn
('Giảm 20K cho đơn từ 100K', 2, 20000, NULL, 100000, NULL, 'Áp dụng cho tất cả món ăn', true),
('Giảm 10% cho đơn từ 200K', 2, NULL, 10, 200000, 50000, 'Giảm tối đa 50K', true),
('Miễn phí ship cho đơn từ 150K', 2, NULL, NULL, 150000, NULL, 'Áp dụng trong phạm vi 5km', true),
('Giảm 20%', 3, NULL, 20, 100000, 30000, 'Giảm 20% cho đơn từ 100k, tối đa 30k', TRUE),
('Freeship 15k', 3, 15000, NULL, 80000, NULL, 'Miễn phí ship 15k cho đơn từ 80k', TRUE);

-- 9. Thêm dữ liệu cho bảng dish_options
INSERT INTO dish_option (name, dish_id, price, description, is_available) VALUES
-- Options cho món Cua rang me
('Tăng size', 1, 50000, 'Tăng kích thước cua', true),
('Thêm me', 1, 15000, 'Thêm me chua', true),

-- Options cho món Tôm hùm nướng muối ớt
('Tăng size', 2, 100000, 'Tăng kích thước tôm hùm', true),
('Thêm muối ớt', 2, 20000, 'Thêm muối ớt', true),

-- Options cho món Cơm tấm sườn nướng
('Thêm sườn', 7, 25000, 'Thêm 1 miếng sườn', true),
('Thêm bì chả', 7, 15000, 'Thêm bì chả', true),
('Thêm chả trứng', 7, 10000, 'Thêm chả trứng', true),

('Thêm Trân Châu', 13, 5000, 'Trân châu truyền thống', TRUE),
('Thêm Thạch Phô Mai', 13, 7000, 'Thạch phô mai béo ngậy', TRUE),
('Thêm Kem Cheese', 15, 8000, 'Kem cheese mặn mà', TRUE),
('Ít Đá', 19, 0, 'Giữ hương vị đậm đà hơn', TRUE),
('Không Đường', 20, 0, 'Uống healthy hơn', TRUE),
('Đường 70%', 14, 0, 'Ngọt vừa phải', TRUE);

-- 10. Thêm dữ liệu cho bảng order_status
INSERT INTO order_status (name) VALUES
('Chờ xác nhận'),
('Đã xác nhận'),
('Đang chế biến'),
('Đang giao hàng'),
('Đã giao hàng'),
('Đã hủy');

-- 11. Thêm dữ liệu cho bảng shippers
INSERT INTO shipper (name, phone, deliver_minute, price, is_available) VALUES
('Nguyễn Văn A', '0901234569', 30, 50000, true),
('Trần Thị B', '0901234570', 25, 45000, true),
('Lê Văn C', '0901234571', 20, 40000, true);

-- 12. Thêm dữ liệu cho bảng orders (để test)
INSERT INTO orders (user_id, restaurant_id, shipper_id, order_status_id, coupon_id, customer_note) VALUES
(4, 1, 1, 5, 1, 'Giao hàng cẩn thận'),
(4, 2, 2, 5, 4, 'Không cay'),
(4, 1, 3, 4, 2, 'Giao nhanh'),
(1, 3, 1, 1, NULL, 'Ít đá, ngọt vừa'),
(2, 3, 2, 2, 1, 'Giao nhanh giúp mình nha'),
(1, 3, 3, 3, 2, 'Thêm nhiều trân châu');

-- 13. Thêm dữ liệu cho bảng order_details
INSERT INTO order_detail (order_id, dish_id, quantity) VALUES
(1, 1, 1),
(1, 3, 1),
(2, 7, 2),
(3, 2, 1),
(4, 13, 2), -- Đơn 4: 2 ly Trà Sữa Trân Châu Đường Đen
(4, 19, 1), -- Đơn 4: 1 ly Trà Chanh Sả Tắc
(5, 15, 1), -- Đơn 5: 1 ly Trà Sữa Oolong Kem Cheese
(5, 21, 2), -- Đơn 5: 2 ly Trà Chanh Đào
(6, 14, 1), -- Đơn 6: 1 ly Trà Sữa Matcha
(6, 20, 1); -- Đơn 6: 1 ly Trà Chanh Cam Quế

-- 14. Thêm dữ liệu cho bảng order_option_details
INSERT INTO oder_option_detail (order_detail_id, dish_option_id) VALUES
(1, 1),
(1, 2),
(3, 5),
(5, 1),  -- Trà Sữa Đường Đen thêm trân châu
(5, 2),  -- Trà Sữa Đường Đen thêm thạch phô mai
(7, 3),  -- Trà Oolong thêm Kem Cheese
(9, 6);  -- Trà Sữa Matcha mức ngọt 70%

INSERT INTO user_address (user_id, full_address, default_address) VALUES
(4, '123 Đường ABC, Quận 1, TP.HCM', 0),
(4, '456 Đường XYZ, Quận 3, TP.HCM', 0),
(1, '123 Nguyễn Trãi, Hà Nội', 1),
(2, '56 Lê Lợi, Hà Nội', 0);

UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/cua_rang_me.jpg' WHERE (`id` = '1');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/tom_hung_nuong.jpg' WHERE (`id` = '2');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/ca_hoi_nuong_la_chuoi.jpg' WHERE (`id` = '3');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/muc_sao_chua_ngot.jpg' WHERE (`id` = '4');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/sup_hai_san.jpg' WHERE (`id` = '5');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/com_tam.jpg' WHERE (`id` = '7');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/com_tam_ga_nuong.jpg' WHERE (`id` = '8');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/com-tam-cha-ca.jpg' WHERE (`id` = '9');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/pho-bo-tai.jpg' WHERE (`id` = '10');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/bun-cha-ca.jpg' WHERE (`id` = '11');
UPDATE `casestudy`.`dish` SET `picture_url` = 'eat_food/ga-nuong-la-tranh.jpg' WHERE (`id` = '12');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra-sua-chan-trau-duong-den.jpg' WHERE (`id` = '13');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra-sua-matcha.jpg' WHERE (`id` = '14');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra-oolong-kem-cheese.jpg' WHERE (`id` = '15');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra_thai_xanh.jpg' WHERE (`id` = '16');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra_sua_socola.jpg' WHERE (`id` = '17');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/hong-tra-sua-tran-chau-trang.jpg' WHERE (`id` = '18');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra-tac.jpg' WHERE (`id` = '19');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra-cam-que.jpg' WHERE (`id` = '20');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra-chanh-dao.jpg' WHERE (`id` = '21');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra-chanh-bac-ha.jpg' WHERE (`id` = '22');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/tra-gung-mat-ong.jpg' WHERE (`id` = '23');
UPDATE `casestudy`.`dish` SET `picture_url` = 'drinks/meo-khi-pha-che-tra-kiwi.jpg' WHERE (`id` = '24');


UPDATE `casestudy`.`category` SET `picture_url` = 'com.jpg' WHERE (`id` = '1');
UPDATE `casestudy`.`category` SET `picture_url` = 'pho.jpg' WHERE (`id` = '2');
UPDATE `casestudy`.`category` SET `picture_url` = 'bun.jpg' WHERE (`id` = '3');
UPDATE `casestudy`.`category` SET `picture_url` = 'hai-san.jpg' WHERE (`id` = '4');
UPDATE `casestudy`.`category` SET `picture_url` = 'ga.jpg' WHERE (`id` = '5');
UPDATE `casestudy`.`category` SET `picture_url` = 'rau.jpg' WHERE (`id` = '6');
UPDATE `casestudy`.`category` SET `picture_url` = 'tra-sua.jpg' WHERE (`id` = '7');
UPDATE `casestudy`.`category` SET `picture_url` = 'tra-chanh.jpg' WHERE (`id` = '8');
UPDATE `casestudy`.`category` SET `picture_url` = 'trai-cay.jpg ' WHERE (`id` = '9');


