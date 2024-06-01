let appointments = []; 

document.getElementById("create-button").addEventListener("click", function() {
  window.location.href = "./manualblocking.html"; 
});

function logout() {
  localStorage.removeItem('tokenF');
  window.location.href = './index.html';
}

document.addEventListener('DOMContentLoaded', () => {
  fetchAllAppointments();
});

function fetchAllAppointments() {
  const jwtToken = localStorage.getItem('tokenF');
  return fetch('http://localhost:8082/api/func/appointment/appointments', {
      headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${jwtToken}`
      },
  })
  .then(response => response.json())
  .then(data => {
      appointments = data; 
      appointments.sort((a, b) => {
          const dateComparison = new Date(a.date) - new Date(b.date);
          if (dateComparison !== 0) {
              return dateComparison; 
          } else {
              return a.time.localeCompare(b.time);
          }
      });

      console.log(appointments);
      renderAppointments(appointments); // Render appointments after fetching
  })
  .catch(error => console.error('Error fetching appointments:', error));
}

function toggleAppointments() {
  const button = document.getElementById('toggleButton');
  if (button.textContent === 'Show Today\'s Appointments') {
    button.textContent = 'Show All Appointments';
    showTodayAppointments();
  } else {
    button.textContent = 'Show Today\'s Appointments';
    fetchAllAppointments();
  }
}

function showTodayAppointments() {
  const today = new Date().toISOString().split('T')[0]; 
  const filteredAppointments = appointments.filter(appointment => appointment.date === today);
  filteredAppointments.sort((a, b) => a.time.localeCompare(b.time));
  renderAppointments(filteredAppointments);
}

function renderAppointments(appointmentsData) {
  const appointmentsContainer = document.querySelector('.appointments-container');
  appointmentsContainer.innerHTML = ''; 
  
  appointmentsData.forEach(appointment => {
    const appointmentDiv = document.createElement('div');
    appointmentDiv.classList.add('appointment');
    appointmentDiv.innerHTML = `
      <p>Date: ${appointment.date}</p>
      <p>Time: ${appointment.time}</p>
      <p>Client: ${appointment.client}</p>
      <p>Email: ${appointment.email}</p>
    `;
    
    const statusButton = document.createElement('button');
    statusButton.textContent = getStatusButtonText(appointment.status);
    statusButton.addEventListener('click', () => {
      handleStatusChange(appointment, statusButton);
    });

    setStatusButtonClass(statusButton, appointment.status);
    
    appointmentDiv.appendChild(statusButton);
    appointmentsContainer.appendChild(appointmentDiv);
  });
}

function getStatusButtonText(status) {
  switch(status) {
    case 'scheduled':
      return 'Scheduled';
    case 'on_hold':
      return 'On hold';
    case 'in_progress':
      return 'In progress';
    case 'Done':
      return 'Done';
    default:
      return '';
  }
}

function setStatusButtonClass(button, status) {
  button.className = '';
  switch(status) {
    case 'scheduled':
      button.classList.add('scheduled');
      break;
    case 'on_hold':
      button.classList.add('on_hold');
      break;
    case 'in_progress':
      button.classList.add('in_progress');
      break;
    case 'Done':
      button.classList.add('Done');
      break;
  }
}

function handleStatusChange(appointment, button) {
  let confirmMessage;
  let newStatus;
  let executeEndpoint = false;

  switch(appointment.status) {
    case 'scheduled':
      confirmMessage = 'Are you sure you want to mark this appointment as On Hold?';
      newStatus = 'on_hold';
      executeEndpoint = true;
      break;
    case 'on_hold':
      confirmMessage = 'Are you sure you want to mark this appointment as In Progress?';
      newStatus = 'in_progress';
      break;
    case 'in_progress':
      confirmMessage = 'Are you sure you want to mark this appointment as Done?';
      newStatus = 'Done';
      break;
    case 'Done':
      confirmMessage = 'Are you sure you want to undo the appointment to Scheduled?';
      newStatus = 'scheduled';
      break;
    default:
      return;
  }

  if (confirm(confirmMessage)) {
    if (executeEndpoint) {
      updateQrCode(appointment.id)
        .then(() => {
          appointment.status = newStatus;
          button.textContent = getStatusButtonText(newStatus);
          setStatusButtonClass(button, newStatus);
        })
        .catch(error => console.error('Error updating QR code:', error));
    } else {
      appointment.status = newStatus;
      button.textContent = getStatusButtonText(newStatus);
      setStatusButtonClass(button, newStatus);
    }
  }
}

function updateQrCode(appointmentId) {
  const jwtToken = localStorage.getItem('tokenF');
  return fetch(`http://localhost:8082/api/func/appointment/appointmentQrCode/${appointmentId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${jwtToken}`
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    console.log("on_hold");
    return response.json();
  });
}
