document.getElementById("create-button").addEventListener("click", function() {
    window.location.href = "./manualblockingClient.html"; 
  });


  document.getElementById("create-button2").addEventListener("click", function() {
    window.location.href = "./manualpet.html"; 
  });
  
  
  
  


function logout() {
    localStorage.removeItem('tokenF');
    window.location.href = './index.html';
}