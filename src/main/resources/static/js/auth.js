// Enhanced Authentication Form Experience
document.addEventListener('DOMContentLoaded', function() {
    
    // Form submission loading state
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) {
                submitBtn.classList.add('loading');
                submitBtn.disabled = true;
                
                // Re-enable after 3 seconds in case of error
                setTimeout(() => {
                    submitBtn.classList.remove('loading');
                    submitBtn.disabled = false;
                }, 3000);
            }
        });
    });
    
    // Enhanced input focus effects
    const inputs = document.querySelectorAll('.form-control');
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('focused');
        });
        
        input.addEventListener('blur', function() {
            if (!this.value) {
                this.parentElement.classList.remove('focused');
            }
        });
        
        // Check if input has value on page load
        if (input.value) {
            input.parentElement.classList.add('focused');
        }
    });
    
    // Password strength indicator (for register page)
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            const password = this.value;
            const strength = checkPasswordStrength(password);
            updatePasswordStrengthUI(strength);
        });
    }
    
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', function() {
            const password = passwordInput.value;
            const confirmPassword = this.value;
            validatePasswordMatch(password, confirmPassword);
        });
    }
    
    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert.classList.contains('show')) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        }, 5000);
    });
    
    // Add floating labels effect
    addFloatingLabels();
});

function checkPasswordStrength(password) {
    let strength = 0;
    
    if (password.length >= 6) strength++;
    if (password.match(/[a-z]/)) strength++;
    if (password.match(/[A-Z]/)) strength++;
    if (password.match(/[0-9]/)) strength++;
    if (password.match(/[^a-zA-Z0-9]/)) strength++;
    
    return strength;
}

function updatePasswordStrengthUI(strength) {
    // Remove existing strength indicator
    const existingIndicator = document.querySelector('.password-strength');
    if (existingIndicator) {
        existingIndicator.remove();
    }
    
    const passwordInput = document.getElementById('password');
    if (!passwordInput || !passwordInput.value) return;
    
    const strengthContainer = document.createElement('div');
    strengthContainer.className = 'password-strength mt-2';
    
    const strengthBar = document.createElement('div');
    strengthBar.className = 'progress';
    strengthBar.style.height = '4px';
    
    const strengthFill = document.createElement('div');
    strengthFill.className = 'progress-bar';
    strengthFill.style.transition = 'all 0.3s ease';
    
    let strengthText = '';
    let strengthColor = '';
    let strengthWidth = 0;
    
    switch(strength) {
        case 0:
        case 1:
            strengthText = 'Yếu';
            strengthColor = '#dc3545';
            strengthWidth = 20;
            break;
        case 2:
            strengthText = 'Trung bình';
            strengthColor = '#ffc107';
            strengthWidth = 40;
            break;
        case 3:
            strengthText = 'Khá';
            strengthColor = '#fd7e14';
            strengthWidth = 60;
            break;
        case 4:
            strengthText = 'Mạnh';
            strengthColor = '#28a745';
            strengthWidth = 80;
            break;
        case 5:
            strengthText = 'Rất mạnh';
            strengthColor = '#20c997';
            strengthWidth = 100;
            break;
    }
    
    strengthFill.style.width = strengthWidth + '%';
    strengthFill.style.backgroundColor = strengthColor;
    
    const strengthLabel = document.createElement('small');
    strengthLabel.className = 'text-muted ms-2';
    strengthLabel.textContent = `Độ mạnh: ${strengthText}`;
    strengthLabel.style.color = strengthColor;
    
    strengthBar.appendChild(strengthFill);
    strengthContainer.appendChild(strengthBar);
    strengthContainer.appendChild(strengthLabel);
    
    passwordInput.parentElement.appendChild(strengthContainer);
}

function validatePasswordMatch(password, confirmPassword) {
    const confirmInput = document.getElementById('confirmPassword');
    if (!confirmInput) return;
    
    // Remove existing match indicator
    const existingIndicator = document.querySelector('.password-match');
    if (existingIndicator) {
        existingIndicator.remove();
    }
    
    if (!confirmPassword) return;
    
    const matchContainer = document.createElement('div');
    matchContainer.className = 'password-match mt-1';
    
    const matchText = document.createElement('small');
    
    if (password === confirmPassword) {
        matchText.className = 'text-success';
        matchText.innerHTML = '<i class="fas fa-check-circle me-1"></i>Mật khẩu khớp';
        confirmInput.classList.remove('is-invalid');
        confirmInput.classList.add('is-valid');
    } else {
        matchText.className = 'text-danger';
        matchText.innerHTML = '<i class="fas fa-times-circle me-1"></i>Mật khẩu không khớp';
        confirmInput.classList.remove('is-valid');
        confirmInput.classList.add('is-invalid');
    }
    
    matchContainer.appendChild(matchText);
    confirmInput.parentElement.appendChild(matchContainer);
}

function addFloatingLabels() {
    const formGroups = document.querySelectorAll('.mb-3, .mb-4');
    
    formGroups.forEach(group => {
        const input = group.querySelector('.form-control');
        const label = group.querySelector('.form-label');
        
        if (input && label) {
            // Add floating label styles
            group.style.position = 'relative';
            
            input.addEventListener('focus', function() {
                label.style.transform = 'translateY(-8px) scale(0.85)';
                label.style.color = 'var(--primary-color)';
            });
            
            input.addEventListener('blur', function() {
                if (!this.value) {
                    label.style.transform = 'translateY(0) scale(1)';
                    label.style.color = 'var(--text-dark)';
                }
            });
            
            // Check initial state
            if (input.value) {
                label.style.transform = 'translateY(-8px) scale(0.85)';
                label.style.color = 'var(--primary-color)';
            }
        }
    });
}

// Add smooth page transitions
function addPageTransitions() {
    // Fade in animation on page load
    document.body.style.opacity = '0';
    document.body.style.transition = 'opacity 0.3s ease-in-out';
    
    window.addEventListener('load', function() {
        document.body.style.opacity = '1';
    });
    
    // Fade out on navigation
    const links = document.querySelectorAll('a:not([target="_blank"])');
    links.forEach(link => {
        link.addEventListener('click', function(e) {
            if (this.hostname === window.location.hostname) {
                e.preventDefault();
                document.body.style.opacity = '0';
                setTimeout(() => {
                    window.location.href = this.href;
                }, 150);
            }
        });
    });
}

// Initialize page transitions
addPageTransitions();
