
let previousContentId = '';
let appointments = [];

document.addEventListener('DOMContentLoaded', () => {
    const logoutButton = document.getElementById('logout-button');

    logoutButton.addEventListener('click', () => {
       
        window.location.href = './index.html';
        
      
        localStorage.removeItem('token');
    });
});

document.addEventListener('DOMContentLoaded', () => {
    fetchAppointments();

    document.querySelectorAll('.menu-options > div').forEach(item => {
        item.addEventListener('click', (event) => {
            const option = event.currentTarget.getAttribute('onclick').match(/'(\w+)'/)[1];
            changeContent(event, option);
        });
    });
});

async function fetchAppointments() {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch('http://localhost:8081/api/vet/appointment/all',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`

            },
        });
        appointments = await response.json();
        const todayAppointments = filterTodayAppointments(appointments);
        console.log(appointments);
        populateTodayAppointments(todayAppointments);
        populateAllAppointments(appointments);
        const afterTodayAppointments = filterAppointmentsAfterToday(appointments);
        const todayAndOldDoneAppointments = filterTodayAndOldDoneAppointments(appointments);
        populateUpcomingAppointments(afterTodayAppointments);
        populateLastAppointments(todayAndOldDoneAppointments);
    } catch (error) {
        console.error('Error fetching appointments:', error);
    }
}

function filterAppointmentsAfterToday(appointments) {
    const today = new Date().toISOString().split('T')[0];
    return appointments.filter(appointment => new Date(appointment.date) > new Date(today));
}

function filterTodayAndOldDoneAppointments(appointments) {
    const today = new Date().toISOString().split('T')[0];
    return appointments.filter(appointment => {
        const appointmentDate = new Date(appointment.date);
        return (appointmentDate.toISOString().split('T')[0] === today && appointment.status.toLowerCase() === 'done') || appointmentDate < new Date(today);
    });
}

function findAppointmentById(appointmentId) {
    return appointments.find(appointment => appointment.id === appointmentId);
}


function handleEyeClick(appointmentId) {
    const appointment = findAppointmentById(appointmentId); 
    if (appointment) {
        changeContentColumn('content-appointment');
        viewAppointmentDetails(appointment);
    } else {
        console.error('Appointment not found with ID:', appointmentId);
    }
}
function filterTodayAppointments(appointments) {
    const today = new Date().toISOString().split('T')[0]; 
    return appointments.filter(appointment => appointment.date === today);
}

function populateTodayAppointments(appointments) {
    const tbody = document.querySelector('#content-today tbody');
    tbody.innerHTML = ''; 

    appointments.forEach(appointment => {
        const stateClass = getStateClass(appointment.status);
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="eye-app">
            <i onclick="handleEyeClick('${appointment.id}')" class="fas fa-eye"></i>
            </td>
            <td>${appointment.date}</td>
            <td>${appointment.time}</td>
            <td>${appointment.user.firstName}</td>
            <td>${appointment.pet.name}</td>
            <td class="tdtd"><div class="state-app ${stateClass}"><p>${appointment.status}</p></div></td>
        `;
        tbody.appendChild(tr);
    });
}


function populateAllAppointments(appointments) {
    const tbody = document.querySelector('#content-all tbody');
    tbody.innerHTML = ''; 

    appointments.forEach(appointment => {
        const stateClass = getStateClass(appointment.status);
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="eye-app">
            <i onclick="handleEyeClick('${appointment.id}')" class="fas fa-eye"></i>
            </td>
            <td>${appointment.date}</td>
            <td>${appointment.time}</td>
            <td>${appointment.user.firstName}</td>
            <td>${appointment.pet.name}</td>
            <td class="tdtd"><div class="state-app ${stateClass}"><p>${appointment.status}</p></div></td>
        `;
        tbody.appendChild(tr);
    });
}

function populateLastAppointments(lastAppointments) {
    const tbody = document.querySelector('#content-last tbody');
    tbody.innerHTML = ''; 

    lastAppointments.forEach(appointment => {
        const stateClass = getStateClass(appointment.status);
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="eye-app">
                <i onclick="handleEyeClick(${appointment.id})" class="fas fa-eye"></i>
            </td>
            <td>${appointment.date}</td>
            <td>${appointment.time}</td>
            <td>${appointment.user.firstName}</td>
            <td>${appointment.pet.name}</td>
            <td class="tdtd"><div class="state-app ${stateClass}"><p>${appointment.status}</p></div></td>
        `;
        tbody.appendChild(tr);
    });
}

function populateUpcomingAppointments(upcomingAppointments) {
    const tbody = document.querySelector('#content-upcoming tbody');
    tbody.innerHTML = ''; 

    upcomingAppointments.forEach(appointment => {
        const stateClass = getStateClass(appointment.status);
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="eye-app">
            <i onclick="handleEyeClick('${appointment.id}')" class="fas fa-eye"></i>
            </td>
            <td>${appointment.date}</td>
            <td>${appointment.time}</td>
            <td>${appointment.user.firstName}</td>
            <td>${appointment.pet.name}</td>
            <td class="tdtd"><div class="state-app ${stateClass}"><p>${appointment.status}</p></div></td>
        `;
        tbody.appendChild(tr);
    });
}

function getStateClass(state) {
    if (typeof state === 'undefined') {
        return '';
    }
    
    if (state === null) {
        return 'null'; 
    }
    
    switch (state.toLowerCase()) {
        case 'in progress':
            return 'state-in-progress';
        case 'on hold':
            return 'state-on-hold';
        case 'done':
            return 'state-done';
        default:
            return '';
    }
}




function viewAppointmentDetails(appointmentData) {
    const appointment =appointmentData;
    document.getElementById('app-id').textContent = appointment.id;
    document.getElementById('name-client').textContent = appointment.user.firstName;
    document.getElementById('email-client').textContent = appointment.user.email;
    document.getElementById('phone-client').textContent = appointment.user.phone | "N/A";
    document.getElementById('name-pet').textContent = appointment.pet.name;
    document.getElementById('type-pet').textContent = appointment.pet.type;
    document.getElementById('breed-pet').textContent = appointment.pet.breed;
    document.getElementById('color-pet').textContent = appointment.pet.color;
    document.getElementById('age-pet').textContent = appointment.pet.age;
    document.getElementById('weight-pet').value = appointment.weight | 0;
    document.getElementById('height-pet').value = appointment.height | 0
    document.getElementById('bloodtype-pet').value = appointment.bloodType | "BloodType";
    document.getElementById('medical-info-pet').value = appointment.pet.medicalInfo;
    document.querySelector('.app-occurence p').textContent = appointment.observations;

    const stateDetailDiv = document.getElementById('state-detail');
    stateDetailDiv.className = `state-app ${getStateClass(appointment.state)}`;
    document.getElementById('state-detail-text').textContent = appointment.state;

    changeContentColumn('content-appointment');
}



function updateTime() {
    const now = new Date();
    const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const dayOfWeek = daysOfWeek[now.getDay()];

    const hours = now.getHours().toString().padStart(2, '0');
    const minutes = now.getMinutes().toString().padStart(2, '0');
    const seconds = now.getSeconds().toString().padStart(2, '0');
    const timeString = `${hours}:${minutes}:${seconds}`;

    const day = now.getDate().toString().padStart(2, '0');
    const month = (now.getMonth() + 1).toString().padStart(2, '0'); 
    const year = now.getFullYear();
    const dateString = `${day}/${month}/${year}`;

    const dateTimeString = `${dayOfWeek}, ${dateString} ${timeString}`; 
    document.getElementById('date-time').textContent = dateTimeString;
}



setInterval(updateTime, 1000);


updateTime();


function changeContent(event, option) {
   
    event.preventDefault();
    
    const selectedOptions = document.querySelectorAll('.menu-options > div');
    selectedOptions.forEach(item => item.classList.remove('selected'));

    event.currentTarget.classList.add('selected');

    const allContents = document.querySelectorAll('.content-column > div');
    allContents.forEach(content => content.style.display = 'none');

    document.getElementById(`content-${option}`).style.display = 'block';

    previousContentId = `content-${option}`;
}


function changeContentColumn(option) {
    const allContents = document.querySelectorAll('.content-column > div');
    allContents.forEach(content => content.style.display = 'none');

    document.getElementById(option).style.display = 'block';
}


function goBack() {
    
    if (!previousContentId) return;

 
    const currentContent = document.getElementById('content-appointment');
    currentContent.style.display = 'none';

    
    const previousContent = document.getElementById(previousContentId);
    previousContent.style.display = 'block';

    
    previousContentId = 'content-appointment';
}


document.addEventListener('DOMContentLoaded', (event) => {
    const addPrescriptionBtn = document.getElementById('add-prescription-btn');
    const petMedicalInfoDiv = document.getElementById('box2');

    addPrescriptionBtn.addEventListener('click', () => {
        const newPrescriptionDiv = document.createElement('div');
        newPrescriptionDiv.className = 'user-box3';
        newPrescriptionDiv.innerHTML = `
            <input type="text" name="" required="">
            <label>New Prescription</label>
            <button type="button" class="remove-prescription-btn">Remove prescription</button>
        `;
        
        const removeBtn = newPrescriptionDiv.querySelector('.remove-prescription-btn');
        removeBtn.addEventListener('click', () => {
            newPrescriptionDiv.remove();
        });

        petMedicalInfoDiv.parentNode.insertBefore(newPrescriptionDiv, petMedicalInfoDiv.nextSibling);
    });
});


document.addEventListener('DOMContentLoaded', (event) => {
    const doneButton = document.querySelector('.button-add-info:last-child');
    doneButton.addEventListener('click', () => {
        const appointmentId = document.getElementById('app-id').textContent;;
        const agePet = document.getElementById('age-pet').textContent;
        const weightPet = document.getElementById('weight-pet').value;
        const heightPet = document.getElementById('height-pet').value;
        const bloodTypePet = document.getElementById('bloodtype-pet').value;
        const observations= document.getElementById('medical-observations').value;
        const medicalInfoPet= document.getElementById('medical-info-pet').value;
        const occurence = document.querySelector('.app-occurence p').textContent;


    
        const prescriptions = [];
        document.querySelectorAll('.user-box3 input').forEach(input => {
            const prescription = input.value.trim();
            if (prescription) {
                prescriptions.push(prescription);
            }
        });

       
        const appointmentData = {
            id: appointmentId,
            weightPet: weightPet,
            heightPet: heightPet,
            bloodTypePet: bloodTypePet,
            occurence:occurence,
            observations:observations,
            prescriptions: prescriptions
        };



      
        updateAppointment(appointmentData);
    });
});

async function updateAppointment(appointmentData) {
    console.log(appointmentData);
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`http://localhost:8081/api/vet/appointment/update/${appointmentData.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(appointmentData)
        });

        if (response.ok) {
            const updatedAppointment = await response.json();
            console.log('Appointment updated:', updatedAppointment);
        } else {
            console.error('Failed to update appointment');
        }
    } catch (error) {
        console.error('Error updating appointment:', error);
    }
}