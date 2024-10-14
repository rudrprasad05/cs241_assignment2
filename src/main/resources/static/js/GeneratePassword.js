// Function to generate a random password
function generateRandomPassword(length) {
    const characters = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()';
    let result = '';

    for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        result += characters.charAt(randomIndex);
    }

    return result;
}

// Get elements from the DOM
const password = document.getElementById('password');
const generateButton = document.getElementById('generatePassword');

// Add event listener to the button
generateButton.addEventListener('click', function() {
    // Generate a 12-character random password
    password.value = generateRandomPassword(12);
});

