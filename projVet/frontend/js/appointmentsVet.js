let previousContentId = '';


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

