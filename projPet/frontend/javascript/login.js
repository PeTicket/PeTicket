
function loginUser() {
    event.preventDefault();
    const username = document.getElementById("login-email").value;
    const password = document.getElementById("login-password").value;

    const user = {
        email: username,
        password: password
    };

    fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    })
    .then(response => {
        if (response.ok) {
           
            return response.text();
        } else {
            throw new Error('Login failed');
        }
    })
    .then(data => {
       
        localStorage.setItem('jwtToken', data);
        console.log('Login successful');
        alert('Login successful');
        setTimeout(() => {
            window.location.href = './home.html';
        }, 2000);
    })
    .catch(error => {
        console.error('Error:', error);
        localStorage.removeItem('jwtToken');
    });
}



function registerUser() {

    event.preventDefault();
    const firstname = document.getElementById("register-firstname").value;
    const email = document.getElementById("register-email").value;
    const password = document.getElementById("register-password").value;
    const confirmPassword = document.getElementById("confirm-password").value;

    if (password !== confirmPassword) {
        
        console.error('Passwords do not match');
        return;
    }

    const user = {
        firstName: firstname,
        email: email,
        password: password
    };

    fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    })
    .then(response => {
        if (response.ok) {
            
            console.log('Registration successful');
        } else {
            
            console.error('Registration failed');
        }
    })
    .catch(error => console.error('Error:', error));
}



