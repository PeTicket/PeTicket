let currentUserInfo =null ;


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

    fetch(`http://pet-service:8080/api/client/user/by-email/${email}`, {
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
            currentUserInfo = user;
            console.log('User information:', user);
            fetchAppointments(user.id);
        })
        .catch(error => {
            console.error('Error:', error);
        });
       
    } else {
        console.log('User information not available');
    }
  });  


  function fetchAppointments(userId) {
    const jwtToken = localStorage.getItem('jwtToken');

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${jwtToken}`
    };

    fetch(`http://pet-service:8080/api/client/appointment/by-user-id`, {
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
        displayAppointments(appointments);
    })
    .catch(error => {
        console.error('Error:', error);
    });
}


function displayAppointments(appointments) {
  const now = new Date();
  const futureAppointments = appointments.filter(appointment => new Date(appointment.date) > now);
  const pastAppointments = appointments.filter(appointment => new Date(appointment.date) <= now);

  futureAppointments.sort((a, b) => new Date(a.date) - new Date(b.date));
  pastAppointments.sort((a, b) => new Date(a.date) - new Date(b.date));

  
  renderAppointments('future-appointments', futureAppointments);
  renderAppointments('past-appointments', pastAppointments);
}

function renderAppointments(containerId, appointments) {
  const container = document.getElementById(containerId);
  container.innerHTML = '';

  appointments.forEach(appointment => {
    const appointmentHTML = `
      <div class="appointment-div">
        <div class="appointment-container">
          <div>
          <p>Pet Name: ${appointment.pet.name}</p>
          <p>Date: ${appointment.date} </p>
          <p>Horas: ${appointment.time}</p>
          </div>
      
          <div>
            <p>Observation: ${appointment.observations}</p>
            <p>Prescriptions: ${appointment.prescription ? appointment.prescription : '&nbsp;'}</p>
          </div>

          <div class="qrcode-icon">
            <i class="fas fa-qrcode"  style="font-size: 40px;" data-id="${appointment.id}"></i>
            <span class="icon-delete"><i class="fas fa-trash-alt" data-id="${appointment.id}"></i></span>
          </div>
          
        </div>
      </div>
    `;
    container.innerHTML += appointmentHTML;
  });

  const deleteIcons = document.querySelectorAll('.icon-delete');
  deleteIcons.forEach(icon => {
    icon.addEventListener('click', () => {
      const appointmentId = icon.querySelector('i').getAttribute('data-id');
      displayConfirmationModal(appointmentId);
    });
  });


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

function displayConfirmationModal(appointmentId) {

  const jwtToken = localStorage.getItem('jwtToken');
  
  const confirmed = confirm('Are you sure you want to delete this appointment?');
  if (confirmed) {
    
    deleteAppointment(appointmentId);
  }
};


function deleteAppointment(appointmentId) {

  const jwtToken = localStorage.getItem('jwtToken');

  const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${jwtToken}`
  };

  
  fetch(`http://pet-service:8080/api/client/appointment/delete/${appointmentId}`, {
    method: 'DELETE',
    headers: headers
  })
  .then(response => {
    if (response.ok) {
      const appointmentElement = document.querySelector(`.icon-delete i[data-id="${appointmentId}"]`).closest('.appointment-div');
      appointmentElement.remove();
    } else {
      // Handle error response
      console.error('Failed to delete appointment');
    }
  })
  .catch(error => {
    console.error('Error deleting appointment:', error);
  });
}





function handleTabChange() {
  const futureTab = document.getElementById('tab1');
  const pastTab = document.getElementById('tab2');

  if (futureTab.checked) {
    document.getElementById('future-appointments').style.display = 'block';
    document.getElementById('past-appointments').style.display = 'none';
  } else if (pastTab.checked) {
    document.getElementById('future-appointments').style.display = 'none';
    document.getElementById('past-appointments').style.display = 'block';
  }
}

document.addEventListener('DOMContentLoaded', function() {
  document.getElementById('tab1').addEventListener('change', handleTabChange);
  document.getElementById('tab2').addEventListener('change', handleTabChange);

  handleTabChange(); 
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
    localStorage.removeItem('jwtToken');
    window.location.href = './Homepage.html';
}




