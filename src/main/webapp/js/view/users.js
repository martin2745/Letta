var UsersView = (function () {
    var dao;

    // Referencia a this que permite acceder a las funciones públicas desde las funciones de jQuery.
    var self;

    var formId = 'users-form';
    var listId = 'users-list';
    var formQuery = '#' + formId;
    var listQuery = '#' + listId;

    function UsersView(usersDao, formContainerId, listContainerId) {
        dao = usersDao;
        self = this;

        insertUsersForm($('#' + formContainerId));
        insertUsersList($('#' + listContainerId));

        this.init = function () {
            dao.listUsers(function (users) {
                $.each(users, function (key, user) {
                    appendToTable(user);
                });
            },
                    function () {
                        alert('No ha sido posible acceder al listado de usuarios.');
                    });

            // La acción por defecto de enviar formulario (submit) se sobreescribe
            // para que el envío sea a través de AJAX
            $(formQuery).submit(function (event) {
                var user = self.getUserInForm();

                if (self.isEditing(user)) {
                   // document.getElementById('password').style.display = "none";
                   // document.getElementById('loginInsertar').value = document.getElementById('loginEditar').value;
                    dao.modifyUser(user,
                            function (user) {
                                $('#user-' + user.login + ' td.password').text(user.password);
                                $('#user-' + user.login + ' td.role').text(user.role);
                                self.resetForm();
                            },
                            showErrorMessage,
                            self.enableForm
                            );
                } 
                return false;
            });

            $('#btnClear').click(this.resetForm);
        };

        this.getUserInForm = function () {
            var form = $(formQuery);
            //console.log(form.find($('#loginEditar')).val());
            return {                // ***
                'login': form.find('input[name="login"]').val(),
                'password': form.find('input[name="password"]').val(),
                'role': form.find('input[name="role"]').val()
            };
        };

        this.getUserInRow = function (login) {
            var row = $('#user-' + login);

            if (row !== undefined) {
                return {
                    'login': login,
                    'password': row.find('td.password').text(),
                    'role': row.find('td.role').text()
                };
            } else {
                return undefined;
            }
        };

        this.editUser = function (login) {

            var row = $('#user-' + login);

            if (row !== undefined) {
                var form = $(formQuery);

                //document.getElementById('loginEditar').style.display = "block";
               //document.getElementById('loginInsertar').style.display = "none";
                //form.find($('#loginEditar')).val(login);
                form.find('input[name="login"]').val(login);
                form.find('input[name="password"]').val(row.find('td.password').text());
                form.find('input[name="role"]').val(row.find('td.role').text());

                document.getElementById('btnSubmit').style.display = "inline-block";
            }
        };

        this.deleteUser = function (login) {
            if (confirm('Está a punto de eliminar a un usuario. ¿Está seguro de que desea continuar?')) {
                dao.deleteUser(login,
                        function () {
                            $('tr#user-' + login).remove();
                        },
                        showErrorMessage
                        );
            }
        };

        this.isEditing = function (user) {
            /// ????????????????????
            return true;
        };

        this.disableForm = function () {
            $(formQuery + ' input').prop('disabled', true);
        };

        this.enableForm = function () {
            $(formQuery + ' input').prop('disabled', false);
        };

        this.resetForm = function () {
            document.getElementById('btnSubmit').style.display = "none";
            $(formQuery)[0].reset();
            $(formQuery + ' input[name="login"]').val('');
            $('#btnSubmit').val('Editar');
        };
    }
    ;

    var insertUsersList = function (parent) {
        parent.append(
                '<table id="' + listId + '" class="table">\
				<thead>\
					<tr class="row">\
						<th class="col-sm-4">Usuario</th>\
                                                <th class="col-sm-4">Rol</th>\
						<th class="col-sm-4">&nbsp;</th>\
					</tr>\
				</thead>\
				<tbody>\
				</tbody>\
			</table>'
                );
    };
 
    var insertUsersForm = function (parent) {
        parent.append(
                '<form id="' + formId + '" class="mb-5 mb-10">\
				<div class="row">\
                                        <div class="col-sm-3">\
						<input name="login" type="text" value="" placeholder="Login" class="form-control" readonly/>\
					</div>\
                                         <div class="col-sm-3">\
						<input name="password" type="password" value="" placeholder="contraseña" class="form-control" required/>\
					</div>\
					<div class="col-sm-3">\
						<input name="role" value="" placeholder="Rol" class="form-control" required/>\
					</div>\
					<div class="col-sm-3">\
						<input id="btnSubmit" type="submit" value="Editar" class="btn btn-primary" style="display:none"/>\
						<input id="btnClear" type="reset" value="Limpiar" class="btn" />\
					</div>\
				</div>\
			</form>'
                );
    };

    var createUserRow = function (user) {
        return '<tr id="user-' + user.login + '" class="row">\
                        <td class="login col-sm-3">' + user.login + '</td>\
                        <td class="password col-sm-3" hidden>' + user.password + '</td>\
			<td class="role col-sm-3">' + user.role + '</td>\
			<td class="col-sm-3">\
				<a class="edit btn btn-primary" href="#">Editar</a>\
			</td>\
		</tr>';
    };

    var showErrorMessage = function (jqxhr, textStatus, error) {
        alert(textStatus + ": " + error);
    };

    var addRowListeners = function (user) {
        $('#user-' + user.login + ' a.edit').click(function () {
            self.editUser(user.login);
        });

        $('#user-' + user.login + ' a.delete').click(function () {
            self.deleteUser(user.login);
        });
    };

    var appendToTable = function (user) {
        $(listQuery + ' > tbody:last')
                .append(createUserRow(user));
        addRowListeners(user);
    };

    return UsersView;
})();
