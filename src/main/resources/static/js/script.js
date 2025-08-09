// script.js - Client-side functionality for login/registration system

document.addEventListener('DOMContentLoaded', function() {
    // Initialize forms
    initLoginForm();
    initRegistrationForm();
    initForgotPasswordForm();
    initResetPasswordForm();

    // Show/hide password functionality
    setupPasswordToggle();

    // Form validation
    setupFormValidations();
});

// Initialize Login Form
function initLoginForm() {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            if (!validateLoginForm()) {
                e.preventDefault();
            }
        });
    }
}

// Initialize Registration Form
function initRegistrationForm() {
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            if (!validateRegisterForm()) {
                e.preventDefault();
            }
        });

        // Password strength indicator
        const passwordInput = registerForm.querySelector('input[name="password"]');
        if (passwordInput) {
            passwordInput.addEventListener('input', showPasswordStrength);
        }
    }
}

// Initialize Forgot Password Form
function initForgotPasswordForm() {
    const forgotForm = document.getElementById('forgotPasswordForm');
    if (forgotForm) {
        forgotForm.addEventListener('submit', function(e) {
            if (!validateEmail(forgotForm.querySelector('input[name="email"]').value)) {
                e.preventDefault();
                showAlert('Please enter a valid email address', 'error');
            }
        });
    }
}

// Initialize Reset Password Form
function initResetPasswordForm() {
    const resetForm = document.getElementById('resetPasswordForm');
    if (resetForm) {
        resetForm.addEventListener('submit', function(e) {
            if (!validateResetPasswordForm()) {
                e.preventDefault();
            }
        });
    }
}

// Form Validations
function validateLoginForm() {
    const email = document.querySelector('#loginForm input[name="email"]').value;
    const password = document.querySelector('#loginForm input[name="password"]').value;

    if (!email || !password) {
        showAlert('Please fill in all fields', 'error');
        return false;
    }

    if (!validateEmail(email)) {
        showAlert('Please enter a valid email address', 'error');
        return false;
    }

    return true;
}

function validateRegisterForm() {
    const form = document.getElementById('registerForm');
    const firstName = form.querySelector('input[name="firstName"]').value;
    const lastName = form.querySelector('input[name="lastName"]').value;
    const email = form.querySelector('input[name="email"]').value;
    const password = form.querySelector('input[name="password"]').value;

    if (!firstName || !lastName || !email || !password) {
        showAlert('Please fill in all fields', 'error');
        return false;
    }

    if (!validateEmail(email)) {
        showAlert('Please enter a valid email address', 'error');
        return false;
    }

    if (password.length < 8) {
        showAlert('Password must be at least 8 characters long', 'error');
        return false;
    }

    return true;
}

function validateResetPasswordForm() {
    const password = document.querySelector('#resetPasswordForm input[name="password"]').value;

    if (!password) {
        showAlert('Please enter a new password', 'error');
        return false;
    }

    if (password.length < 8) {
        showAlert('Password must be at least 8 characters long', 'error');
        return false;
    }

    return true;
}

// Helper Functions
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function showAlert(message, type) {
    // Remove any existing alerts
    const existingAlert = document.querySelector('.alert');
    if (existingAlert) {
        existingAlert.remove();
    }

    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;

    // Prepend to form or use a dedicated alerts container
    const forms = document.querySelectorAll('form');
    if (forms.length > 0) {
        forms[0].parentNode.insertBefore(alertDiv, forms[0]);
    } else {
        document.body.prepend(alertDiv);
    }

    // Auto-hide after 5 seconds
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

// Password Strength Indicator
function showPasswordStrength() {
    const password = this.value;
    const strengthIndicator = document.getElementById('passwordStrength');

    if (!strengthIndicator) return;

    let strength = 0;
    if (password.length >= 8) strength++;
    if (password.match(/[a-z]/) && password.match(/[A-Z]/)) strength++;
    if (password.match(/[0-9]/)) strength++;
    if (password.match(/[^a-zA-Z0-9]/)) strength++;

    const strengthText = ['Weak', 'Fair', 'Good', 'Strong'];
    const strengthColors = ['#ff4d4d', '#ffa64d', '#4da6ff', '#4dff4d'];

    strengthIndicator.textContent = strengthText[strength - 1] || '';
    strengthIndicator.style.color = strengthColors[strength - 1] || '';
}

// Toggle Password Visibility
function setupPasswordToggle() {
    document.querySelectorAll('.password-toggle').forEach(toggle => {
        toggle.addEventListener('click', function() {
            const input = this.previousElementSibling;
            const icon = this.querySelector('i');

            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    });
}

// Setup Form Validations
function setupFormValidations() {
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function(e) {
            const requiredFields = this.querySelectorAll('[required]');
            let isValid = true;

            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    field.classList.add('error');
                    isValid = false;
                } else {
                    field.classList.remove('error');
                }
            });

            if (!isValid) {
                e.preventDefault();
                showAlert('Please fill in all required fields', 'error');
            }
        });
    });
}