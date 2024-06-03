
let starCount = 0;
let selectedDate;
let selectedTime;
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
  
      fetch(`http://deti-tqs-13.ua.pt:8080/api/client/user/by-email/${email}`, {
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
            fetchPets();
              console.log('User information:', user);
          })
          .catch(error => {
              console.error('Error:', error);
          });
         
      } else {
          console.log('User information not available');
      }
    });  


document.addEventListener('DOMContentLoaded', function() {
    createStarsPeriodically();
    createConsultationTimes();

    document.querySelector('.button-addapp').addEventListener('click', function() {
      addAppointment();
    });
});

function addAppointment() {
    const petId = document.getElementById('pet-select').value;
    const observations = document.querySelector('input[name="observations"]').value;
    const jwtToken = localStorage.getItem('jwtToken');

    if (!petId || !selectedDate || !selectedTime) {
        alert('Please fill out all fields.');
        return;
    }


    const appointment = {
        userId: currentUserInfo.id,
        petId: petId,
        observations: observations,
        date:selectedDate,
        time:selectedTime
    };



    fetch('http://deti-tqs-13.ua.pt:8080/api/client/appointment/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken}`
        },
        body: JSON.stringify(appointment)
    })
    .then(response => {
        if (response.ok) {
            // alert('Appointment added successfully.');
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
    const jwtToken = localStorage.getItem('jwtToken');
    
    const userId = currentUserInfo.id; 
    fetch(`http://deti-tqs-13.ua.pt:8080/api/client/pet/by-user-id`,{
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

function createStar() {
    if (starCount >= 40) {
        return;
    }

    const star = document.createElement('div');
    star.classList.add('star');
    star.style.top = Math.random() * window.innerHeight + 'px';
    star.style.animationDuration = (Math.random() * 10 + 5) + 's';
    document.body.appendChild(star);

    starCount++;

    star.addEventListener('animationend', function() {
        star.remove();
        starCount--;
    });
}

function createStarsPeriodically() {
    setInterval(createStar, 1000);
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
    const jwtToken = localStorage.getItem('jwtToken');
    return fetch('http://deti-tqs-13.ua.pt:8080/api/client/appointment/all', {
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


    function logout() {
        localStorage.removeItem('jwtToken');
        window.location.href = './Homepage.html';
    }

