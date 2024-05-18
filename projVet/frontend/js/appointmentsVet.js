function updateTime() {
    const now = new Date();
    const daysOfWeek = ['Domingo', 'Segunda-feira', 'Terça-feira', 'Quarta-feira', 'Quinta-feira', 'Sexta-feira', 'Sábado'];
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


function changeContent(option) {
    // Obtém todos os elementos da classe 'selected' e remove a classe deles
    const selectedOptions = document.querySelectorAll('.selected');
    selectedOptions.forEach(option => option.classList.remove('selected'));

    // Adiciona a classe 'selected' ao item selecionado
    event.target.classList.add('selected');

    // Oculta todos os conteúdos
    const allContents = document.querySelectorAll('.content-column > div');
    allContents.forEach(content => content.style.display = 'none');

    // Exibe o conteúdo correspondente ao item selecionado
    document.getElementById(`content-${option}`).style.display = 'block';
}
