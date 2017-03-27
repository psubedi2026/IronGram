package com.theironyard.novauc.controllers;

import com.theironyard.novauc.entities.Photo;
import com.theironyard.novauc.entities.User;
import com.theironyard.novauc.services.PhotoRepository;
import com.theironyard.novauc.services.UserRepository;
import com.theironyard.novauc.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.h2.tools.Server;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by psubedi2020 on 3/21/17.
 */
@RestController
public class IronGramController {

    public static Integer sleepTimer;
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
        } else if (!PasswordStorage.verifyPassword(password, user.getPassword())) {
            throw new Exception("Wrong password");
        }
        session.setAttribute("username", username);
        response.sendRedirect("/");
        return user;
    }
//kndfkfbsnj
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
            Integer timer,
            MultipartFile photo,
            String isPublic
    ) throws Exception {
        sleepTimer = timer;
        String username = (String) session.getAttribute("username");
        User senderUser = users.findFirstByName(username);
        User receiverUser = users.findFirstByName(receiver);

        validateUser(username, receiverUser);
        Photo p = savePhoto(photo, isPublic, senderUser, receiverUser);

        response.sendRedirect("/");
        return p;

    }

    private void validateUser(String username, User receiverUser) throws Exception {
        if (username == null) {
            throw new Exception("Not logged in.");
        }

        if (receiverUser == null) {
            throw new Exception("Receiver name doesn't exist.");
        }
    }


    @RequestMapping("/photos")
    public List<Photo> showPhotos(HttpSession session, HttpServletResponse response) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in.");
        }

        User user = users.findFirstByName(username);
        List<Photo> publicPhotos = photos.findByIsPublic("noNull");
        List<Photo> receiverPhotos = photos.findByReceiver(user);

        List<Photo> listAll = new ArrayList<>(publicPhotos);
        listAll.addAll(receiverPhotos);

        return listAll;
    }

    @RequestMapping(path = "/delete")
    public void delete(HttpSession session) throws InterruptedException, IOException {
        String userName = (String) session.getAttribute("userName");
        Thread.sleep(sleepTimer * 1000);
        deleteFiles(userName);
    }

    private void deleteFiles(String userName) throws IOException {
        User receiverUser = users.findFirstByName(userName);
        List<Photo> pList = photos.findByReceiver(receiverUser);
        for (Photo p : pList) {
            String fileName = p.getFilename();
            Path filePath = Paths.get("public/", fileName);
            Files.delete(filePath);
            photos.delete(p);
        }
    }

    @RequestMapping(path = "/public-photos/{userName}", method = RequestMethod.GET)
    public List<Photo> getPublicPhotos(HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        User user = users.findFirstByName(userName);
        List<Photo> publicPhoto = photos.findByReceiver(user);
        return publicPhoto;
    }

    private Photo savePhoto(MultipartFile photo, String isPublic, User senderUser, User receiverUser) throws Exception {
        if (!photo.getContentType().startsWith("image")) {
            throw new Exception("not an image file");

        }

        File photoFile = File.createTempFile("photo", photo.getOriginalFilename(), new File("public"));
        FileOutputStream fos = new FileOutputStream(photoFile);
        fos.write(photo.getBytes());

        Photo p = new Photo();
        p.setSender(senderUser);
        p.setReceiver(receiverUser);
        p.setFilename(photoFile.getName());
        p.setIsPublic(isPublic);
        photos.save(p);

        return p;
    }
}




