package com.theironyard.novauc.controllers;

import com.sun.javafx.binding.StringFormatter;
import com.theironyard.novauc.entities.Photo;
import com.theironyard.novauc.entities.User;
import com.theironyard.novauc.services.PhotoRepository;
import com.theironyard.novauc.services.UserRepository;
import com.theironyard.novauc.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//import org.springframework.messaging.handler.annotation;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.h2.tools.Server;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;


/**
 * Created by psubedi2020 on 3/21/17.
 */
@RestController
public class IronGramController {

    @Autowired
    UserRepository users;

    @Autowired
    PhotoRepository photos;

    Server dbui = null;

    @PostConstruct
    public void init() throws SQLException {
        dbui = Server.createWebServer().start();
    }

    @PreDestroy
    public void destroy() {
        dbui.stop();
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User login(String username, String password, HttpSession session, HttpServletResponse response) throws Exception {
        User user = users.findFirstByName(username);
        if (user == null) {
            user = new User(username, PasswordStorage.createHash(password));
            users.save(user);
        }
        else if (!PasswordStorage.verifyPassword(password, user.getPassword())) {
            throw new Exception("Wrong password");
        }
        session.setAttribute("username", username);
        response.sendRedirect("/");
        return user;
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.invalidate();
        response.sendRedirect("/");
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public User getUser(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return users.findFirstByName(username);
    }


    @RequestMapping("/upload")
    public Photo upload(
            HttpSession session,
            HttpServletResponse response,
            String receiver,
            MultipartFile photo,
            int delay,
            boolean isPublic
    ) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in.");
        }

        User senderUser = users.findFirstByName(username);
        User receiverUser = users.findFirstByName(receiver);

        if (receiverUser == null) {
            throw new Exception("Receiver name doesn't exist.");
        }

        if (!photo.getContentType().startsWith("image")) {
            throw new Exception("Only images are allowed.");
        }

        File photoFile = File.createTempFile("photo", photo.getOriginalFilename(), new File("public"));
        FileOutputStream fos = new FileOutputStream(photoFile);
        fos.write(photo.getBytes());

        Photo p = new Photo();
        p.setSender(senderUser);
        p.setRecipient(receiverUser);
        p.setFilename(photoFile.getName());
        p.setDelay(delay);
        p.setPublicPhoto(isPublic);
        photos.save(p);

        response.sendRedirect("/");

        return p;
    }


    @RequestMapping("/photos")
    public List<Photo> showPhotos(HttpSession session) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in.");
        }

        User user = users.findFirstByName(username);
        return  photos.findByRecipient(user);
    }


    @RequestMapping("/photosdisplay/{filename}")
    public void showPhoto(HttpSession session, HttpServletResponse response,@PathVariable String filename) throws Exception {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in.");
        }
        String realFileName=String.format("%s.jpg",filename);
        Photo thisPhoto = photos.findFirstByFilename(realFileName);
        session.setAttribute("thisPhoto", thisPhoto);
        response.sendRedirect("/");
    }

    @RequestMapping("/sessionPhoto")
    public Photo realShow(HttpSession session){
        return (Photo)session.getAttribute("thisPhoto");
    }

    @RequestMapping("/delete")
    public void autoDelete(HttpSession session, HttpServletResponse response)throws Exception{
        Photo thisPhoto=(Photo) session.getAttribute("thisPhoto");
        int delay = thisPhoto.getDelay()*1000;
        Thread.sleep(delay);
        String actualPath= String.format("/Users/souporman/Code/IronGram/public/%s",thisPhoto.getFilename());
        Path pathToPhoto = Paths.get(actualPath);
        Files.delete(pathToPhoto);
        photos.delete(thisPhoto.getId());
        response.reset();
    }
//
//    @RequestMapping("/reload")
//    public void reloader()throws Exception{
//        Thread.sleep(10000);
//    }
}
