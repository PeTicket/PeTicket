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