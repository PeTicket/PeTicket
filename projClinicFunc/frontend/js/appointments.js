document.getElementById("create-button").addEventListener("click", function() {
  window.location.href = "./manualblocking.html"; 
});





  function logout() {
    localStorage.removeItem('tokenF');
    window.location.href = './index.html';
}
