document.addEventListener("DOMContentLoaded", function() {
    const loginTab = document.getElementById("tab1");
    const registerTab = document.getElementById("tab2");
    const loginBox = document.getElementById("login-box");
    const registerBox = document.getElementById("register-box");

    loginTab.addEventListener("click", function() {
        loginBox.style.display = "block";
        registerBox.style.display = "none";
    });

    registerTab.addEventListener("click", function() {
        loginBox.style.display = "none";
        registerBox.style.display = "block";
    });
});