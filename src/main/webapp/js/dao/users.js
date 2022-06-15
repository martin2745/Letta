var UserDAO = (function() {
    var resourcePath = "rest/users/";
    var requestByAjax = function(data, done, fail, always) {
	done = typeof done !== 'undefined' ? done : function() {};
	fail = typeof fail !== 'undefined' ? fail : function() {};
	always = typeof always !== 'undefined' ? always : function() {};

	let authToken = localStorage.getItem('authorization-token');
	if (authToken !== null) {
	    data.beforeSend = function(xhr) {
		xhr.setRequestHeader('Authorization', 'Basic ' + authToken);
	    };
	}

	$.ajax(data).done(done).fail(fail).always(always);
    };

    function UserDAO() {
	this.listUsers = function(done, fail, always) {
	    requestByAjax({             
		url : resourcePath,
		type : 'GET'
	    }, done, fail, always);
	};

	this.addUser = function(user, done, fail, always) {
	    requestByAjax({
		url : resourcePath,
		type : 'POST',
		data : user
	    }, done, fail, always);
	};

	
	this.modifyUser = function(user, done, fail, always) {
	    requestByAjax({
		url : resourcePath + user.login,
		type : 'PUT',
		data : user
	    }, done, fail, always);
	};

	this.deleteUser = function(login, done, fail, always) {
	    requestByAjax({
		url : resourcePath + login,
		type : 'DELETE',
	    }, done, fail, always);
	};

//	this.getId = function(login, done, fail, always) {
//	    requestByAjax({
//		url : "rest/users/recuperarId/" + login,
//		type : 'GET'
//	    }, done, fail, always);
//	}; 

    }

    return UserDAO;
})();
