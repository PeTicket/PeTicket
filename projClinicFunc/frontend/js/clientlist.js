document.getElementById("create-button").addEventListener("click", function() {
    window.location.href = "./manualblockingClient.html"; 
  });
  
  


function logout() {
    localStorage.removeItem('tokenF');
    window.location.href = './index.html';
}