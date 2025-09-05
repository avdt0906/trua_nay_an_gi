document.addEventListener('DOMContentLoaded', function () {
    const cartContainer = document.getElementById('cart-items-container');
    if (!cartContainer) return;

    // --- Tham chiếu đến các element ---
    const confirmDeleteModalEl = document.getElementById('confirmDeleteModal');
    const confirmDeleteModal = new bootstrap.Modal(confirmDeleteModalEl);
    const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
    const selectAllButton = document.getElementById('select-all-btn');
    const checkoutButton = document.getElementById('checkout-btn');

    let itemIdToDelete = null;

    // --- Hàm định dạng tiền tệ ---
    const currencyFormatter = new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });

    // --- Hàm cập nhật tổng tiền và trạng thái nút thanh toán ---
    function updateTotals() {
        console.log("Updating totals now..."); // Thêm log để dễ dàng kiểm tra
        let subtotal = 0;
        const selectedItems = document.querySelectorAll('.item-checkbox:checked');

        document.querySelectorAll('.cart-item-row').forEach(row => {
            row.style.backgroundColor = '#fff';
        });

        selectedItems.forEach(checkbox => {
            const row = checkbox.closest('.cart-item-row');
            if (row) {
                row.style.backgroundColor = '#f0f8ff';
                const price = parseFloat(row.getAttribute('data-item-price'));
                const quantity = parseInt(row.querySelector('.quantity-input').value);
                subtotal += price * quantity;
            }
        });

        const shippingFee = subtotal > 0 ? 15000 : 0;
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

            if (checkoutButton) {
                checkoutButton.disabled = selectedItems.length === 0;
            }
        }
    }

    // --- Hàm cập nhật số lượng trên navbar ---
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

    // --- Lắng nghe các sự kiện thay đổi trong giỏ hàng ---
    cartContainer.addEventListener('change', function(event) {
        const target = event.target;
        if (target.classList.contains('quantity-input') || target.classList.contains('item-checkbox')) {
            updateTotals();
        }
    });

    // --- Lắng nghe các sự kiện click ---
    document.body.addEventListener('click', function(event) {
        const target = event.target;

        // Nút "Chọn tất cả"
        if (target.id === 'select-all-btn') {
            const allCheckboxes = document.querySelectorAll('.item-checkbox');
            const isAllSelected = Array.from(allCheckboxes).every(cb => cb.checked);
            allCheckboxes.forEach(checkbox => {
                checkbox.checked = !isAllSelected;
            });
            updateTotals();
        }

        // Nút "Xóa" trong modal
        if (target.id === 'confirm-delete-btn' && itemIdToDelete) {
            fetch(`/cart/remove/${itemIdToDelete}`, { method: 'POST' })
                .then(response => response.ok ? response.json() : Promise.reject('Lỗi khi xóa'))
                .then(data => {
                    console.log(data.message);
                    const rowToDelete = cartContainer.querySelector(`.cart-item-row[data-item-id='${itemIdToDelete}']`);
                    if (rowToDelete) rowToDelete.remove();
                    updateTotals();
                    updateCartCountFromServer();
                })
                .catch(error => console.error('Error:', error))
                .finally(() => {
                    confirmDeleteModal.hide();
                    itemIdToDelete = null;
                });
        }

        // Nút thanh toán
        if(target.id === 'checkout-btn'){
            event.preventDefault();

            const selectedItems = document.querySelectorAll('.item-checkbox:checked');
            const selectedItemIds = Array.from(selectedItems).map(cb => cb.value);
            const params = new URLSearchParams();
            selectedItemIds.forEach(id => params.append('selectedItems', id));

            window.location.href = `/cart/checkout?${params.toString()}`;
        }

        // Nút xóa item (lấy id để đưa vào modal)
        const removeButton = target.closest('.remove-item-btn');
        if (removeButton) {
            event.preventDefault();
            itemIdToDelete = removeButton.getAttribute('data-item-id');
        }
    });


    // SỬA ĐỔI: ĐÂY LÀ PHẦN QUAN TRỌNG NHẤT
    // ======================================================================
    // Luôn kiểm tra lại trạng thái khi trang được hiển thị (tải mới hoặc back)
    window.addEventListener('pageshow', function(event) {
        // event.persisted cho biết trang có được khôi phục từ cache hay không
        console.log(`Page show event fired. Persisted: ${event.persisted}`);
        updateTotals();
    });
    // ======================================================================
});