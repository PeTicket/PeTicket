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

    fetch(`http://localhost:8080/api/client/user/by-email/${email}`, {
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
          console.log('User information:', user);
          populateUserInfo(user);
          fetchPets();
        })
        .catch(error => {
            console.error('Error:', error);
        });
       
    } else {
        console.log('User information not available');
    }
  });  



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




let starCount = 0;

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

  document.addEventListener('DOMContentLoaded', createStarsPeriodically);


  function populateUserInfo(user) {
    document.getElementById('profile-firstname1').textContent = user.firstName || 'N/A';
    document.getElementById('profile-firstname2').textContent = user.firstName || 'N/A';
    document.getElementById('profile-lastname').textContent = user.lastName || 'N/A';
    document.getElementById('profile-email').textContent = user.email;
    document.getElementById('profile-address').textContent = user.address || 'N/A';
    document.getElementById('profile-phone').textContent = user.phone || 'N/A';
}

  function fetchPets() {
    const jwtToken = localStorage.getItem('jwtToken');

    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${jwtToken}`
  };
  
    const userId = currentUserInfo.id; 
    fetch(`http://localhost:8080/api/client/pet/by-user-id`,{
            method: 'GET',
            headers: headers
    })
    .then(response => {
      if (response.ok) {
          
          return response.text().then(text => {
              if (text) {
                  try {
                      return JSON.parse(text);
                  } catch (e) {
                      throw new Error('Failed to parse JSON response');
                  }
              } else {
                  return []; 
              }
          });
      } else {
          throw new Error('Failed to fetch appointments');
      }
  })
      .then(pets => {
        console.log('Pets returned:', pets); 
        displayPets(pets);
      })
      .catch(error => console.error('Error fetching pets:', error));
}


  function displayPets(pets) {
    const petContainer = document.getElementById('profile-pets-container');
    petContainer.innerHTML = '';
    pets.forEach(pet => {
      const petDiv = document.createElement('div');
      petDiv.classList.add('pet-container');
      petDiv.innerHTML = `
        <p>${pet.name}</p>
        <div class="div-buttons-eachpet">
          <span class="icon-view-info" data-pet-info='${JSON.stringify(pet)}'><i class="fas fa-eye"></i></span>
          <span class="icon-delete"><i class="fas fa-trash-alt"></i></span>
        </div>
      `;
      petContainer.appendChild(petDiv);
    });
    document.querySelectorAll('.icon-view-info').forEach(icon => {
      icon.addEventListener('click', function() {
        const petInfo = JSON.parse(this.getAttribute('data-pet-info'));
        showModal(petInfo);
      });
    });
    document.querySelectorAll('.icon-delete').forEach(icon => {
      icon.addEventListener('click', function() {
        const petDiv = this.closest('.pet-container');
        const petInfo = JSON.parse(petDiv.querySelector('.icon-view-info').getAttribute('data-pet-info'));
        const petId = petInfo.id;
        const jwtToken = localStorage.getItem('jwtToken');
    
        
        // if (confirm(`Are you sure you want to delete pet? This action cannot be undone.`)) {
          fetch(`http://localhost:8080/api/client/pet/delete/${petId}`, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${jwtToken}`
            },
            
          })
          .then(response => {
            if (response.ok) {
              // alert("Pet Deleted");
              fetchPets();
            } else {
              console.error('Failed to delete pet:', response.status);
            }
          })
          .catch(error => {
            console.error('Error deleting pet:', error);
          });
        // } else {
          
        //   console.log('Pet deletion canceled.'); 
        // }
      });
    });
    
  }


  function showModal(petInfo) {
    const modal = document.getElementById('pet-info-modal');
    const modalContent = document.getElementById('pet-info-content');
    modalContent.innerHTML = `
      <div class="pet-info-container">
        <p>Name: ${petInfo.name}</p>
        <p>Age: ${petInfo.age}</p>
        <p>Type: ${petInfo.type}</p>
        <p>Breed: ${petInfo.breed}</p>
        <p>Color: ${petInfo.color}</p>
      </div>
      <button class="button-updatepet"></button>
    `;
    modal.style.display = 'flex';
   
  
    document.querySelector('.close-btn').onclick = function() {
      modal.style.display = 'none';
    };
  
    window.onclick = function(event) {
      if (event.target === modal) {
        modal.style.display = 'none';
      }
    };
  
    document.querySelector('.button-updatepet').onclick = function() {
      showUpdateForm(petInfo);
    };
  }
  
  function showUpdateForm(petInfo) {
    const modalContent = document.getElementById('pet-info-content');

    const [ageValue, ageUnit] = petInfo.age.split(' ');
    modalContent.innerHTML = `
    <div class="input-box">
      <div class="user-box">
        <input type="text" id="update-name" name="name" value="${petInfo.name}" required="">
        <label>Name</label>
      </div>
      <div class="user-box">
        <input type="text" id="update-type" value="${petInfo.type}" name="type" required="">
        <label>Specie</label>
      </div>
      <div class="user-box">
        <input type="text" id="update-breed" value="${petInfo.breed}" name="breed" required="">
        <label>Breed</label>
      </div>
      <div class="user-box">
        <input type="number" name="age" id="update-age" value="${parseInt(ageValue, 10)}" required min="1" max="100">
        <label for="age">Age</label>
        <select class="unit-selector" id="unit-selector">
        <option value="years" ${ageUnit === 'years' ? 'selected' : ''}>Years</option>
        <option value="months" ${ageUnit === 'months' ? 'selected' : ''}>Months</option>
        </select>
      </div>
      <div class="user-box">
        <input type="text" id="update-color" value="${petInfo.color}" name="color" required="">
        <label>Color</label>
      </div>
    </div>  
      <button class="button-save-update"></button>
    `;
  
  
    document.querySelector('.button-save-update').onclick = function() {
      const updatedPetInfo = {
        id:petInfo.id,
        userId:petInfo.userId,
        name: document.getElementById('update-name').value,
        age: `${document.getElementById('update-age').value} ${document.getElementById('unit-selector').value}`,
        type: document.getElementById('update-type').value,
        breed: document.getElementById('update-breed').value,
        color: document.getElementById('update-color').value
      };
      saveUpdatedPetInfo(updatedPetInfo);
    };
  }
  
  function saveUpdatedPetInfo(updatedPetInfo) {
    console.log('Updated Pet Info:', updatedPetInfo);

    const jwtToken = localStorage.getItem('jwtToken');
    
  
    fetch(`http://localhost:8080/api/client/pet/update/${updatedPetInfo.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${jwtToken}`
      },
      body: JSON.stringify(updatedPetInfo)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      console.log('Success:', data);
      // alert('Pet updated successfully');
      
      document.getElementById('pet-info-modal').style.display = 'none';
      
      fetchPets(); 
    })
    .catch(error => {
      console.error('Error updating pet:', error);
      alert('Error updating pet');
    });
  }


  document.addEventListener("DOMContentLoaded", function() {

    const originalProfileInfo = document.getElementById('userinfo-container').innerHTML;
   
    document.querySelector('.icon-pencil').addEventListener('click', function() {
        clearProfileInfo();
    });

  

    function clearProfileInfo() {

      
        const profileInfoContainer = document.getElementById('userinfo-container');
        profileInfoContainer.innerHTML = `
        <p class="pets-profile-title">My info</p>
          <div class="profile-info">
                <div class="user-box">
                <input type="text" id="update-firstname" name="name" value="${currentUserInfo.firstName}" required="">
                <label>First Name</label>

                <div class="user-box">
                  <input type="text" id="update-lastname" name="name" value="${currentUserInfo.lastName}" required="">
                  <label>Last Name</label>
                </div>

                <div class="user-box">
                  <input type="text" id="update-email" name="name" value="${currentUserInfo.email}" required="">
                  <label>Email</label>
                </div>

                <div class="user-box">
                  <input type="text" id="update-address" name="name" value="${currentUserInfo.address}" required="">
                  <label>Address</label>
                </div>

                <div class="user-box">
                  <input type="text" id="update-phone" name="name" value="${currentUserInfo.phone}" required="">
                  <label>Phone</label>
                </div>
              </div>
          </div>
          <div class="edit-icon">
            <button class="button-update-userinfo"></button>
            <button class="button-update-cancelupdate"></button>
          </div>
        `; 

        document.querySelector('.button-update-cancelupdate').addEventListener('click', function() {
          restoreOriginalProfileInfo();
      });

      document.querySelector('.button-update-userinfo').addEventListener('click', function() {
        const userDetails = {
            firstName: document.getElementById('update-firstname').value,
            lastName: document.getElementById('update-lastname').value,
            email: document.getElementById('update-email').value,
            address: document.getElementById('update-address').value,
            phone: document.getElementById('update-phone').value
        };

        console.log(userDetails);
        updateUserDetails(userDetails);
    
    });
  }

    function updateUserDetails(userDetails){
      console.log(userDetails);
    const jwtToken = localStorage.getItem('jwtToken');
    
    fetch('http://localhost:8080/api/client/user/update', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken}`
        },
        body: JSON.stringify(userDetails)
    })
    .then(response => {
        if (response.status === 204) {
            console.log('No content, user not found.');
            return null;
        } else if (response.ok) {
            return response.json();
        } else {
            throw new Error('Unexpected response');
        }
    })
    .then(data => {
        if (data) {
            console.log('Success:', data);
            restoreOriginalProfileInfo()
        }
    })
    .catch((error) => {
        console.error('Error:', error);
    });
  }
    
 

    function restoreOriginalProfileInfo() {
      const profileInfoContainer = document.getElementById('userinfo-container');
      profileInfoContainer.innerHTML = originalProfileInfo;
      populateUserInfo(currentUserInfo);
  }
});





  function logout() {
    localStorage.removeItem('jwtToken');
    window.location.href = './Homepage.html';
}
