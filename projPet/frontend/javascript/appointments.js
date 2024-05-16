
document.addEventListener("DOMContentLoaded", function() {

  const userInfo = getUserInfoFromJWT();
 
    
   
    if (userInfo) {
      const email = userInfo.sub;
      const jwtToken = localStorage.getItem('jwtToken');


      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${jwtToken}`
    };

    fetch(`http://localhost:8080/api/client/user/by-email/${email}`, {
            method: 'GET',
            headers: headers
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch user information');
            }
        })
        .then(user => {
            console.log('User information:', user);
        })
        .catch(error => {
            console.error('Error:', error);
        });
       
    } else {
        console.log('User information not available');
    }
  });  




document.addEventListener("DOMContentLoaded", function() {
    const dropdownItems = document.querySelectorAll(".dropdown-item");
  
    dropdownItems.forEach(item => {
      item.addEventListener("click", function(event) {
        event.stopPropagation(); 
        const dropdown = item.querySelector(".dropdown");
        dropdown.classList.toggle("show");
      });
    });
  
    window.addEventListener("click", function(event) {
      dropdownItems.forEach(item => {
        const dropdown = item.querySelector(".dropdown");
        if (!item.contains(event.target) && !dropdown.contains(event.target)) {
          dropdown.classList.remove("show");
        }
      });
    });
  });
  


  function logout() {
    window.location.href = './Homepage.html';
}