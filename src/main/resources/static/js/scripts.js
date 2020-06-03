<!--    JS function to hide username and password entry for other employees (addEmployee.html) Ilias-->
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

<!--    JS function to hide role entry (updateEmployee.html) Ilias-->
function showPriv(input){
    if (input.checked == true){
        document.getElementById("rolepass").style.display = "block";
        alert("A new username/ password will have to be selected for admin or user!");
        document.getElementById('role').disabled = false;
    } else {
        document.getElementById("rolepass").style.display = "none";
        document.getElementById('role').value = '';
    }
}

<!--    JS function to show cpr or passport input fields based on country Marianna-->
function cprOrPass(){
    var country = document.getElementById("country");
    if (country.value != 'Denmark'){
        document.getElementById("cprLabel").innerHTML="Passport:";
        document.getElementById("cpr").pattern="[A-Z0-9]+";
        document.getElementById("cpr").title="Must contain only capital letters and/or numbers.";
        document.getElementById("cpr").placeholder="ex. M0993353";
    } else {
        document.getElementById("cprLabel").innerHTML="CPR:";
        document.getElementById("cpr").pattern="(\\d){6}-(\\d){4}";
        document.getElementById("cpr").title="Must be in this format: DDMMYY-SSSS";
        document.getElementById("cpr").placeholder="ex. 251278-4568";
    }
}
