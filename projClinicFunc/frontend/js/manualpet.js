let fetchedUserId = null;

async function fetchUserByEmail(email) {

    const jwtToken = localStorage.getItem("tokenF");
  try {
    const response = await fetch(`http://deti-tqs-13.ua.pt:8082/api/func/user/${email}`,{
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken}`
        },
    });
    if (!response.ok) {
      throw new Error('User not found');
    }
    const user = await response.json();
    fetchedUserId = user.id; 
    console.log('User ID fetched:', fetchedUserId);
  } catch (error) {
    console.error(error);
    fetchedUserId = null; 
  }
}

async function addPet() {
  const email = document.getElementById('user').value;
  await fetchUserByEmail(email);

  if (fetchedUserId) {
    const petData = {
      userId: fetchedUserId,
      name: document.getElementById('name').value,
      type: document.getElementById('type').value,
      breed: document.getElementById('breed').value,
      age: document.getElementById('age').value,
      unit: document.getElementById('unit-selector').value,
      color: document.getElementById('color').value
    };

    const jwtToken = localStorage.getItem("tokenF");
    try {
      const response = await fetch(`http://deti-tqs-13.ua.pt:8082/api/func/pet/user/${petData.userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${jwtToken}`
        },
        body: JSON.stringify(petData)
      });

      if (response.ok) {
        console.log('Pet added successfully');
        
      } else {
        console.error('Failed to add pet');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  } else {
    console.error('User ID not found, cannot add pet');
  }
}

document.querySelector('.button-addpet').addEventListener('click', addPet);