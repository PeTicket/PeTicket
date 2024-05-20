
let previousContentId = '';


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
    try {
        const response = await fetch('http://localhost:8081/api/vet/appointment/all');
        const appointments = await response.json();
        const todayAppointments = filterTodayAppointments(appointments);
        console.log(appointments);
        populateTodayAppointments(todayAppointments);
    } catch (error) {
        console.error('Error fetching appointments:', error);
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
                <i onclick="changeContentColumn('content-appointment')" onclick="viewAppointmentDetails(${encodeURIComponent(JSON.stringify(appointment))})" class="fas fa-eye"></i>
            </td>
            <td>${appointment.date}</td>
            <td>${appointment.time}</td>
            <td>${appointment.client}</td>
            <td>${appointment.pet}</td>
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
        case 'waiting':
            return 'state-waiting';
        default:
            return '';
    }
}


function viewAppointmentDetails(appointmentData) {
    const appointment = JSON.parse(decodeURIComponent(appointmentData));

    document.getElementById('name-client').textContent = appointment.client;
    document.getElementById('name-pet').textContent = appointment.pet;
    document.getElementById('type-pet').textContent = appointment.petType;
    document.getElementById('breed-pet').textContent = appointment.breed;
    document.getElementById('color-pet').textContent = appointment.color;
    document.getElementById('age-pet').textContent = appointment.age;
    document.getElementById('weight-pet').value = appointment.weight;
    document.getElementById('height-pet').value = appointment.height;
    document.getElementById('bloodtype-pet').value = appointment.bloodType;
    document.getElementById('medical-info-pet').value = appointment.medicalInfo;
    document.querySelector('.app-occurence p').textContent = appointment.occurrence;

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
