function handleRegistration(event) {
    event.preventDefault();

    // Get user input
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // Create an object with user information
    const user = {
        username: username,
        email: email,
        password: password,
    };

    fetch('http://localhost:8081/api/v1/users/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    }).then(response => {
        return response.json().then(data => {
            if (!response.ok) {
                // The backend returned an error, so we throw it
                throw new Error(data.message || 'Registration failed');
            }
            return data;
        });
    }).then(data => {
        // Registration was successful
        localStorage.setItem("connectedUser", JSON.stringify(data));
        window.location.href = "index.html";
    }).catch(error => {
        // Handle the error and display the message
        console.error('POST request error:', error);
        const errorMessageElement = document.getElementById("error-message");
        errorMessageElement.textContent = error.message;
        errorMessageElement.style.display = "block"; // Make the error message visible
    });
}

// Attach the handleRegistration function to the form's submit event
const registrationForm = document.getElementById("registrationForm");
registrationForm.addEventListener("submit", handleRegistration);
