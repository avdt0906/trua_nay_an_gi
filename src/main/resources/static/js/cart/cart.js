document.addEventListener('DOMContentLoaded', function () {
    const addToCartButtons = document.querySelectorAll('.add-to-cart-btn');
    const toastEl = document.getElementById('add-to-cart-toast');
    const toastBody = toastEl ? toastEl.querySelector('.toast-body') : null;
    const toast = toastEl ? new bootstrap.Toast(toastEl) : null;

    // Hàm cập nhật số lượng trên icon giỏ hàng
    function updateCartCountUI(count) {
        const cartCountEl = document.getElementById('cart-item-count');
        if (cartCountEl) {
            cartCountEl.innerText = count;
            cartCountEl.style.display = count > 0 ? 'block' : 'none';
        }
    }

    addToCartButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();

            const dishId = this.getAttribute('data-dish-id');
            const formData = new FormData();
            formData.append('dishId', dishId);
            formData.append('quantity', 1);

            fetch('/cart/add', {
                method: 'POST',
                body: formData,
            })
                .then(response => response.json()) // <<< THAY ĐỔI 1: Luôn parse response dạng JSON
                .then(data => {
                    // Xử lý khi server trả về lỗi nghiệp vụ (ví dụ: thêm món từ nhà hàng khác)
                    if (data.message && !data.hasOwnProperty('cartItemCount')) {
                        throw new Error(data.message);
                    }

                    console.log('Success:', data.message);

                    // Cập nhật thông báo và hiển thị toast
                    if (toastBody && toast) {
                        toastBody.textContent = data.message || 'Thêm vào giỏ hàng thành công!';
                        toast.show();
                    }

                    // <<< THAY ĐỔI 2: Dùng trực tiếp số lượng từ response của API /cart/add
                    if (data.cartItemCount !== undefined) {
                        updateCartCountUI(data.cartItemCount);
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                    // Hiển thị thông báo lỗi cho người dùng
                    if (toastBody && toast) {
                        toastBody.textContent = error.message || 'Có lỗi xảy ra, vui lòng thử lại.';
                        // Đổi màu nền toast thành màu cảnh báo (nếu muốn)
                        toastEl.classList.remove('bg-success');
                        toastEl.classList.add('bg-danger');
                        toast.show();
                        // Reset lại màu sau khi toast ẩn đi
                        toastEl.addEventListener('hidden.bs.toast', function () {
                            toastEl.classList.remove('bg-danger');
                            toastEl.classList.add('bg-success');
                        }, { once: true });
                    }
                });
        });
    });

    // Hàm lấy số lượng ban đầu khi tải trang (giữ nguyên)
    function getInitialCartCount() {
        fetch('/cart/count')
            .then(response => response.json())
            .then(count => updateCartCountUI(count))
            .catch(error => console.error('Error getting initial cart count:', error));
    }

    // Gọi hàm để lấy số lượng chính xác khi trang vừa tải xong
    getInitialCartCount();
});