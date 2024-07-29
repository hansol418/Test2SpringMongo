package com.mymongotest.springmongtest3.service;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {
	
    private final MongoTemplate mongoTemplate;
    
    private final GridFsTemplate gridFsTemplate;
    
    //파일이름 모두 검색
    public List<String> findAllFilenames() {
        GridFSFindIterable files = gridFsTemplate.find(new Query());
        List<String> filenames = new ArrayList<>();
        for (GridFSFile file : files) {
            filenames.add(file.getFilename());
        }
//        System.out.println(filenames);
        return filenames;
    }

    //파일이름 하나 검색
//    public String findOneFilename(String filename) {
//        Query query = Query.query(Criteria.where("filename").is(filename));
//        List<String> filenames = new ArrayList<>();
//        for (GridFSFile file : files) {
//            filenames.add(file.getFilename());
//        }
////        System.out.println(filenames);
//        return filenames;
//    }
    

 // 삭제 파일명으로 삭제
    public void deleteImage(String filename) {
    	   Query query = Query.query(Criteria.where("filename").is(filename));
    	    gridFsTemplate.delete(query);
    }

    //삭제, ObjectId로 삭제
    public void deleteImageByObjectId(String objectId) {
        Query query = Query.query(Criteria.where("_id").is(objectId));
        gridFsTemplate.delete(query);
    }

}