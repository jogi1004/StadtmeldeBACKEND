function toggleDropdown(dropdownId) {
    var numStr = dropdownId.substring(11);
    var dropdown = document.getElementById(dropdownId);
    var subCategory = document.getElementById('SubCategories_' + numStr);
    var mainCategory = document.getElementById('MainCategory_' + numStr);
    if (subCategory.style.display === "block") {
        subCategory.style.display = "none";
        dropdown.classList.add('hover');
        mainCategory.classList.remove('hover');
        mainCategory.classList.remove('borderSchemeBottom');
    } else {
        subCategory.style.display = "block";
        dropdown.classList.remove('hover');
        mainCategory.classList.add('hover');
        mainCategory.classList.add('borderSchemeBottom');
    }
}