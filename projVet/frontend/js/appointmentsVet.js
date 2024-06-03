
let previousContentId = 'content-today';
let appointments = [];
let currentAppointmentDetails = null;
let infouser = null;


function getUserInfoFromJWT() {
  
    const jwtToken = localStorage.getItem('token');
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
        const jwtToken = localStorage.getItem('token');
  
        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken}`
        };
  
        fetch(`http://localhost:8081/api/vet/users/${email}`, {
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
            console.log('User information:', user);
            infouser = user;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    } else {
        console.log('User information not available');
    }
  
});

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

    appointments.sort((a, b) => {
       
        const dateComparison = new Date(b.date) - new Date(a.date);
        if (dateComparison !== 0) {
            return dateComparison; 
        } else {
           
            return a.time.localeCompare(b.time);
        }
    });
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

    appointments.sort((a, b) => {
       
        const dateComparison = new Date(b.date) - new Date(a.date);
        if (dateComparison !== 0) {
            return dateComparison; 
        } else {
           
            return a.time.localeCompare(b.time);
        }
    });

   
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

    lastAppointments.sort((a, b) => {
        // Compara as datas
        const dateComparison = new Date(b.date) - new Date(a.date);
        if (dateComparison !== 0) {
            return dateComparison; 
        } else {
           
            return a.time.localeCompare(b.time);
        }
    });
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

    upcomingAppointments.sort((a, b) => {
       
        const dateComparison = new Date(b.date) - new Date(a.date);
        if (dateComparison !== 0) {
            return dateComparison; 
        } else {
           
            return a.time.localeCompare(b.time);
        }
    }); 
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
        case 'in_progress':
            return 'state-in-progress';
        case 'on_hold':
            return 'state-on-hold';
        case 'done':
            return 'state-done';
        default:
            return '';
    }
}




function viewAppointmentDetails(appointmentData) {
    const appointment =appointmentData;

    const appointmentDetails = {
        id: appointment.id,
        appointment_number:"0",
        clinic_number:"0",
        diagnosis:"none",
        observations:appointment.occurence,
        occurence:appointment.observations,
        qrCode:appointment.qrCode,
        status:appointment.status,
        vetId:"2c7bb00c-6022-4866-9165-a8f4886f3406",
        time:appointment.time,
        userId: appointment.userId,
        oetId: appointment.pet.id,
        petWeight: appointment.weight || 0,
        petHeight: appointment.height || 0,
        petBloodType: appointment.bloodType || "BloodType",
        observations: appointment.observations,
        state: appointment.state
    };

    currentAppointmentDetails=appointmentDetails;


    document.getElementById('app-id').textContent = appointment.id;
    document.getElementById('id-client').textContent = appointment.user.id;
    document.getElementById('name-client').textContent = appointment.user.firstName;
    document.getElementById('email-client').textContent = appointment.user.email;
    document.getElementById('phone-client').textContent = appointment.user.phone | "N/A";
    document.getElementById('id-pet').textContent = appointment.pet.id;
    document.getElementById('name-pet').textContent = appointment.pet.name;
    document.getElementById('type-pet').textContent = appointment.pet.type;
    document.getElementById('breed-pet').textContent = appointment.pet.breed;
    document.getElementById('color-pet').textContent = appointment.pet.color;
    document.getElementById('age-pet').textContent = appointment.pet.age;
    document.getElementById('weight-pet').value = appointment.pet.weight ;
    document.getElementById('height-pet').value = appointment.pet.height ;
    document.getElementById('bloodtype-pet').value = appointment.pet.bloodType | "BloodType";
    document.getElementById('medical-info-pet').value = appointment.pet.medicalInfo;
    document.querySelector('.app-occurence p').textContent = appointment.observations;


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
        const appointmentId = document.getElementById('app-id').textContent;
        const userId = document.getElementById('id-client').textContent;;
        const agePet = document.getElementById('age-pet').textContent;
        const namePet = document.getElementById('name-pet').textContent;
        const typePet = document.getElementById('type-pet').textContent;
        const breedPet = document.getElementById('breed-pet').textContent;
        const colorPet = document.getElementById('color-pet').textContent;
        const petid = document.getElementById('id-pet').textContent;
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


        const petData ={
            id:petid,
            userId:userId,
            name:namePet,
            type:typePet,
            breed:breedPet,
            color:colorPet,
            age:agePet,
            weight: weightPet,
            height: heightPet,
            bloodType: bloodTypePet,
            medicalInfo:medicalInfoPet
        }

        currentAppointmentDetails.prescriptions = prescriptions;


        updatepet(petData);
        updateAppointment(currentAppointmentDetails);
    });
});


async function updatepet(petData){
    console.log(petData);

    const token =localStorage.getItem('token'); 

    try{
        const response = await fetch(`http://localhost:8081/api/vet/pets/${petData.id}`,{
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(petData)
        });
        if (response.ok) {
            const updatedAppointment = await response.json();
            console.log('Pet updated:', updatedAppointment);
        } else {
            console.error('Failed to update pet');
        }
    } catch (error){
        console.error('Error updating pet:', error);
    }


}

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


document.getElementById('done-app').addEventListener('click', function() {
    const appointmentId = document.getElementById('app-id').textContent.trim();
    if (appointmentId) {
        const url = `http://localhost:8081/api/vet/appointment/terminate/${appointmentId}`;
        const token = localStorage.getItem('token');
        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to terminate appointment');
        })
        .then(data => {
            console.log('Appointment terminated successfully:', data);
        })
        .catch(error => {
            console.error('Error terminating appointment:', error);
        });
    } else {
        console.error('Appointment ID is missing');
    }
});


document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById('choice-modal');
    modal.style.display = 'block';

    document.getElementById('close2').addEventListener('click', function() {
        modal.style.display = 'none';
    });
   

    document.getElementById('confirm-choice').addEventListener('click', function() {
        const selectedOption = document.querySelector('input[name="choice"]:checked');
        if (selectedOption) {
            const choice = selectedOption.value;
          
            document.getElementById('user-choice').value = choice;
          
            modal.style.display = 'none';
            console.log('Vet choice:', choice); 
        } else {
            alert('Please select an option.');
        }
    });
});


function getNextAppointment() {

    const clinicChoice = document.getElementById('user-choice').value || '01'; 
    const url = `http://localhost:8081/api/vet/appointment/next/${clinicChoice}`;
    const token = localStorage.getItem('token');
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data) {
            
                console.log("Next appointment:", data);
                viewAppointmentDetails(data);
            
            } else {
                
                console.log("No appointments found for clinic " + clinicNumber);
            }
        })
        .catch(error => {
            console.error("Error fetching next appointment:", error);
        });
}