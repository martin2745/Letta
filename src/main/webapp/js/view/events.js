let EventsView = (function () {
    let dao;
    const peopleDao = new PeopleDAO();
    // Referencia a this que permite acceder a las funciones públicas desde las funciones de jQuery.
    let self;
    let formId = 'events-form';
    const gridId = 'events-grid';
    let formQuery = '#' + formId;
    const gridQuery = '#' + gridId;
    let person_id;

    function EventsView(eventDao, formContainerId, gridCardContainerId) {
        dao = eventDao;
        let login = getCookie("login");
        console.log("login: " + login);
        self = this;

        insertEventForm($('#' + formContainerId));
        insertGridCardEvents($('#' + gridCardContainerId))


        this.init = function () {
            peopleDao.getId(login, function (person) {
                console.log("login: " + login);
                person_id = person.id;
                console.log("login id: " + person_id);
                dao.listEvent(person_id, function (events) {
                        $.each(events, function (key, event) {
                            appendToTable(event);
                        });
                    },
                    function () {
                        alert('No has sido posible acceder al listado de eventos.');
                    });
            });


            // La acción por defecto de enviar formulario (submit) se sobreescribe
            // para que el envío sea a través de AJAX
            $(formQuery).submit(function (event) {
                var event = self.getEventInForm();

                if (self.isEditing()) {
                    dao.modifyEvent(event,
                        function (event) {
                            $('#event-' + event.id + ' td.ubicacion').text(event.ubicacion);
                            $('#event-' + event.id + ' td.nombre').text(event.nombre);
                            $('#event-' + event.id + ' td.descripcion').text(event.descripcion);
                            $('#event-' + event.id + ' td.date').text(event.date);
                            self.resetForm();
                        },
                        showErrorMessage,
                        self.enableForm
                    );
                } else {
                    dao.addEvent(event,
                        function (event) {
                            appendToTable(event);
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

        this.getEventInForm = function () {
            var form = $(formQuery);
            return {

                'image' : URL.createObjectURL(form.find('input[name="formFile"]').files[0]) ,
                'id': form.find('input[name="id"]').val(),
                'ubicacion': form.find('input[name="ubicacion"]').val(),
                'nombre': form.find('input[name="nombre"]').val(),
                'descripcion': form.find('input[name="descripcion"]').val(),
                'date': form.find('input[name="date"]').val(),
                'id_persona': person_id
            };
        };

        this.editEvent = function (id) {
            var row = $('#event-' + id);

            if (row !== undefined) {
                var form = $(formQuery);

                form.find('input[name="id"]').val(id);
                form.find('input[name="ubicacion"]').val(row.find('p.location').text());
                form.find('input[name="nombre"]').val(row.find('h5.card-title').text());
                form.find('input[name="descripcion"]').val(row.find('p.description').text());
                form.find('input[name="date"]').val(row.find('p.date').text());

                $('input#btnSubmit').val('Modificar');
            }
        };

        this.deleteEvent = function (id) {
            if (confirm('Está a punto de eliminar un evento. ¿Está seguro de que desea continuar?')) {
                dao.deleteEvent(id,
                    function () {
                        $('tr#event-' + id).remove();
                        window.location.reload(true);
                    },
                    showErrorMessage
                );
            }
        };

        this.isEditing = function () {
            return $(formQuery + ' input[name="id"]').val() != "";
        };

        this.disableForm = function () {
            $(formQuery + ' input').prop('disabled', true);
        };

        this.enableForm = function () {
            $(formQuery + ' input').prop('disabled', false);
        };

        this.resetForm = function () {
            $(formQuery)[0].reset();
            $(formQuery + ' input[name="id"]').val('');
            $('#btnSubmit').val('Crear');
        };
    };


    var insertEventForm = function (parent) {
        parent.append(
            '<h1 class="display-5 mt-3 mb-3">Events Management</h1>' +
            '<form id="' + formId + '" class="mb-5 mb-10">\
				<input name="id" type="hidden" value=""/>\
				<div class="row">\
					<div class="col-sm-2">\
                        <label for="formFile" class="form-label">Image</label>\
                        <input name="formFile" class="form-control"  type="file" accept="image/*" id="formFile">\
                    </div>\
					<div class="col-sm-3">\
						<input name="ubicacion" type="text" value="" placeholder="Ubicación" class="form-control" required/>\
					</div>\
					<div class="col-sm-2">\
						<input name="nombre" type="text" value="" placeholder="Nombre" class="form-control" required/>\
					</div>\
					<div class="col-sm-3">\
						<input name="descripcion" type="text" value="" placeholder="Descripción" class="form-control" required/>\
					</div>\
					<div class="col-sm-2">\
						<input name="date" type="text" value="" placeholder="Fecha" class="form-control" required/>\
					</div>\
					<div class="col-sm-2">\
						<input id="btnSubmit" type="submit" value="Crear" class="btn btn-primary" />\
						<input id="btnClear" type="reset" value="Limpiar" class="btn" />\
					</div>\
				</div>\
			</form>'
        );
    };

    var showErrorMessage = function (jqxhr, textStatus, error) {
        alert(textStatus + ": " + error);
    };

    var addRowListeners = function (event) {
        $('#event-' + event.id + ' a.edit').click(function () {
            self.editEvent(event.id);
        });

        $('#event-' + event.id + ' a.delete').click(function () {
            self.deleteEvent(event.id);
        });
    };

    var appendToTable = function (event) {
        $(gridQuery)
            .append(createEventCard(event));
        addRowListeners(event);
    };

    var insertGridCardEvents = function (parent) {
        parent.append(
            '<div class="main-grid" id="' + gridId + '">' +
            '</div>'
        );
    };

    var createEventCard = function (event) {
        return '<div id="event-' + event.id + '" class="card">' +
            '  <img class="card-img-top" src="" alt="Card image cap">\n' +
            '  <div class="card-body">\n' +
            '    <h5 class="card-title">' + event.nombre + '</h5>\n' +
            '    <p class="card-text location">' + event.ubicacion + '</p>\n' +
            '    <p class="card-text date">' + event.date + '</p>\n' +
            '    <p class="card-text description">' + event.descripcion + '</p>\n' +
            '    <a class="edit btn btn-info" href="#">Editar</a>' +
            '    <a class="delete btn btn-warning" href="#">Eliminar</a>' +
            '  </div>' +
            '</div>'

    }


    return EventsView;
})();
