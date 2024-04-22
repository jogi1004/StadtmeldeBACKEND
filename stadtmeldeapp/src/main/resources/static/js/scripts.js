function fixFooter() {
    var contentHeight = document.getElementById('content').offsetHeight;
    var viewportHeight = window.innerHeight;
    var footer = document.querySelector('footer');

    if (contentHeight < viewportHeight) {
        var footerOffset = viewportHeight - contentHeight - footer.offsetHeight;
        footer.style.position = 'relative';
        footer.style.top = footerOffset + 'px';
    } else {
        footer.style.position = 'static';
    }
}

// Aufruf der Funktion, wenn sich die Fenstergröße ändert oder der Inhalt der Seite geladen wird
window.addEventListener('resize', fixFooter);
window.addEventListener('DOMContentLoaded', fixFooter);
