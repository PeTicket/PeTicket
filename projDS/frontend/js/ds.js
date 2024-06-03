document.addEventListener("DOMContentLoaded", () => {
    const lastCallsInfoDiv = document.querySelector('.last-calls-info');

    const fetchCacheEntries = () => {
        fetch('http://display-service:8888/api/cache/all')
            .then(response => response.json())
            .then(data => {
                const lastThreeEntries = Object.entries(data).slice(-3);
                displayLastThreeCacheEntries(lastThreeEntries);
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

    setInterval(fetchCacheEntries, 1000);

    fetchCacheEntries();
});
