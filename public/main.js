
function getUser(userData) {

    if (userData.length == 0) {
        $("#login").show();
        $("#upload").hide();
    }  else {
        $("#logout").show();
        $("#upload").show();
    }
}

$.get("/user", getUser);



function getPhotos(photosData) {
    for (var i in photosData) {
        var photo = photosData[i];
        var elem = $("<img>");
        elem.attr("src", photo.filename);
        $("#photos").append(elem);
    }
}

$.get("/photos", getPhotos);