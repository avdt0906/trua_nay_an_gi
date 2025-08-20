document.addEventListener('DOMContentLoaded', function () {
    const addToCartButtons = document.querySelectorAll('.add-to-cart-btn');
    const toastEl = document.getElementById('add-to-cart-toast');
    const toast = new bootstrap.Toast(toastEl);

    addToCartButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Ngăn chặn hành vi mặc định của thẻ <a>

            const dishId = this.getAttribute('data-dish-id');
            const quantity = 1; // Mặc định thêm 1 sản phẩm

            const formData = new FormData();
            formData.append('dishId', dishId);
            formData.append('quantity', quantity);

            // Gửi yêu cầu AJAX đến server
            fetch('/cart/add', {
                method: 'POST',
                body: formData,
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(data => {
                    console.log('Success:', data);
                    // Hiển thị thông báo toast
                    toast.show();
                    // Gọi hàm cập nhật số lượng từ server
                    updateCartCountFromServer();
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
        });
    });

    /**
     * Hàm mới: Lấy số lượng item thực tế từ server và cập nhật UI.
     * Đây là cách làm chính xác và đáng tin cậy.
     */
    function updateCartCountFromServer() {
        fetch('/cart/count')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch cart count');
                }
                return response.json(); // Server trả về một số (Integer)
            })
            .then(count => {
                const cartCountEl = document.getElementById('cart-item-count');
                if (cartCountEl) {
                    cartCountEl.innerText = count;
                    // Ẩn/hiện badge tùy thuộc vào số lượng
                    if (count > 0) {
                        cartCountEl.style.display = 'block';
                    } else {
                        cartCountEl.style.display = 'none';
                    }
                }
            })
            .catch(error => console.error('Error updating cart count:', error));
    }

    // Gọi hàm này một lần khi trang được tải để đảm bảo số lượng ban đầu là chính xác.
    updateCartCountFromServer();
});
