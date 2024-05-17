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
          <p>Pet Name: ${appointment.petName}</p>
          <p>Date: ${appointment.date} </p>
          <p>Horas: ${appointment.time}</p>
          </div>
      
          <div>
            <p>Observation: ${appointment.observations}</p>
          </div>

          <div class="qrcode-icon">
            <i class="fas fa-qrcode"  style="font-size: 40px;"></i>
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
}


function displayConfirmationModal(appointmentId) {
  
  const confirmed = confirm('Are you sure you want to delete this appointment?');
  if (confirmed) {
    
    deleteAppointment(appointmentId);
  }
}

function deleteAppointment(appointmentId) {
  fetch(`http://localhost:8080/api/client/appointment/delete/${appointmentId}`, {
    method: 'DELETE',
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
    window.location.href = './Homepage.html';
}




