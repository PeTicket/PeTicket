document.getElementById("create-button").addEventListener("click", function() {
    window.location.href = "./manualblockingClient.html"; 
  });


  document.getElementById("create-button2").addEventListener("click", function() {
    window.location.href = "./manualpet.html"; 
  });
  
  
  
  


function logout() {
    localStorage.removeItem('tokenF');
    window.location.href = './index.html';
}


document.addEventListener('DOMContentLoaded', async function() {
  const jwtToken = localStorage.getItem("tokenF");
  try {
    const response = await fetch('http://localhost:8082/api/func/users',{
      method: 'GET',
      headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${jwtToken}`
      },
    });
    const clients = await response.json();
    console.log(clients);
    displayClients(clients);
  } catch (error) {
    console.error('Erro ao buscar clientes:', error);
  }
});

function displayClients(clients) {
  const container = document.getElementById('clients-container');
  clients.forEach(client => {
    const clientDiv = document.createElement('div');
    clientDiv.classList.add('client');
    clientDiv.innerHTML = `
      <h3>${client.firstName}</h3>
      <h3>${client.lastName}</h3>
      <h3>${client.phone}</h3>
      <p>Email: ${client.email}</p>
      <button class="view-pets-btn" data-client-id="${client.id}"><i class="fas fa-eye"></i> Ver Pets</button>
    `;
    container.appendChild(clientDiv);
  });
}


const modal = document.getElementById('petModal');
const closeModalBtn = document.getElementsByClassName('close')[0];


window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}

closeModalBtn.onclick = function() {
  modal.style.display = "none";
}

document.getElementById('clients-container').addEventListener('click', async function(event) {
  const jwtToken = localStorage.getItem("tokenF");
  if (event.target.classList.contains('view-pets-btn')) {
    const clientId = event.target.dataset.clientId;
    try {
      const response = await fetch(`http://localhost:8082/api/func/pets/users/${clientId}`,{
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken}`
        },
      });
      const pets = await response.json();
      displayPets(pets);
      modal.style.display = "block";
    } catch (error) {
      console.error('Erro ao buscar pets do cliente:', error);
    }
  }
});

function displayPets(pets) {
  const petList = document.getElementById('petList');
  petList.innerHTML = '';
  if (pets.length === 0) {
    petList.innerHTML = '<p>Nenhum pet encontrado para este cliente.</p>';
  } else {
    pets.forEach(pet => {
      const petItem = document.createElement('div');
      petItem.innerHTML = `<p><strong>Nome:</strong> ${pet.name}, <strong>Espécie:</strong> ${pet.specie}, <strong>Raça:</strong> ${pet.breed}</p>`;
      petList.appendChild(petItem);
    });
  }
}
document.querySelector('.search-button').addEventListener('click', function() {
  const searchEmail = document.getElementById('searchemail').value.trim().toLowerCase();
  searchClientsByEmail(searchEmail);
});


function searchClientsByEmail(email) {
  const clientsContainer = document.querySelector('.clients-container');
  const clients = document.querySelectorAll('.client');
  clients.forEach(client => {
    const clientEmail = client.querySelector('p').textContent.split(':')[1].trim().toLowerCase();
    if (clientEmail.includes(email)) {
      client.style.display = 'flex'; 
    } else {
      client.style.display = 'none'; 
    }
  });
}
