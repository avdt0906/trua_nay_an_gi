document.addEventListener('DOMContentLoaded', function () {
    const cartContainer = document.getElementById('cart-items-container');
    if (!cartContainer) return;

    // --- Tham chiếu đến Modal ---
    const confirmDeleteModalEl = document.getElementById('confirmDeleteModal');
    const confirmDeleteModal = new bootstrap.Modal(confirmDeleteModalEl);
    const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
    let itemIdToDelete = null; // Biến tạm để lưu ID item cần xóa

    // --- Hàm định dạng tiền tệ ---
    const currencyFormatter = new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });

    // --- Hàm cập nhật tổng tiền ---
    function updateTotals() {
        let subtotal = 0;
        document.querySelectorAll('.cart-item-row').forEach(row => {
            const price = parseFloat(row.getAttribute('data-item-price'));
            const quantity = parseInt(row.querySelector('.quantity-input').value);
            subtotal += price * quantity;
        });

        const shippingFee = 15000; // Phí ship cố định
        const total = subtotal + shippingFee;

        const cartContent = document.getElementById('cart-content');
        const emptyCartMessage = document.getElementById('empty-cart-message');

        if (document.querySelectorAll('.cart-item-row').length === 0) {
            if (cartContent) cartContent.style.display = 'none';
            if (emptyCartMessage) emptyCartMessage.style.display = 'block';
        } else {
            document.getElementById('cart-subtotal').innerText = currencyFormatter.format(subtotal);
            document.getElementById('shipping-fee').innerText = currencyFormatter.format(shippingFee);
            document.getElementById('cart-total').innerText = currencyFormatter.format(total);
        }
    }

    // --- Lắng nghe sự kiện click trên toàn bộ container ---
    cartContainer.addEventListener('click', function(event) {
        const removeButton = event.target.closest('.remove-item-btn');
        if (removeButton) {
            event.preventDefault();
            // Lấy ID từ nút và lưu lại để modal sử dụng
            itemIdToDelete = removeButton.getAttribute('data-item-id');
            // Modal sẽ tự động hiển thị nhờ thuộc tính data-bs-toggle & data-bs-target
        }
    });

    // --- Thêm sự kiện click cho nút "Xóa" trong Modal ---
    confirmDeleteBtn.addEventListener('click', function() {
        if (itemIdToDelete) {
            fetch(`/cart/remove/${itemIdToDelete}`, {
                method: 'POST',
            })
                .then(response => {
                    if (!response.ok) throw new Error('Lỗi khi xóa sản phẩm');
                    return response.json();
                })
                .then(data => {
                    console.log(data.message);
                    const rowToDelete = cartContainer.querySelector(`.cart-item-row[data-item-id='${itemIdToDelete}']`);
                    if (rowToDelete) rowToDelete.remove();
                    updateTotals();
                    updateCartCountFromServer();
                })
                .catch(error => console.error('Error:', error))
                .finally(() => {
                    // Đóng modal và reset ID
                    confirmDeleteModal.hide();
                    itemIdToDelete = null;
                });
        }
    });

    // --- Xử lý sự kiện thay đổi số lượng ---
    cartContainer.addEventListener('change', function(event) {
        if (event.target.classList.contains('quantity-input')) {
            const row = event.target.closest('.cart-item-row');
            const itemId = row.getAttribute('data-item-id');
            const price = parseFloat(row.getAttribute('data-item-price'));
            const quantity = parseInt(event.target.value);

            const subtotalEl = row.querySelector('.item-subtotal');
            subtotalEl.innerText = currencyFormatter.format(price * quantity);

            updateTotals();

            const formData = new FormData();
            formData.append('itemId', itemId);
            formData.append('quantity', quantity);

            fetch('/cart/update', {
                method: 'POST',
                body: formData
            }).catch(error => console.error('Error updating cart:', error));
        }
    });

    // Hàm này được định nghĩa trong cart.js, đảm bảo cart.js được load trước
    function updateCartCountFromServer() {
        fetch('/cart/count')
            .then(response => response.json())
            .then(count => {
                const cartCountEl = document.getElementById('cart-item-count');
                if (cartCountEl) {
                    cartCountEl.innerText = count;
                    cartCountEl.style.display = count > 0 ? 'block' : 'none';
                }
            })
            .catch(error => console.error('Error updating cart count:', error));
    }

    // Khởi tạo tính toán và ẩn/hiện thông báo giỏ hàng trống khi tải trang
    if (document.querySelectorAll('.cart-item-row').length > 0) {
        const emptyCartMessage = document.getElementById('empty-cart-message');
        if(emptyCartMessage) emptyCartMessage.style.display = 'none';
        updateTotals();
    } else {
        const cartContent = document.getElementById('cart-content');
        if(cartContent) cartContent.style.display = 'none';
    }
});
