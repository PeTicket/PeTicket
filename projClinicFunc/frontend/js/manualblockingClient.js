
function logout() {
    localStorage.removeItem('tokenF');
    window.location.href = './index.html';
}



async function addClient() {

    const jwtToken = localStorage.getItem("tokenF");
   
    const firstName = document.getElementById('name').value;
    const lastName = document.getElementById('lname').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
  
  
    const clientData = {
      firstName: firstName,
      lastName: lastName,
      email: email,
      phone: phone,
      password:"password"
    };
  
    try {
      
      const response = await fetch('http://localhost:8082/api/func/user/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${jwtToken}`
        },
        body: JSON.stringify(clientData)
      });
  
     
      if (response.ok) {
        const createdUser = await response.json();
        console.log('Client created:', createdUser);
       
      } else {
        console.error('Failed to create client:', response.statusText);
       
      }
    } catch (error) {
      console.error('Error creating client:', error);
      
    }
  }
  

  document.querySelector('.button-addpet').addEventListener('click', addClient);