let selectedDate;
let selectedTime;
let currentUserInfo;

function logout() {
    localStorage.removeItem('tokenF');
    window.location.href = './index.html';
}




document.addEventListener('DOMContentLoaded', function() {
    createStarsPeriodically();
    createConsultationTimes();

    document.getElementById('useremail').addEventListener('input', function(event) {
        const email = event.target.value;
        if (email) {
            fetchUserByEmail(email);
        }
    });

    document.querySelector('.button-addapp').addEventListener('click', function() {
      addAppointment();
    });
});

function addAppointment() {
    const petId = document.getElementById('pet-select').value;
    const observations = document.querySelector('input[name="observations"]').value;
    const jwtToken = localStorage.getItem('tokenF');

    if (!petId || !selectedDate || !selectedTime) {
        alert('Please fill out all fields.');
        return;
    }


    const appointment = {
        observations: observations,
        date: selectedDate,
        time: selectedTime,
        status:"scheduled"
    };


    fetch(`http://localhost:8082/api/func/appointment/appointment/${petId}/${currentUserInfo.id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken}`
        },
        body: JSON.stringify(appointment)
    })
    .then(response => {
        if (response.ok) {
            alert('Appointment added successfully.');
            document.getElementById('appointment-form').reset();
            document.querySelector('.selected-date-time').textContent = 'No data yet';
            selectedDate = null;
            selectedTime = null;
        } else {
            alert('Failed to add appointment.');
        }
    })
    .catch(error => console.error('Error adding appointment:', error));
}


function fetchPets() {
    const jwtToken = localStorage.getItem('tokenF');
    
    fetch(`http://localhost:8080/api/client/pet/by-user-id`,{
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken}`
        },
    })
      .then(response => response.json())
      .then(pets => populatePetSelect(pets))
      .catch(error => console.error('Error fetching pets:', error));
}
function populatePetSelect(pets) {
    const petSelect = document.getElementById('pet-select');
    pets.forEach(pet => {
        const option = document.createElement('option');
        option.value = pet.id;
        option.textContent = pet.name;
        petSelect.appendChild(option);
    });
}



var btn = document.querySelector(".button-adddateapp");
var modal = document.getElementById("myModal");
var span = document.getElementsByClassName("close")[0];
var dateOfBirthInput = document.getElementById('dateofbirth');
var currentDate = new Date();
var formattedCurrentDate = currentDate.toISOString().split('T')[0];
dateOfBirthInput.setAttribute('min', formattedCurrentDate);

btn.onclick = function() {
    modal.style.display = "flex";
}

span.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

function fetchAllAppointments() {
    const jwtToken = localStorage.getItem('tokenF');
    return fetch('http://localhost:8082/api/func/appointment/appointments', {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken}`
        },
    })
    .then(response => response.json())
    .catch(error => console.error('Error fetching appointments:', error));
}

function createConsultationTimes() {
    fetchAllAppointments().then(appointments => {
        var consultationTimesDiv = document.getElementById('consultationTimes');
        consultationTimesDiv.innerHTML = '';

        var startTime = 10;
        var endTime = 18;

        for (var hour = startTime; hour <= endTime; hour++) {
            createConsultationTimeSlot(hour, '00', consultationTimesDiv, appointments);
            if (hour < endTime) {
                createConsultationTimeSlot(hour, '30', consultationTimesDiv, appointments);
            }
        }

        dateOfBirthInput.addEventListener('change', function() {
            selectedDate = this.value;
            updateAppointmentText();
            markUnavailableTimes(appointments);
        });
    });
}

function createConsultationTimeSlot(hour, minute, parentDiv, appointments) {
    var timeSlot = document.createElement('div');
    timeSlot.classList.add('time-slot');
    timeSlot.textContent = hour.toString().padStart(2, '0') + ':' + minute;
    parentDiv.appendChild(timeSlot);

    timeSlot.addEventListener('click', function() {
        if (!this.classList.contains('unavailable')) {
            selectedTime = this.textContent;
            updateAppointmentText();
            modal.style.display = "none";
        }
    });
}

function markUnavailableTimes(appointments) {
    const slots = document.querySelectorAll('.time-slot, .half-hour-slot');
    slots.forEach(slot => {
        slot.classList.remove('unavailable');
        slot.disabled = false;
    });

    const selectedDateAppointments = appointments.filter(app => app.date === selectedDate);

    selectedDateAppointments.forEach(app => {
        slots.forEach(slot => {
            if (slot.textContent === app.time) {
                slot.classList.add('unavailable');
                slot.disabled = true;
            }
        });
    });
}

function updateAppointmentText() {
    var appointmentText = selectedDate ? selectedDate : 'No date selected';
    appointmentText += selectedTime ? ' at ' + selectedTime : ' and no time selected';
    document.querySelector('.selected-date-time').textContent = appointmentText;
}

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

 
async function fetchUserByEmail(email) {
    const jwtToken = localStorage.getItem("tokenF");
    try {
        const response = await fetch(`http://localhost:8082/api/func/user/${email}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
        });
        if (!response.ok) {
            throw new Error('User not found');
        }
        const user = await response.json();
        fetchedUserId = user.id;
        currentUserInfo=user;
        console.log('User ID fetched:', fetchedUserId);
        
        fetchPetsByUserId(fetchedUserId);
    } catch (error) {
        console.error(error);
        fetchedUserId = null;
    }
}

async function fetchPetsByUserId(userId) {
    const jwtToken = localStorage.getItem("tokenF");
    try {
        const response = await fetch(`http://localhost:8082/api/func/pets/users/${userId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
        });
        if (!response.ok) {
            throw new Error('Pets not found');
        }
        const pets = await response.json();
        console.log('Pets fetched:', pets);
        populatePetSelect(pets);
    } catch (error) {
        console.error(error);
    }
}

function populatePetSelect(pets) {
    const petSelect = document.getElementById('pet-select');
    petSelect.innerHTML = ''; 

    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.textContent = 'Select pet';
    defaultOption.disabled = true;
    defaultOption.selected = true;
    petSelect.appendChild(defaultOption);


    if (pets && pets.length > 0) {
        pets.forEach(pet => {
            const option = document.createElement('option');
            option.value = pet.id;
            option.textContent = pet.name;
            petSelect.appendChild(option);
        });
    }
}