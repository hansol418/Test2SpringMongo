package com.mymongotest.springmongtest3.controller;

import com.mongodb.client.gridfs.model.GridFSFile;

import com.mymongotest.springmongtest3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
	
	private final ImageService imageService;

    @Autowired
    private GridFsTemplate gridFsTemplate;
    
    @Autowired
    private GridFsOperations gridFsOperations;

    // 파일이미지 등록.
    // 뷰에서 선택된 파일이름으로 등록.
    @PostMapping
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ObjectId objectId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType());
        return new ResponseEntity<>( HttpStatus.OK);
    }
    
    // 파일이름에 해당하는 이미지 또는 동영상, 몽고 디비에서 조회 후 존재하면 
    // 파일을 가지고 와서 바이트로 변환해서 
    // 뷰에 JSON으로 응답.
    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String id) throws IOException {
    	GridFSFile gridFSFile = 	gridFsOperations.findOne(new Query(Criteria.where("filename").is(id)));
    	
        if (gridFSFile != null) {
            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
            byte[] bytes = IOUtils.toByteArray(resource.getInputStream());
            return ResponseEntity.ok().body(bytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //하나 파일이름 불러오기
//    @ResponseBody
//    @GetMapping("/findFileNameOne")
//    public String findOneFilenames( ){
//        String FileName =  imageService.findOneFilename();
//        return FileName;
//    }

//전체 파일이름 불러오기
	@ResponseBody
	@GetMapping("/findFileNameAll")
	public List<String> findAllFilenames( ){
		List<String> FileNameList =  imageService.findAllFilenames();
		return FileNameList;
	}
    
	  //파일 삭제  
    @ResponseBody
  		@DeleteMapping("/deleteImage/{filename}")
  		public String deleteImage(@PathVariable String filename) {
    	imageService.deleteImage(filename);
  			return filename;
  	  
  }
    
    
}
