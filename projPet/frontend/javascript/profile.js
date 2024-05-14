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


  document.addEventListener('DOMContentLoaded', function() {
    fetchPets();
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



  function fetchPets() {
    const userId = 1; 
    fetch(`http://localhost:8080/api/client/pet/by-user-id/${userId}`)
      .then(response => response.json())
      .then(pets => displayPets(pets))
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
          <span class="icon-view-info"><i class="fas fa-eye"></i></span>
          <span class="icon-delete"><i class="fas fa-trash-alt"></i></span>
        </div>
      `;
      petContainer.appendChild(petDiv);
    });
  }
  





  function logout() {
    window.location.href = './Homepage.html';
}
