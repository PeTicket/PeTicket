
  function logout() {
    localStorage.removeItem('jwtToken');
    window.location.href = './index.html';
}
