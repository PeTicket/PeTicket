function getUserInfoFromJWT() {
  
  const jwtToken = localStorage.getItem('jwtToken');
  if (jwtToken) {
      try {
          const userInfo = JSON.parse(atob(jwtToken.split('.')[1]));
          return userInfo;
      } catch (error) {
          console.error('Error decoding JWT token:', error);
          return null;
      }
  } else {
      return null;
  }
}

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





  // add pet button to page

  document.addEventListener("DOMContentLoaded", function() {
    
    document.getElementById("addPetButton").addEventListener("click", function() {

      window.location.href = "./addpet.html";
    });

    document.getElementById("seeprofile").addEventListener("click", function() {

      window.location.href = "./profile.html";
    });
  });


  function fetchAppointments(userId) {
    const jwtToken = localStorage.getItem('jwtToken');

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${jwtToken}`
    };

    fetch(`http://localhost:8080/api/client/appointment/by-user-id`, {
        method: 'GET',
        headers: headers
    })
    .then(response => {
        if (response.ok) {
            return response.text().then(text => {
                if (text) {
                    try {
                        return JSON.parse(text);
                    } catch (e) {
                        throw new Error('Failed to parse JSON response');
                    }
                } else {
                    return [];
                }
            });
        } else {
            throw new Error('Failed to fetch appointments');
        }
    })
    .then(appointments => {
        console.log('Appointments:', appointments);
        
        // Get the current date and time
        const now = new Date();

        // Filter and sort the appointments to find the next one
        const upcomingAppointments = appointments
            .filter(appointment => new Date(appointment.date) > now)
            .sort((a, b) => new Date(a.date) - new Date(b.date));

            const appointmentInfo = document.getElementById('appointment-info');

            if (upcomingAppointments.length > 0) {
              const nextAppointment = upcomingAppointments[0];
              console.log('Next appointment:', nextAppointment);

              const appointmentDate = new Date(nextAppointment.date);
              appointmentInfo.innerHTML = `
                  <p>Next appointment date: ${appointmentDate}</p>
                  <i class="fas fa-qrcode"  style="font-size: 40px;"></i>
              `;
          } else {
              console.log('No upcoming appointments');
              appointmentInfo.innerHTML = '<p>No upcoming appointments</p>';
          }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

document.addEventListener("DOMContentLoaded", function() {
  fetchAppointments();
});





  function logout() {
    localStorage.removeItem('jwtToken');
    window.location.href = './Homepage.html';
}
