document.addEventListener('DOMContentLoaded', function () {
    const cartContainer = document.getElementById('cart-items-container');
    if (!cartContainer) return;

    // --- Tham chiếu đến các element ---
    const confirmDeleteModalEl = document.getElementById('confirmDeleteModal');
    const confirmDeleteModal = new bootstrap.Modal(confirmDeleteModalEl);
    const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
    const selectAllCheckbox = document.getElementById('select-all-checkbox');
    const checkoutButton = document.querySelector('a.btn-danger');

    let itemIdToDelete = null;

    // --- Hàm định dạng tiền tệ ---
    const currencyFormatter = new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });

    // --- Hàm cập nhật tổng tiền ---
    function updateTotals() {
        let subtotal = 0;
        const selectedItems = document.querySelectorAll('.item-checkbox:checked');

        document.querySelectorAll('.cart-item-row').forEach(row => {
            row.style.backgroundColor = '#fff';
        });

        selectedItems.forEach(checkbox => {
            const row = checkbox.closest('.cart-item-row');
            if (row) {
                row.style.backgroundColor = '#f8f9fa'; // Highlight dòng được chọn
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
                checkoutButton.classList.toggle('disabled', selectedItems.length === 0);
            }
        }
    }

    // --- Hàm cập nhật trạng thái của checkbox "Chọn tất cả" ---
    function updateSelectAllCheckboxState() {
        if (selectAllCheckbox) {
            const itemCheckboxes = document.querySelectorAll('.item-checkbox');
            const allChecked = itemCheckboxes.length > 0 && Array.from(itemCheckboxes).every(cb => cb.checked);
            const someChecked = Array.from(itemCheckboxes).some(cb => cb.checked);

            selectAllCheckbox.checked = allChecked;
            selectAllCheckbox.indeterminate = !allChecked && someChecked;
        }
    }

    // --- SỬA ĐỔI: Thêm lại hàm cập nhật số lượng trên navbar ---
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

    // --- Lắng nghe sự kiện trên container của các món hàng ---
    cartContainer.addEventListener('change', function(event) {
        if (event.target.classList.contains('quantity-input')) {
            const row = event.target.closest('.cart-item-row');
            const price = parseFloat(row.getAttribute('data-item-price'));
            const quantity = parseInt(event.target.value);
            const subtotalEl = row.querySelector('.item-subtotal');
            subtotalEl.innerText = currencyFormatter.format(price * quantity);
            updateTotals();
        }

        if (event.target.classList.contains('item-checkbox')) {
            updateSelectAllCheckboxState();
            updateTotals();
        }
    });

    // --- Lắng nghe sự kiện click cho nút xóa ---
    cartContainer.addEventListener('click', function(event) {
        const removeButton = event.target.closest('.remove-item-btn');
        if (removeButton) {
            event.preventDefault();
            itemIdToDelete = removeButton.getAttribute('data-item-id');
        }
    });

    // --- Logic cho checkbox "Chọn tất cả" ---
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function() {
            document.querySelectorAll('.item-checkbox').forEach(checkbox => {
                checkbox.checked = this.checked;
            });
            updateTotals();
        });
    }

    // --- Logic cho nút "Xóa" trong Modal ---
    confirmDeleteBtn.addEventListener('click', function() {
        if (itemIdToDelete) {
            fetch(`/cart/remove/${itemIdToDelete}`, { method: 'POST' })
                .then(response => response.ok ? response.json() : Promise.reject('Lỗi khi xóa'))
                .then(data => {
                    console.log(data.message);
                    const rowToDelete = cartContainer.querySelector(`.cart-item-row[data-item-id='${itemIdToDelete}']`);
                    if (rowToDelete) rowToDelete.remove();
                    updateTotals();
                    updateSelectAllCheckboxState();
                    // SỬA ĐỔI: Gọi lại hàm cập nhật số lượng sau khi xóa
                    updateCartCountFromServer();
                })
                .catch(error => console.error('Error:', error))
                .finally(() => {
                    confirmDeleteModal.hide();
                    itemIdToDelete = null;
                });
        }
    });

    // --- Logic cho nút thanh toán ---
    if (checkoutButton) {
        checkoutButton.addEventListener('click', function(event) {
            event.preventDefault();

            const selectedItems = document.querySelectorAll('.item-checkbox:checked');
            if (selectedItems.length === 0) {
                alert('Vui lòng chọn ít nhất một sản phẩm để thanh toán.');
                return;
            }

            const selectedItemIds = Array.from(selectedItems).map(cb => cb.value);
            const params = new URLSearchParams();
            selectedItemIds.forEach(id => params.append('selectedItems', id));

            window.location.href = `/cart/checkout?${params.toString()}`;
        });
    }

    // --- Khởi tạo khi tải trang ---
    updateSelectAllCheckboxState();
    updateTotals();
});
