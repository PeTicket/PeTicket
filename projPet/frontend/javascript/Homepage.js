const qrIcons = document.querySelectorAll('.qrcode-icon i');
    qrIcons.forEach(icon => {
      icon.addEventListener('click', () => {
        const appointmentId = icon.getAttribute('data-id');
        const appointment = appointments.find(app => app.id === appointmentId);
        if (appointment) {
          displayQRModal(appointment);
        }
      });
    });


function displayQRModal(appointment) {
    const modal = document.getElementById("qrModal");
    const span = document.getElementsByClassName("close2")[0];
    const qrcodeDiv = document.getElementById("qrcode");

    const qrCodeBase64 = appointment.qrCode;

   const dataUrl = `data:image/png;base64,${qrCodeBase64}`;

   const imgElement = document.createElement('img');
  imgElement.src = dataUrl;
  imgElement.style.display = 'block';
  imgElement.style.margin = 'auto auto'; 

  if (window.innerWidth < 1000) {
    imgElement.style.width = '250px';
    imgElement.style.height = '250px';
} else {
    imgElement.style.width = '400px';
    imgElement.style.height = '400px';
}
  imgElement.alt = 'QR Code Image';

  qrcodeDiv.innerHTML = '';

  qrcodeDiv.appendChild(imgElement);
  const petName = appointment.pet.name;
  const pElement = document.createElement('p');
  pElement.textContent = `QR code for appointment with ${petName}`;
  pElement.style.textAlign = 'center';
  pElement.style.marginTop = '10px';

  qrcodeDiv.appendChild(pElement);

    modal.style.display = "block";

    span.onclick = function() {
      modal.style.display = "none";
    }

    window.onclick = function(event) {
      if (event.target == modal) {
        modal.style.display = "none";
      }
    }
  }