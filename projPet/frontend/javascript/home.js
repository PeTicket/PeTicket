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
        
        const now = new Date();

     
        const upcomingAppointments = appointments
            .filter(appointment => new Date(appointment.date) > now)
            .sort((a, b) => new Date(a.date) - new Date(b.date));

            const appointmentInfo = document.getElementById('appointment-info');

            if (upcomingAppointments.length > 0) {
              const nextAppointment = upcomingAppointments[0];
              console.log('Next appointment:', nextAppointment);

              const appointmentDate = nextAppointment.date;
              appointmentInfo.innerHTML = `
              <div class="qrcode-icon">
                  <p>Next appointment date: ${appointmentDate}</p>
                  <i class="fas fa-qrcode"  style="font-size: 40px;" data-id="${nextAppointment.id}"></i>
                </div>
              `;

              const qrIcons = document.querySelectorAll('.qrcode-icon i');
              qrIcons.forEach(icon => {
                icon.addEventListener('click', () => {
                  const appointmentId = icon.getAttribute('data-id');
                  const appointment = appointments.find(app => app.id === appointmentId);
                  if (appointment) {
                    displayQRModal(appointment);
                  }
                });
              });
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




function displayQRModal(appointment) {
    const modal = document.getElementById("qrModal");
    const span = document.getElementsByClassName("close2")[0];
    const qrcodeDiv = document.getElementById("qrcode");

    const qrCodeBase64 = appointment.qrCode;

   const dataUrl = `data:image/png;base64,${qrCodeBase64}`;

   const imgElement = document.createElement('img');
  imgElement.src = dataUrl;
  imgElement.style.display = 'block';
  imgElement.style.margin = 'auto auto'; 

  if (window.innerWidth < 1000) {
    imgElement.style.width = '250px';
    imgElement.style.height = '250px';
} else {
    imgElement.style.width = '400px';
    imgElement.style.height = '400px';
}
  imgElement.alt = 'QR Code Image';

  qrcodeDiv.innerHTML = '';

  qrcodeDiv.appendChild(imgElement);
  const petName = appointment.pet.name;
  const pElement = document.createElement('p');
  pElement.textContent = `QR code for appointment with ${petName}`;
  pElement.style.textAlign = 'center';
  pElement.style.marginTop = '10px';

  qrcodeDiv.appendChild(pElement);

    modal.style.display = "block";

    span.onclick = function() {
      modal.style.display = "none";
    }

    window.onclick = function(event) {
      if (event.target == modal) {
        modal.style.display = "none";
      }
    }
  }