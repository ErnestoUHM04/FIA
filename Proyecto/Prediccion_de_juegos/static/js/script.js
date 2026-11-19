let selectedGames = [];

async function searchGames() {
    const query = document.getElementById('gameSearch').value;
    const response = await fetch('/search', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ query }),
    });

    const data = await response.json();
    const resultsDiv = document.getElementById('searchResults');
    resultsDiv.innerHTML = '';

    data.results.forEach(game => {
        const gameItem = document.createElement('div');
        gameItem.textContent = game;
        gameItem.onclick = () => addGameToSelection(game);
        resultsDiv.appendChild(gameItem);
    });
}

function addGameToSelection(game) {
    if (selectedGames.length >= 5) {
        alert("Solo puedes seleccionar 5 juegos.");
        return;
    }

    if (!selectedGames.includes(game)) {
        selectedGames.push(game);
        updateSelectedGames();
    } else {
        alert("El juego ya está seleccionado.");
    }
}

function updateSelectedGames() {
    const selectedDiv = document.getElementById('selectedGames');
    selectedDiv.innerHTML = selectedGames.map(game => `<div>${game}</div>`).join('');
    document.querySelector('h3').textContent = `Juegos seleccionados (${selectedGames.length}/5):`;

    // Habilitar o deshabilitar el botón de recomendación
    const recommendButton = document.getElementById('recommendButton');
    recommendButton.disabled = selectedGames.length !== 5;
}

async function getRecommendations() {
    const response = await fetch('/recommend', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ games: selectedGames }),
    });

    const recommendationsDiv = document.getElementById('recommendations');
    recommendationsDiv.innerHTML = '';

    if (response.status === 400) {
        const errorData = await response.json();
        alert(`${errorData.error}. Faltan ${errorData.required} juegos.`);
        return;
    }

    const data = await response.json();
    data.recommendations.forEach(game => {
        const gameItem = document.createElement('div');
        gameItem.textContent = game;
        recommendationsDiv.appendChild(gameItem);
    });
}
