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
    document.getElementById('unit-selector').addEventListener('change', function() {
      var input = document.getElementById('age');
      var selectedUnit = this.value;
      console.log("ola");

      if (selectedUnit === 'months') {
        input.setAttribute('min', '1');
        input.setAttribute('max', '12');
      } else {
        input.setAttribute('min', '1');
        input.setAttribute('max', '100');
      }
    });
  });



  function addPet() {
    const name = document.getElementById('name').value;
    const type = document.getElementById('type').value;
    const breed = document.getElementById('breed').value;
    const age = document.getElementById('age').value;
    const unit = document.getElementById('unit-selector').value;
    const color = document.getElementById('color').value;

    const pet = {
        userId: 1,
        name: name,
        type: type,
        breed: breed,
        age: age + ' ' + unit,
        color: color
    };

    fetch('http://localhost:8080/api/client/pet/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(pet)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to add pet');
        }
    })
    .then(data => {
        console.log('Pet added:', data);
        alert('Pet added successfully!');
        document.getElementById('add-pet-form').reset();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error adding pet');
    });
}



function logout() {
  window.location.href = './Homepage.html';
}
