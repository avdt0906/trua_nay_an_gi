document.addEventListener('DOMContentLoaded', function () {
    const cartContainer = document.getElementById('cart-items-container');
    if (!cartContainer) return;

    const confirmDeleteModalEl = document.getElementById('confirmDeleteModal');
    const confirmDeleteModal = new bootstrap.Modal(confirmDeleteModalEl);
    const checkoutButton = document.getElementById('checkout-btn');
    let itemIdToDelete = null;

    const currencyFormatter = new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });

    function updateTotals() {
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

        if (document.querySelectorAll('.cart-item-row').length === 0) {
            const cartContent = document.getElementById('cart-content');
            const emptyCartMessage = document.getElementById('empty-cart-message');
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

    function debounce(func, delay = 500) {
        let timeout;
        return function(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => {
                func.apply(this, args);
            }, delay);
        };
    }

    function updateQuantityOnServer(itemId, quantity) {
        const formData = new FormData();
        formData.append('itemId', itemId);
        formData.append('quantity', quantity);

        fetch('/cart/update', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.ok ? response.json() : Promise.reject('Lỗi cập nhật'))
            .then(data => console.log('Server response:', data.message))
            .catch(error => console.error('Error updating quantity:', error));
    }

    const debouncedUpdate = debounce(updateQuantityOnServer);

    // =============================================================
    // === START: ĐOẠN CODE SỬA ĐỔI / THÊM MỚI ===
    // =============================================================

    // --- Lắng nghe sự kiện click trong khu vực giỏ hàng ---
    cartContainer.addEventListener('click', function(event) {
        const target = event.target;

        // Xử lý nút cộng/trừ số lượng
        const minusBtn = target.closest('.quantity-minus');
        const plusBtn = target.closest('.quantity-plus');

        if (minusBtn || plusBtn) {
            const row = target.closest('.cart-item-row');
            const input = row.querySelector('.quantity-input');
            let quantity = parseInt(input.value);

            if (minusBtn) {
                quantity = Math.max(1, quantity - 1);
            } else if (plusBtn) {
                quantity += 1;
            }

            input.value = quantity;

            const itemId = row.getAttribute('data-item-id');
            const price = parseFloat(row.getAttribute('data-item-price'));
            const itemSubtotalEl = row.querySelector('.item-subtotal');
            if (itemSubtotalEl) {
                itemSubtotalEl.innerText = currencyFormatter.format(price * quantity);
            }

            updateTotals();
            debouncedUpdate(itemId, quantity);
        }
    });

    // --- Lắng nghe sự kiện thay đổi của checkbox ---
    cartContainer.addEventListener('change', function(event) {
        if (event.target.classList.contains('item-checkbox')) {
            updateTotals();
        }
    });

    // =============================================================
    // === END: ĐOẠN CODE SỬA ĐỔI / THÊM MỚI ===
    // =============================================================

    document.body.addEventListener('click', function(event) {
        const target = event.target;

        if (target.id === 'select-all-btn') {
            const allCheckboxes = document.querySelectorAll('.item-checkbox');
            const isAllSelected = Array.from(allCheckboxes).every(cb => cb.checked);
            allCheckboxes.forEach(checkbox => {
                checkbox.checked = !isAllSelected;
            });
            updateTotals();
        }

        if (target.id === 'confirm-delete-btn' && itemIdToDelete) {
            fetch(`/cart/remove/${itemIdToDelete}`, { method: 'POST' })
                .then(response => response.ok ? response.json() : Promise.reject('Lỗi khi xóa'))
                .then(data => {
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

        if(target.id === 'checkout-btn'){
            event.preventDefault();
            const selectedItems = document.querySelectorAll('.item-checkbox:checked');
            const selectedItemIds = Array.from(selectedItems).map(cb => {
                const row = cb.closest('.cart-item-row');
                return row.getAttribute('data-item-id');
            });
            const params = new URLSearchParams();
            selectedItemIds.forEach(id => params.append('selectedItems', id));

            window.location.href = `/cart/checkout?${params.toString()}`;
        }

        const removeButton = target.closest('.remove-item-btn');
        if (removeButton) {
            event.preventDefault();
            itemIdToDelete = removeButton.getAttribute('data-item-id');
        }
    });

    window.addEventListener('pageshow', function(event) {
        updateTotals();
    });

    updateTotals();
});

