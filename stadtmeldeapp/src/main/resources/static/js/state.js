document.getElementById('addStatusButton').addEventListener('click', function () {
    changeDivIds();

    // Neues Div erstellen
    var newDiv = document.createElement('div');
    newDiv.id = 'status_1';
    newDiv.className = 'borderScheme mb-3';
    newDiv.style.minHeight = '65px';

    // Neuen Inhalt für das Div hinzufügen
    var newContent = document.createTextNode('1');
    var innerDiv = document.createElement('div');
    innerDiv.appendChild(newContent);
    innerDiv.id = 'index_status_1';
    innerDiv.className = 'borderSchemeRightBottom p-0 center';
    innerDiv.style.height = '100%';
    innerDiv.style.width = '30px';
    newDiv.appendChild(innerDiv);

    // Neues <input> Element erstellen
    var userInput = document.createElement('input');
    userInput.type = 'text';
    userInput.className = 'center'; // Zentriert den Text im Input-Feld
    userInput.placeholder = 'Hier den Namen des neuen Status eingeben';
    userInput.style.border = 'none'; // Rahmen ausblenden
    userInput.style.outline = 'none'; // Außenlinie ausblenden
    userInput.style.textAlign = 'center'; // Text zentrieren
    userInput.onchange =
        newDiv.appendChild(userInput);

    // Event Listener für die Eingabe hinzufügen
    userInput.addEventListener('keyup', function (event) {
        // Wenn Enter gedrückt wurde
        if (event.keyCode === 13) {
            // Text des Input-Felds dem <p> Element zuweisen
            var newText = document.createTextNode(userInput.value);
            var paragraph = document.createElement('p');
            paragraph.appendChild(newText);
            paragraph.className = 'center';
            newDiv.appendChild(paragraph);

            // Input-Feld ausblenden
            userInput.style.display = 'none';

            // Parent-Div hover class zuweisen
            var parentDiv = document.getElementById('status_1');
            parentDiv.className = 'borderScheme hover mb-3';
        }
    });

    // Das neue Div zum #status-Container hinzufügen
    var statusContainer = document.getElementById('statusContainer');
    statusContainer.insertBefore(newDiv, statusContainer.firstChild);

    // Fokus auf das Input-Feld setzen
    setTimeout(function () {
        userInput.focus();
    }, 0);
});


function changeDivIds() {
    // Alle Divs finden, die mit der ID 'status_' beginnen
    var status = document.querySelectorAll('[id^="status_"]');
    var count = status.length;

    for (var i = 0; i < count; i++) {
        var borderDiv = status[i];
        var index = i + 2;
        borderDiv.id = 'status_' + index;
    }

    // Aktualisierung der index_status_ IDs
    var indexStatus = document.querySelectorAll('[id^="index_status_"]');
    var indexCount = indexStatus.length;

    for (var j = 0; j < indexCount; j++) {
        var indexDiv = indexStatus[j];
        var newIndex = j + 2;
        indexDiv.id = 'index_status_' + newIndex;
        indexDiv.textContent = newIndex;
    }
}