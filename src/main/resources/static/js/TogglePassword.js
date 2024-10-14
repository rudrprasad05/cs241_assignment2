const passwordInput = document.getElementById('password');
const togglePassword = document.getElementById('togglePassword');

// Add an event listener to the toggle icon
togglePassword.addEventListener('click', function () {
    // Toggle the type attribute between 'password' and 'text'
    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordInput.setAttribute('type', type);

    // Toggle the eye / eye-slash icon
    this.classList.toggle('bi-eye');
    this.classList.toggle('bi-eye-slash');
});