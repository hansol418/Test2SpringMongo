package com.mymongotest.springmongtest3.service;


import com.mymongotest.springmongtest3.DTO.SearchDB;
import com.mymongotest.springmongtest3.document.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LunchMenuService implements UserDetailsService{

    private final MongoTemplate mongoTemplate;
    
    private final GridFsTemplate gridFsTemplate;

// 샘플 하나 추가. 
    public void mongoInsert() {
        LunchMenu lunchmenu1 = new LunchMenu(3L, "제목2", "메세지2");
        mongoTemplate.insert(lunchmenu1);
    }
    
    // Convert Date to String
    public String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    // Convert String to Date
    public Date stringToDate(String dateString) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateString);
    }
    
//하나 추가. 
//	private Long id;
//    private String title;
//    private String message;
    public void mongoLunchMenuInsert(LunchMenu lunchMenu) {
        mongoTemplate.insert(lunchMenu);
    }
    
  // 메모 하나 추가. 
//	@Id
//	private ObjectId id;
//	
//	private String title;
//	private String message;
    public void mongoMemoInsert(Memo memo) {
    	Date date = new Date();
    	String converDate = dateToString(date);
    	memo.setDateField(converDate);
        mongoTemplate.insert(memo);
    }
    
  //하나 추가. 
    
    public void mongoLunchMenu2Insert(LunchMenu2 lunchMenu) {
        mongoTemplate.insert(lunchMenu);
    }
    
    
//전체 검색
    public List<LunchMenu> mongoFindAll() {
    	Query query = new Query();
    	query.with(Sort.by(Sort.Direction.DESC, "id"));

     	List<LunchMenu> lunchMenuList=mongoTemplate.find(query,LunchMenu.class);
		return lunchMenuList;
        
    }
    //전체 검색
    public List<Memo> mongoFindAllMemo() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "id"));

        List<Memo> memoList=mongoTemplate.find(query,Memo.class);
        return memoList;

    }
    
 //조건 검색
    public List<Memo> mongoSearchFindAll(SearchDB searchDB) {
//    	System.out.println("서비스 searchDB.getSearchDB(): "+searchDB.getSearchDB());
//    	System.out.println("서비스 searchDB.getSearchContent(): "+searchDB.getSearchContent());
    	List<Memo> memoList = null;
    	if(searchDB.getSearchDB().equals("_id")) {
    		Criteria criteria = new Criteria("_id");
    		criteria.is(Long.parseLong(searchDB.getSearchContent()));
    		
    		//기존 1:1 검색
    		Query query = new Query(criteria);
            memoList=mongoTemplate.find(query, Memo.class);
    	} else if( searchDB.getSearchDB().equals("title")) {
    		
    		//like 검색. 
    		Query searchQuery = new Query();
    		 
    		// LIKE '%[searchIndexInfoSearchParam.getTitleMain()]%' 와 같음
    		searchQuery.addCriteria(Criteria.where("title").regex(searchDB.getSearchContent()));
            memoList=mongoTemplate.find(searchQuery, Memo.class);
    		
    	} else if( searchDB.getSearchDB().equals("message")) {
    		//like 검색. 
    		Query searchQuery = new Query();
    		 
    		// LIKE '%[searchIndexInfoSearchParam.getTitleMain()]%' 와 같음
    		searchQuery.addCriteria(Criteria.where("message").regex(searchDB.getSearchContent()));    
    		memoList=mongoTemplate.find(searchQuery, Memo.class);
    	}
		return memoList;
        
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//    	  System.out.println("로그인 하나요?");
        LunchMenu2 lunchMenu2 = mongoFindOneLunchMenu2Email(email);

        if(lunchMenu2 == null){
            throw new UsernameNotFoundException(email);
        }
        

        return User.builder()
                .username(lunchMenu2.getEmail())
                .password(lunchMenu2.getPassword())
                .roles(lunchMenu2.getRole())
                .build();
    }
    
  //유저 찾기
    public LunchMenu2 mongoFindOneLunchMenu2Email(String email) {
    	Criteria criteria = new Criteria("email");
		criteria.is(email);
		
		//기존 1:1 검색
		Query query = new Query(criteria);
        LunchMenu2 lunchMenu2 = mongoTemplate.findOne(query, LunchMenu2.class);
		return lunchMenu2;
    }
    
//하나 찾기
    public LunchMenu mongoFindOne(Long id) {
        LunchMenu lunchMenu = mongoTemplate.findById(id, LunchMenu.class);
		return lunchMenu;
    }

    public Memo mongoFindOneMemo(ObjectId id) {
        Memo memo = mongoTemplate.findById(id, Memo.class);
        return memo;
    }
 //하나 수정하기.
public void mongoUserUpdate(LunchMenu lunchMenu) {
	Query query = new Query();
    Update update = new Update();

    // where절 조건
    query.addCriteria(Criteria.where("_id").is(lunchMenu.getId()));
    update.set("title",lunchMenu.getFoodName());
    update.set("message", lunchMenu.getWriter());


    mongoTemplate.updateMulti(query, update, "user");

    }

//메모 하나 수정하기.
public void mongoMemoUpdate(Memo memo) {
	Query query = new Query();
   Update update = new Update();

   // where절 조건
   query.addCriteria(Criteria.where("_id").is(memo.getId()));
   update.set("title",memo.getTitle());
   update.set("message", memo.getMessage());
    update.set("imageFileName", memo.getImageFileName());


   mongoTemplate.updateMulti(query, update, "memo");

   }

// 삭제
public void deleteDb(String key, String value) {
	Criteria criteria = new Criteria(key);
	criteria.is(value);
	
	Query query = new Query(criteria);
	mongoTemplate.remove(query, "memo");
}



}

