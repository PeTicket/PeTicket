
let starCount = 0;
let selectedDate;
let selectedTime;

document.addEventListener('DOMContentLoaded', function() {
    fetchPets();
    createStarsPeriodically();
    createConsultationTimes();

    document.querySelector('.button-addapp').addEventListener('click', function() {
      addAppointment();
    });
});

function addAppointment() {
    const petId = document.getElementById('pet-select').value;
    const observations = document.querySelector('input[name="observations"]').value;

    if (!petId || !selectedDate || !selectedTime) {
        alert('Please fill out all fields.');
        return;
    }

    const appointment = {
        petId: petId,
        date: selectedDate,
        time: selectedTime,
        observations: observations
    };

    fetch('http://localhost:3306/api/appointments/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
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
    fetch('http://localhost:3306/api/client/pet/by-user-id?userId=1')
        .then(response => response.json())
        .then(data => populatePetSelect(data))
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

function createConsultationTimes() {
    var consultationTimesDiv = document.getElementById('consultationTimes');
    consultationTimesDiv.innerHTML = '';

    var startTime = 10;
    var endTime = 18;

    for (var hour = startTime; hour <= endTime; hour++) {
        var timeSlot = document.createElement('div');
        timeSlot.classList.add('time-slot');
        timeSlot.textContent = hour.toString().padStart(2, '0') + ':00';
        consultationTimesDiv.appendChild(timeSlot);

        timeSlot.addEventListener('click', function() {
            selectedTime = this.textContent;
            updateAppointmentText();
            modal.style.display = "none";
        });

        if (hour < endTime) {
            var halfHourSlot = document.createElement('div');
            halfHourSlot.classList.add('half-hour-slot');
            halfHourSlot.textContent = hour.toString().padStart(2, '0') + ':30';
            consultationTimesDiv.appendChild(halfHourSlot);

            halfHourSlot.addEventListener('click', function() {
                selectedTime = this.textContent;
                updateAppointmentText();
                modal.style.display = "none";
            });
        }
    }

    dateOfBirthInput.addEventListener('change', function() {
        selectedDate = this.value;
        updateAppointmentText();
    });
}

function updateAppointmentText() {
    var appointmentText = selectedDate ? selectedDate : 'No date selected';
    appointmentText += selectedTime ? ' at ' + selectedTime : ' and no time selected';
    document.querySelector('.selected-date-time').textContent = appointmentText;
}
