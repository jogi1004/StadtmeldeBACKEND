function loadSubcategoryPage(maincategoryId) {
    window.location.href = '/subcategory?maincategoryId=' + maincategoryId;
}

function addDIV() {
    // CategoryConatainer
    var categoryContainer = document.getElementById('categoryContainer');

    // Anzahl aller Kategorien
    var categoryCount = categoryContainer.children.length + 1;

    // Neues Div erstellen
    var newDiv = document.createElement('div');
    newDiv.id = 'category_' + categoryCount;
    newDiv.className = 'borderScheme mb-3';
    newDiv.style.minHeight = '68px';

    // Neues InnerNewDiv erstellen
    var innerNewDiv = document.createElement('div');
    innerNewDiv.id = 'innerCategoryContainer_' + categoryCount;
    innerNewDiv.className = 'categoryContainer';

    // categoryContainer innere Container erstellen
    var categoryHeader = document.createElement('div');
    categoryHeader.id = 'category_header_' + categoryCount;
    categoryHeader.className = 'categoryHeader';
    /* categoryHeader.onclick = function () {
        openCategoryDetailView(this);
    }; */
    var categoryBody = document.createElement('div');
    categoryBody.id = 'category_body_' + categoryCount;
    categoryBody.className = 'center categoryBody';
    /* categoryBody.onclick = function () {
        openCategoryDetailView(this);
    }; */
    var categoryFooter = document.createElement('div');
    categoryFooter.id = 'category_footer_' + categoryCount;
    categoryFooter.className = 'categoryFooter p-1';

    // categoryHeader Index hinzufügen
    var categoryIndex = document.createTextNode(categoryCount);
    var categoryHeaderInnerDiv = document.createElement('div');
    categoryHeaderInnerDiv.appendChild(categoryIndex);
    categoryHeaderInnerDiv.id = 'index_category_' + categoryCount;
    categoryHeaderInnerDiv.className = 'borderSchemeRightBottom p-0 center';
    categoryHeaderInnerDiv.style.height = '30px';
    categoryHeaderInnerDiv.style.width = '30px';
    categoryHeader.appendChild(categoryHeaderInnerDiv);

    // categoryBody mit neuem Input-Element füllen
    var userInput = document.createElement('input');
    userInput.type = 'text';
    userInput.placeholder = 'Hier den Namen der neuen Kategorie eingeben';
    userInput.style.border = 'none'; // Rahmen ausblenden
    userInput.style.outline = 'none'; // Außenlinie ausblenden
    userInput.style.textAlign = 'center'; // Text zentrieren
    userInput.style.width = '100%'; // Input Element 100% breite geben
    categoryBody.appendChild(userInput);

    // categoryFooter neuen editButton hinzufügen
    var editButton = document.createElement("button");
    editButton.setAttribute("id", 'editCategoryButton_' + categoryCount);
    editButton.className = 'icon-button-edit endTop hwAuto';
    var imgElementEdit = document.createElement("img");
    imgElementEdit.setAttribute("src", "images/editIcon.png");
    imgElementEdit.setAttribute("alt", "Icon");
    editButton.appendChild(imgElementEdit);
    editButton.onclick = function () {
        convertParagraphToInput(this);
    };
    categoryFooter.appendChild(editButton);

    // catgeoryFooter neuen deleteButton hinzufügen
    var deleteButton = document.createElement("button");
    deleteButton.setAttribute("id", 'deleteCategoryButton_' + categoryCount);
    deleteButton.className = 'icon-button-edit endBottom hwAuto';
    var imgElementDelete = document.createElement("img");
    imgElementDelete.setAttribute("src", "images/deleteIcon.png");
    imgElementDelete.setAttribute("alt", "Icon");
    deleteButton.appendChild(imgElementDelete);
    deleteButton.onclick = function () {
        deleteCategory(this);
    };
    categoryFooter.appendChild(deleteButton);

    // alle Container dem neuen div anhängen
    innerNewDiv.appendChild(categoryHeader);
    innerNewDiv.appendChild(categoryBody);
    innerNewDiv.appendChild(categoryFooter);

    // innerNewDiv dem newDiv hinzufügen
    newDiv.appendChild(innerNewDiv);

    // Das neue Div zum Category-Container hinzufügen
    categoryContainer.appendChild(newDiv);

    // Fokus auf das Input-Feld setzen
    setTimeout(function () {
        userInput.focus();
    }, 0);

    // Event Listener für das Blur-Ereignis hinzufügen
    userInput.addEventListener('blur', function (event) {
        if (!eventHandlerNewDiv.isEnterPressed) {
            console.log("blur event");
            eventHandlerNewDiv(event);
        } else {
            eventHandlerNewDiv.isEnterPressed = false;
        }
    });

    // Event Listener für die Eingabe hinzufügen
    userInput.addEventListener('keyup', function (event) {
        // Wenn Enter gedrückt wurde
        if (event.keyCode === 13) {
            console.log("enter event");
            eventHandlerNewDiv.isEnterPressed = true;
            eventHandlerNewDiv(event);
        }
    });

    function eventHandlerNewDiv(event) {
        if (event.type === 'keyup') {
            saveCategoryInDatabase(userInput)
        } else if (event.type === 'blur') {
            saveCategoryInDatabase(userInput);
        }
    }
};



function convertParagraphToInput(element) {
    // item id
    var numStr = element.id.substring(19);

    // p-ELement und categoryBody
    var paragraph = document.getElementById('category_paragraph_' + numStr);
    var categoryBody = document.getElementById('category_body_' + numStr);

    // categoryBody mit neuem Input-Element füllen
    var userInput = document.createElement('input');
    userInput.type = 'text';
    userInput.style.border = 'none'; // Rahmen ausblenden
    userInput.style.outline = 'none'; // Außenlinie ausblenden
    userInput.style.textAlign = 'center'; // Text zentrieren
    userInput.style.width = '100%'; // Input Element 100% breite geben
    userInput.value = paragraph.innerText;

    categoryBody.replaceChild(userInput, paragraph);

    // Parent-Div hover class zuweisen
    var parentDiv = document.getElementById('category_' + numStr);
    parentDiv.className = 'borderScheme mb-3';
    parentDiv.style.minHeight = '68px';

    userInput.focus();

    // Event Listener für das Blur-Ereignis hinzufügen
    userInput.addEventListener('blur', function () {
        convertInputToParagraph(userInput, numStr);
    });

    // Event Listener für die Eingabe hinzufügen
    userInput.addEventListener('keyup', function (event) {
        // Wenn Enter gedrückt wurde
        if (event.keyCode === 13) {
            convertInputToParagraph(userInput, numStr);
        }
    });
}

function openCategoryDetailView(element) {
    var hostname = window.location.origin;
    window.location.href = hostname + '/subcategory';
}

function saveCategoryInDatabase(inputElement) {
    const data = {
        title: inputElement.value,
        reportingLocationId: 3
    };

    var hostname = window.location.origin;
    const url = hostname + '/maincategory';


    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    };

    fetch(url, requestOptions)
        .then(response => {
            if (response.ok) {
                window.location.href = '/category?successSave';
            } else {
                throw new Error('Network response was not ok');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            window.location.href = '/category?errorSave';
        });
}

function convertInputToParagraph(inputElement, numStr) {
    var categoryDiv = document.getElementById('category_' + numStr);
    var categoryBody = document.getElementById('category_body_' + numStr);
    var paragraph = document.createElement('p');
    paragraph.id = 'category_paragraph_' + numStr;
    paragraph.textContent = inputElement.value;
    paragraph.className = 'center m-0';

    // Parent-Div hover class zuweisen
    categoryDiv.className = 'borderScheme hover mb-3';

    inputElement.style.display = 'none';
    categoryBody.innerHTML = "";
    categoryBody.appendChild(paragraph);
}

function deleteCategory(element) {
    console.log('delete: ' + element.id);
    var modal = document.getElementById('confirmationModal');
    modal.ariaHidden = 'false';
}

function deleteCategory(element) {
    console.log("Delete button clicked"); // Konsolenausgabe für Debugging
    // Extrahiere die Nummer der Kategorie aus der ID des Elements
    var categoryId = element.id.split('_')[1];

    // Finde das entsprechende Element, das gelöscht werden soll
    var categoryDiv = document.getElementById('category_' + categoryId);

    // Extrahiere den Namen der Kategorie aus dem Paragrapheninhalt
    var categoryParagraph = document.getElementById('category_paragraph_' + categoryId);
    var categoryName = categoryParagraph.textContent.trim();

    // Füge den Kategorienamen in das Löschmodal ein
    var modalBody = document.querySelector('#confirmationModal .modal-body');
    modalBody.textContent = "Bist du sicher, dass du die Kategorie \"" + categoryName + "\" löschen möchtest?";

    // Setze das Löschmodal auf die Kategorie-ID, damit wir sie später löschen können
    $('#confirmationModal').data('categoryId', categoryId);

    // Öffne das Löschmodal
    $('#confirmationModal').modal('show');
}

// Funktion, um die Kategorie tatsächlich zu löschen
function confirmDeleteCategory() {

    console.log("Delete button modal clicked"); // Konsolenausgabe für Debugging
    // Kategorie-ID aus dem Modal extrahieren
    var categoryId = $('#confirmationModal').data('categoryId');

    // Element mit der entsprechenden Kategorie-ID entfernen
    var categoryDiv = document.getElementById('category_' + categoryId);
    categoryDiv.parentNode.removeChild(categoryDiv);

    // Modal schließen und das Modal-Backdrop-Element entfernen
    $('#confirmationModal').modal('toggle');

    // MUSS NOCH EIN RELOAD HIN UM DIE IDS ZU AKTUALISIEREN
}

function closeModal() {
    $('#confirmationModal').modal('toggle');
}