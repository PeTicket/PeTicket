let appointments = []; // Define the appointments array globally

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
      appointments = data; // Update the global appointments array
      appointments.sort((a, b) => {
          const dateComparison = new Date(b.date) - new Date(a.date);
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
  renderAppointments(filteredAppointments); // Render filtered appointments
}

function renderAppointments(appointmentsData) {
  const appointmentsContainer = document.querySelector('.appointments-container');
  appointmentsContainer.innerHTML = ''; // Clear the container
  
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
    statusButton.textContent = appointment.status === 'scheduled' ? 'Mark as Done' : 'Undo';
    statusButton.addEventListener('click', () => {
      switch (appointment.status) {
        case 'scheduled':
          appointment.status = 'in progress';
          statusButton.textContent = 'Mark as Done';
          statusButton.classList.remove('scheduled');
          statusButton.classList.add('in-progress');
          break;
        case 'in progress':
          appointment.status = 'done';
          statusButton.textContent = 'Undo';
          statusButton.classList.remove('in-progress');
          statusButton.classList.add('done');
          break;
        case 'done':
          appointment.status = 'scheduled';
          statusButton.textContent = 'Start';
          statusButton.classList.remove('done');
          statusButton.classList.add('scheduled');
          break;
        default:
          break;
      }
    });

    if (appointment.status === 'scheduled') {
      statusButton.classList.add('scheduled');
    } else if (appointment.status === 'in progress') {
      statusButton.classList.add('in-progress');
    } else {
      statusButton.classList.add('done');
    }
    
    appointmentDiv.appendChild(statusButton);
    appointmentsContainer.appendChild(appointmentDiv);
  });
}