/* function showSection(sectionId) {
    // Hide all sections within the content container
    var contentSections = document.querySelectorAll('#content > section');
    contentSections.forEach(function (section) {
        section.style.display = 'none';
    });

    // Show the selected section
    var selectedSection = document.getElementById(sectionId);
    selectedSection.style.display = 'block';

    // Check if the selected section is the home section
    checkFooterPosition(sectionId);
}

function checkFooterPosition(sectionId) {
    var footer = document.getElementById('footer');
    if (sectionId === 'home') {
        footer.classList.remove('fixed-bottom');
    } else {
        footer.classList.add('fixed-bottom');
    }
}

// Call the checkFooterPosition function on page load
checkFooterPosition('home'); */
