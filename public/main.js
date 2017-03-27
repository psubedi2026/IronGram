
function getUser(userData) {

    if (userData.length === 0) {
        $("#login").show();
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

//        var seconds_left = photo.time;
//        var interval = setInterval(function () {
//        document.getElementById('timer_div').innerHTML = --seconds_left;
//        if (seconds_left <= 0) {
//        $("#photos").empty();
//        clearInterval(interval);
//        $.get("/delete-photos");
//        }
//        } ,1000},

        $("#photos").append(elem);
    }
}

$.get("/photos", getPhotos);