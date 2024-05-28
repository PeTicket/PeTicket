document.getElementById('loginButton').addEventListener('click', async function() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const response = await fetch("http://localhost:8082/api/auth/login", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            
        },
        body: JSON.stringify({ email: email, password: password })
    });

    if (response.ok) {
        const token = await response.text();
        localStorage.setItem('tokenF', token);
        Toastify({
            text: "Login successful",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "green",
            close: true
        }).showToast();
        window.location.href = "./appointments.html";
    } else {
        Toastify({
            text: "Login failed",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "red",
            close: true
        }).showToast();
    }
});


function logout() {
    localStorage.removeItem('jwtToken');
    window.location.href = './index.html';
}
