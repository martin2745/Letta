function doLogin(login, password) {
    $.ajax({
        url: 'rest/users/' + login,
        type: 'GET',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('Authorization', 'Basic ' + btoa(login + ":" + password));
        }
    })
        .done(function () {
            localStorage.setItem('authorization-token', btoa(login + ":" + password));
            window.location = 'main.html';
        })
        .fail(function () {
            alert('Invalid login and/or password.');
        });
}

function doRegister(user,login,password) {
    $.ajax({
        url: 'rest/users/',
        type: 'POST',
        data: user
    }).done(function () {
        localStorage.setItem('authorization-token', btoa(login + ":" + password));
        window.location = 'main.html';
    });
}

function doLogout() {
    localStorage.removeItem('authorization-token');
    window.location = 'index.html';
}

function insertRegisterForm(parent) {
    parent.append(' <div>\n' +
        '            <img src="images/logo.png" alt="logo" style="margin-bottom: 5px;">\n' +
        '        </div>' +
        '\n' +
        '    <label for="name" class="sr-only">Nombre</label> <input id="name"\n' +
        '                                                              name="name" type="text" class="form-control"\n' +
        '                                                              placeholder="Nombre"\n' +
        '                                                              required autofocus/> ' +
        '    <label for="surname" class="sr-only">Apellido</label> <input id="surname"\n' +
        '                                                              name="surname" type="text" class="form-control"\n' +
        '                                                              placeholder="Apellido"\n' +
        '                                                              required autofocus/> ' +
        '    <label for="login" class="sr-only">Usuario</label> <input id="login"\n' +
        '                                                              name="login" type="text" class="form-control"\n' +
        '                                                              placeholder="Usuario"\n' +
        '                                                              required autofocus/> ' +
        '<input type="hidden" id="role" value="USER">' +
        '<label for="password" class="sr-only">Contraseña</label>\n' +
        '    <input id="password" name="password" type="password"\n' +
        '           class="form-control" placeholder="Contraseña" required/>\n' +
        '\n' +
        '    <button type="submit" class="btn btn-lg btn-info btn-block mt-3">Registrarse</button>'
    );
}

/*async function encodeSha256(text) {
   const msgBuffer = new TextEncoder().encode(text);

   // hash the message
   const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);

   // convert ArrayBuffer to Array
   const hashArray = Array.from(new Uint8Array(hashBuffer));

   // convert bytes to hex string
   const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
   return hashHex;
} */