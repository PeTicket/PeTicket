document.addEventListener("DOMContentLoaded", () => {
    const lastCallsInfoDiv = document.querySelector('.last-calls-info');
    let oldData = {}; // Armazena os dados antigos para comparação

    const fetchCacheEntries = () => {
        fetch('http://deti-tqs-13.ua.pt:8888/api/cache/all')
            .then(response => response.json())
            .then(data => {
                const lastThreeEntries = Object.entries(data).slice(-3);
                displayLastThreeCacheEntries(lastThreeEntries);
                updateRoomNumbers(data);
            })
            .catch(error => {
                console.error('Error fetching cache entries:', error);
            });
    };

    const displayLastThreeCacheEntries = (entries) => {
        lastCallsInfoDiv.innerHTML = ''; 
        entries.forEach(([key, value]) => {
            const entryDiv = document.createElement('div');
            entryDiv.classList.add('eachcall');
            entryDiv.innerHTML = `<h2>${key}: ${value}</h2>`;
            lastCallsInfoDiv.appendChild(entryDiv);
        });
    };

    const updateRoomNumbers = (data) => {
        for (const [room, number] of Object.entries(data)) {
            const personNumberDiv = document.getElementById(room);
            if (personNumberDiv) {
                const h1 = personNumberDiv.querySelector('h1');
                const oldValue = parseInt(h1.innerText); 
                h1.innerText = number.toString().padStart(2, '0');
                if (oldData[room] !== undefined && oldValue !== number) {
               
                    h1.classList.add('flash');
                    setTimeout(() => {
                        h1.classList.remove('flash');
                    }, 10000);
                }
            }
        }
        oldData = { ...data };
    };

    setInterval(fetchCacheEntries, 2000);

    fetchCacheEntries();
});
