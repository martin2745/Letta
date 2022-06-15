var EventDAO = (function () {
    var resourcePath = "rest/events/";
    var requestByAjax = function (data, done, fail, always) {
        done = typeof done !== 'undefined' ? done : function () {
        };
        fail = typeof fail !== 'undefined' ? fail : function () {
        };
        always = typeof always !== 'undefined' ? always : function () {
        };

        let authToken = localStorage.getItem('authorization-token');
        if (authToken !== null) {
            data.beforeSend = function (xhr) {
                xhr.setRequestHeader('Authorization', 'Basic ' + authToken);
            };
        }

        $.ajax(data).done(done).fail(fail).always(always);
    };

    function EventDAO() {
        this.listEvent = function (id_persona, done, fail, always) {
            requestByAjax({
                url: resourcePath + 'person/' + id_persona,
                type: 'GET'
            }, done, fail, always);
        };

        this.addEvent = function (event, done, fail, always) {
            requestByAjax({
                url: resourcePath,
                type: 'POST',
                data: event
            }, done, fail, always);
        };

        this.addSendEvent = function (sendEvent, done, fail, always) {
            requestByAjax({
                url: "rest/sendEvents/",
                type: 'POST',
                data: sendEvent
            }, done, fail, always);
        };

        this.declineSendEvent = function (event_id,person_id, done, fail, always) {
            requestByAjax({
                url: "rest/sendEvents/" + event_id + "&" + person_id,
                type: 'DELETE',
            }, done, fail, always);
        };

        this.searchEvent = function (event, done, fail, always) {
            requestByAjax({
                url: resourcePath + 'searchEvents/',
                type: 'POST',
                data: event
            }, done, fail, always);
        };

        this.modifyEvent = function (event, done, fail, always) {
            requestByAjax({
                url: resourcePath + event.id,
                type: 'PUT',
                data: event
            }, done, fail, always);
        };

        this.deleteEvent = function (id, done, fail, always) {
            requestByAjax({
                url: resourcePath + id,
                type: 'DELETE',
            }, done, fail, always);
        };

        this.listEventSubs = function (person_id, done, fail, always) {
            requestByAjax({
                url: resourcePath + 'listEvents/' + person_id,
                type: 'GET'
            }, done, fail, always);
        };

        this.listEventSubscribed = function (person_id, done, fail, always) {
            requestByAjax({
                url: resourcePath + 'listSubsEvents/' + person_id,
                type: 'GET'
            }, done, fail, always);
        };

        this.getSendEvent = function (rrpp_id, event_id, destiny_id, done, fail, always) {
            requestByAjax({
                url: "rest/sendEvents/" + rrpp_id + "&" + event_id + "&" + destiny_id,
                type: 'GET'
            }, done, fail, always);
        };

        this.listReceivedEvents = function (destiny_id, done, fail, always) {
            requestByAjax({
                url: "rest/sendEvents/" + destiny_id,
                type: 'GET'
            }, done, fail, always);
        };

        this.subscribeEvent = function (event_id, person_id, done, fail, always) {
            requestByAjax({
                url: resourcePath + 'person/' + 'subscribe/' + person_id + '/' + event_id,
                type: 'GET',
            }, done, fail, always);
        }

        this.unSubscribeEvent = function (event_id, person_id, done, fail, always) {
            requestByAjax({
                url: resourcePath + 'person/' + 'unsubscribe/' + person_id + '/' + event_id,
                type: 'GET',
            }, done, fail, always);
        }

    }

    return EventDAO;
})();