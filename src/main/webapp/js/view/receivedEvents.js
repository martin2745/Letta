var ReceivedEventsView = (function () {
    let dao;
    const pDAO = new PeopleDAO();

    let self;
    const formId = 'events-form';
    const gridId = 'events-grid';
    const formQuery = '#' + formId;
    const gridQuery = '#' + gridId;
    let roleFlag = "USER";
    let person_id;

    function ReceivedEventsView(eventDao, formContainerId, gridCardContainerId) {
        dao = eventDao;

        self = this;
        let login = getCookie("login");
        console.log("login: " + login);


        searchEventForm($('#' + formContainerId));
        insertGridCardEvents($('#' + gridCardContainerId))

        this.init = function () {

            pDAO.getRole(login, function (person) {
                console.log("Role: " + person.role);
                roleFlag = person.role;
                console.log("Person ID:" + person.id);
                person_id = person.id;
                dao.listReceivedEvents(person.id, function (events) {
                        $.each(events, function (key, event) {
                            appendToTable(event);
                        });
                    },
                    function () {
                        alert('No has sido posible acceder al listado de eventos.');
                        showErrorMessage();
                    });
            }, function (e) {
                console.log("getRole is failing...: " + e);
            });


            $(formQuery).submit(function (search) {
                console.log("submitting search");
                console.log('search events...');
                var search = self.getEventInFormBuscar();
                console.log(search);
                dao.searchEvent(search, function (events) {
                    console.log(events);
                    $(gridQuery).empty();
                    $.each(events, function (key, event) {
                        console.log(event);
                        appendToTable(event);
                    });
                }, function () {
                    showErrorMessage();
                })
                return false;
            });

        };
        this.getEventInFormBuscar = function () {
            var form = $(formQuery);
            return {
                'ubicacion': form.find('input[name=ubicacion]').val(),
                'nombre': form.find('input[name=nombre]').val(),
                'descripcion': form.find('input[name=descripcion]').val(),
                'date': form.find('input[name=date]').val()
            };
        };

        this.subscribeEvent = function (event_id, person_id) {
            console.log(event_id);
            console.log(person_id);
            dao.subscribeEvent(event_id, person_id, function (event) {
                console.log(event);
                window.location.reload(true);
            });
        }
        this.declineEvent = function (event_id, person_id) {
            console.log(event_id);
            console.log(person_id);
            dao.declineSendEvent(event_id, person_id, function (event) {
                console.log(event);
                window.location.reload(true);
            });
        }

    }

    var searchEventForm = function (parent) {
        parent.append(
            '<h1 class="display-5 mt-3 mb-3">Received Events!</h1>' +
            '<form id="' + formId + '" class="mb-5 mb-10">\
				<div class="row">\
					<div class="col-sm-3">\
						<input name="ubicacion" type="text" value="" placeholder="Ubicación" class="form-control"/>\
					</div>\
					<div class="col-sm-2">\
						<input name="nombre" type="text" value="" placeholder="Nombre" class="form-control"/>\
					</div>\
					<div class="col-sm-3">\
						<input name="descripcion" type="text" value="" placeholder="Descripción" class="form-control"/>\
					</div>\
					<div class="col-sm-2">\
						<input name="date" type="text" value="" placeholder="Fecha" class="form-control"/>\
					</div>\
					<div class="col-sm-2">\
						<input id="btnSubmitBuscar" type="submit" value="Buscar" class="btn btn-info" />\
					</div>\
				</div>\
			</form>\
			<hr id="divider-form">'
        );
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
            '    <p class="card-text">' + event.ubicacion + '</p>\n' +
            '    <p class="card-text">' + event.date + '</p>\n' +
            '    <p class="card-text">' + event.descripcion + '</p>\n' +
            '    <a class="sub btn btn-info" href="#">Aceptar</a>' +
            '    <a class="decline btn btn-info" href="#">Rechazar</a>' +
            '  </div>' +
            '</div>'
    }

    var showErrorMessage = function (jqxhr, textStatus, error) {
        alert(textStatus + ": " + error);
    };

    var addRowListeners = function (event) {
        $('#event-' + event.id + ' a.sub').click(function () {
            self.subscribeEvent(event.id, person_id);
        });
        $('#event-' + event.id + ' a.decline').click(function () {
            self.declineEvent(event.id, person_id);
        });
    };

    var appendToTable = function (event) {
        $(gridQuery)
            .append(createEventCard(event))
        addRowListeners(event);
    };

    return ReceivedEventsView;
})();


