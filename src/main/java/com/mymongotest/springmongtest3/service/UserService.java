package com.mymongotest.springmongtest3.service;


import com.mymongotest.springmongtest3.DTO.SearchDB;
import com.mymongotest.springmongtest3.document.Memo;
import com.mymongotest.springmongtest3.document.User2;
import com.mymongotest.springmongtest3.document.Users;
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
public class UserService implements UserDetailsService{

    private final MongoTemplate mongoTemplate;
    
    private final GridFsTemplate gridFsTemplate;

// 샘플 하나 추가. 
    public void mongoInsert() {
        Users user1 = new Users(3L, "제목2", "메세지2");
        mongoTemplate.insert(user1);
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
    public void mongoUserInsert(Users user) {
        mongoTemplate.insert(user);
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
    
    public void mongoUser2Insert(User2 user) {
        mongoTemplate.insert(user);
    }
    
    
//전체 검색
    public List<Users> mongoFindAll() {
    	Query query = new Query();
    	query.with(Sort.by(Sort.Direction.DESC, "id"));

     	List<Users> userList=mongoTemplate.find(query,Users.class);
		return userList;
        
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
        User2 user2 = mongoFindOneUser2Email(email);

        if(user2 == null){
            throw new UsernameNotFoundException(email);
        }
        

        return User.builder()
                .username(user2.getEmail())
                .password(user2.getPassword())
                .roles(user2.getRole())
                .build();
    }
    
  //유저 찾기
    public User2 mongoFindOneUser2Email(String email) {
    	Criteria criteria = new Criteria("email");
		criteria.is(email);
		
		//기존 1:1 검색
		Query query = new Query(criteria);
		User2 user2 = mongoTemplate.findOne(query, User2.class);
		return user2;
    }
    
//하나 찾기
    public Users mongoFindOne(Long id) {
		Users user = mongoTemplate.findById(id, Users.class);
		return user;
    }

    public Memo mongoFindOneMemo(ObjectId id) {
        Memo memo = mongoTemplate.findById(id, Memo.class);
        return memo;
    }
 //하나 수정하기.
public void mongoUserUpdate(Users user) {
	Query query = new Query();
    Update update = new Update();

    // where절 조건
    query.addCriteria(Criteria.where("_id").is(user.getId()));
    update.set("title",user.getTitle());
    update.set("message", user.getMessage());


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

