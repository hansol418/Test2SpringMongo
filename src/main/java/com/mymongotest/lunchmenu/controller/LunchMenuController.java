package com.mymongotest.lunchmenu.controller;

import com.mymongotest.lunchmenu.document.LunchMenu;
import com.mymongotest.lunchmenu.service.LunchMenuService;
import com.mymongotest.springmongtest3.DTO.SearchDB;
import com.mymongotest.springmongtest3.document.Memo;
import com.mymongotest.springmongtest3.document.User2;
import com.mymongotest.springmongtest3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LunchMenuController {

    private final LunchMenuService lunchMenuService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "loginForm";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "loginForm";
    }

    @ResponseBody
    @PostMapping("/insertLunchMenu")
    public ResponseEntity<String> insertLunchMenu(@RequestBody LunchMenu lunchMenu){
        lunchMenuService.insertLunchMenu(lunchMenu);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/insertMemoWithImage")
    public ResponseEntity<String> insertMemoWithImage(@RequestPart(value = "key") Memo memo,
                                                      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        if (file != null) {
            String filename = file.getOriginalFilename();
            String str = filename.substring(filename.lastIndexOf(".") + 1);
            InputStream inputStream = file.getInputStream();

            if (!str.equals("mp4") && !str.equals("mov") && !str.equals("MOV") && !str.equals("avi") && !str.equals("wmv")) {
                BufferedImage bo_img = ImageIO.read(inputStream);
                int newWidth = 200;
                int newHeight = 200;
                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = resizedImage.createGraphics();
                graphics2D.drawImage(bo_img, 0, 0, newWidth, newHeight, null);
                graphics2D.dispose();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", outputStream);
                InputStream reSizeInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                ObjectId objectId = gridFsTemplate.store(reSizeInputStream, file.getOriginalFilename(), file.getContentType());
                memo.setImageFileObjectId(objectId.toString());
                memo.setImageFileName(file.getOriginalFilename());
            } else {
                ObjectId objectId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType());
                memo.setImageFileObjectId(objectId.toString());
                memo.setImageFileName(file.getOriginalFilename());
            }
        }

        lunchMenuService.mongoMemoInsert(memo);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/insertMemo")
    public ResponseEntity<String> insertMemo(@RequestBody Memo memo){
        lunchMenuService.mongoMemoInsert(memo);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @RequestMapping("/joinForm")
    public String joinForm(Model model) {
        model.addAttribute("User2", new User2());
        return "joinForm";
    }

    @PostMapping("/joinUser")
    public String joinUser(User2 user) {
        String email = user.getEmail();
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        if (lunchMenuService.mongoFindOneUser2Email(email) == null) {
            lunchMenuService.mongoUser2Insert(user);
            return "redirect:/";
        }
        return "/joinForm";
    }

    @ResponseBody
    @PostMapping("/updateMemo")
    public ResponseEntity<String> updateMemo(@RequestBody Memo memo) {
        lunchMenuService.mongoMemoUpdate(memo);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/updateWithMemo")
    public ResponseEntity<String> updateWithMemo(@RequestPart(value = "key") Memo memo,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        ObjectId objectId2 = new ObjectId(memo.getId());
        Memo loadMemo = lunchMenuService.mongoFindOneMemo(objectId2);

        if (file != null) {
            String filename = file.getOriginalFilename();
            imageService.deleteImage(loadMemo.getImageFileName());
            String str = filename.substring(filename.lastIndexOf(".") + 1);

            if (!str.equals("mp4") && !str.equals("mov") && !str.equals("MOV") && !str.equals("avi") && !str.equals("wmv")) {
                InputStream inputStream = file.getInputStream();
                BufferedImage bo_img = ImageIO.read(inputStream);
                int newWidth = 200;
                int newHeight = 200;
                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = resizedImage.createGraphics();
                graphics2D.drawImage(bo_img, 0, 0, newWidth, newHeight, null);
                graphics2D.dispose();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", outputStream);
                InputStream reSizeInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                ObjectId objectId = gridFsTemplate.store(reSizeInputStream, file.getOriginalFilename(), file.getContentType());
                memo.setImageFileObjectId(objectId.toString());
                memo.setImageFileName(file.getOriginalFilename());
            } else {
                InputStream inputStream = file.getInputStream();
                ObjectId objectId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType());
                memo.setImageFileObjectId(objectId.toString());
                memo.setImageFileName(file.getOriginalFilename());
            }
        } else {
            memo.setImageFileName(loadMemo.getImageFileName());
        }

        lunchMenuService.mongoMemoUpdate(memo);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/findAllMemo")
    public List<Memo> listMemo() {
        return lunchMenuService.mongoFindAllMemo();
    }

    @ResponseBody
    @PostMapping("/searchDb")
    public List<Memo> searchlist(@RequestBody SearchDB searchDB) {
        return lunchMenuService.mongoSearchFindAll(searchDB);
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        List<Memo> memoList = lunchMenuService.mongoFindAllMemo();
        int count = memoList.size();
        model.addAttribute("count", count);
        return "admin";
    }

    @RequestMapping("/")
    public String main(Model model) {
        return "main";
    }

    @RequestMapping("/updateFormMemo/{id}")
    public String updateFormMemo(Model model, @PathVariable String id) {
        ObjectId objectId = new ObjectId(id);
        Memo memo = lunchMenuService.mongoFindOneMemo(objectId);
        model.addAttribute("memo", memo);
        return "updateForm";
    }

    @ResponseBody
    @DeleteMapping("/dbDelete/{id}/{imageFileName}")
    public String delete(@PathVariable String id, @PathVariable String imageFileName) {
        lunchMenuService.deleteDb("_id", id);
        imageService.deleteImage(imageFileName);
        return id;
    }
}
