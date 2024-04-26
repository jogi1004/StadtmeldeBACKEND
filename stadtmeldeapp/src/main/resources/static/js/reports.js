function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev, target, state) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    var draggedElement = document.getElementById(data);

    var targetBody = document.getElementById(target + 'Body');
    if (targetBody) {
        targetBody.appendChild(draggedElement);
        updateReport(draggedElement.getAttribute("id"), state);
    }
}

function updateReport(id, state) {
    var numStr = id.substring(7);

    var hostname = window.location.origin;
    const url = hostname + '/reports/admin/' + numStr;

    const bodyData = typeof state === 'string' ? { state: state } : state;

    // Die Konfiguration für den Fetch Request
    const requestOptions = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: state
    };

    // Fetch Request, um die Daten von der API abzurufen
    fetch(url, requestOptions)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
        })
        .catch(error => {
            // Fehler behandeln
            console.error('Fetch Error:', error);
        });
}