//css a pelo, está para cambiar la disposición de elementos del perfil y que este se vea algo mejor
//se invoca a la función varias veces porque si se intentan poner varias líneas css juntas solo lee la primera
const addCSS = css => document.head.appendChild(document.createElement("style")).innerHTML=css;
addCSS(
    "#div0{ display:inline }"
);
addCSS(
    "#div1{ display:inline }"
);
addCSS(
    "#div2{ display:inline }"
);
addCSS(
    "#pre1{ display:inline }"
);

let ProfileView = (function() {

	let dao;
	let self;

	function ProfileView(peopleDAO, listContainerId){
		dao = peopleDAO;
		self = this;

        insertProfileList($('#' + listContainerId));

		this.init = function(){
			dao.getData(getCookie("login"), function(person){
            changeData(person);
		})

		};

	}

    function changeData(person){
        document.getElementById("div0").innerHTML = person.login;
        document.getElementById("div1").innerHTML = person.name;
        document.getElementById("div2").innerHTML = person.surname;
    }
    
	let insertProfileList = function(parent) {
		parent.append(
            '<h1 class="display-5 mt-3 mb-3">Profile</h1>' +
            '<div id = divPerfil>\
                <h5 id = 0>Nombre de usuario: <div id = div0> u </div></h5>\
                <pre id = pre1>Nombre: <div id = div1> n </div>        Apellido: <div id = div2> a </div></pre>\
            </div>'
		);
	};

	return ProfileView;
})();