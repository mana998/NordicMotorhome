<!--    JS function to hide username and password entry for other employees (Ilias)-->
function showFields(select) {
    if (select.value == 'admin' || select.value == 'user') {
        document.getElementById('pass').style.display = 'block';
        document.getElementById('usern').value = '';
        document.getElementById('passwd').value = '';
        document.getElementById('passwd2').value = '';
    } else {
        document.getElementById('pass').style.display = 'none';
        document.getElementById('usern').value = '';
        document.getElementById('passwd').value = '';
        document.getElementById('passwd2').value = '';
    }
}