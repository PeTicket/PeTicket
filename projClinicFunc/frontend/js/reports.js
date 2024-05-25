
function logout() {
    localStorage.removeItem('tokenF');
    window.location.href = './index.html';
}