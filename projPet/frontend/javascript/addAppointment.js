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


  var btn = document.querySelector(".button-adddateapp");
var modal = document.getElementById("myModal");


var span = document.getElementsByClassName("close")[0];


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

var dateOfBirthInput = document.getElementById('dateofbirth');


var currentDate = new Date();


var formattedCurrentDate = currentDate.toISOString().split('T')[0];


dateOfBirthInput.setAttribute('min', formattedCurrentDate);


var selectedDate;
var selectedTime;

function createConsultationTimes() {
  var consultationTimesDiv = document.getElementById('consultationTimes');
  consultationTimesDiv.innerHTML = ''; 

  var startTime = 10; 
  var endTime = 18; 

  for (var hour = startTime; hour <= endTime; hour++) {
    
    var timeSlot = document.createElement('div');
    timeSlot.classList.add('time-slot');
    timeSlot.textContent = hour.toString().padStart(2, '0') + ':00'; // Adiciona a hora ao retângulo
    consultationTimesDiv.appendChild(timeSlot);

    
    timeSlot.addEventListener('click', function() {
      //
      selectedTime = this.textContent;

      updateAppointmentText();
    });

    if (hour < endTime) {
      var halfHourSlot = document.createElement('div');
      halfHourSlot.classList.add('half-hour-slot');
      halfHourSlot.textContent = hour.toString().padStart(2, '0') + ':30'; // Adiciona a hora ao retângulo
      consultationTimesDiv.appendChild(halfHourSlot);

      halfHourSlot.addEventListener('click', function() {
        selectedTime = this.textContent;
        

        updateAppointmentText();
        modal.style.display = "none";
      });
    }
  }
}

function updateAppointmentText() {
  var selectedDateTime = document.querySelector('.selected-date-time');
  if (selectedDate && selectedTime) {
    selectedDateTime.textContent = "Selected date and time: " + selectedDate + " " + selectedTime;
  } else {
    selectedDateTime.textContent = "No data yet";
  }
}

createConsultationTimes();

updateAppointmentText();

var dateOfBirthInput = document.getElementById('dateofbirth');

dateOfBirthInput.addEventListener('change', function() {
 
  selectedDate = this.value;

 
  updateAppointmentText();
});