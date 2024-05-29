document.addEventListener("DOMContentLoaded", () => {
    const cacheEntriesDiv = document.getElementById('cache-entries');

    
    const fetchCacheEntries = () => {
        fetch('http://localhost:8080/api/cache/all')
            .then(response => response.json())
            .then(data => {
                displayCacheEntries(data);
            })
            .catch(error => {
                console.error('Error fetching cache entries:', error);
            });
    };


    const displayCacheEntries = (entries) => {
        cacheEntriesDiv.innerHTML = ''; 
        for (const [key, value] of Object.entries(entries)) {
            const entryDiv = document.createElement('div');
            entryDiv.textContent = `${key}: ${value}`;
            cacheEntriesDiv.appendChild(entryDiv);
        }
    };

    setInterval(fetchCacheEntries, 1000);

   
    fetchCacheEntries();
});