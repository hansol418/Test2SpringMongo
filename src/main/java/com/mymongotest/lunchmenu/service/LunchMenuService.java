package com.mymongotest.lunchmenu.service;

import com.mymongotest.lunchmenu.document.LunchMenu;
import com.mymongotest.springmongtest3.DTO.SearchDB;
import com.mymongotest.springmongtest3.document.Memo;
import com.mymongotest.springmongtest3.document.User2;
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
public class LunchMenuService implements UserDetailsService {

    private final MongoTemplate mongoTemplate;
    private final GridFsTemplate gridFsTemplate;

    // LunchMenu 문서 삽입
    public void insertLunchMenu(LunchMenu lunchMenu) {
        mongoTemplate.insert(lunchMenu);
    }

    // LunchMenu 문서 전체 검색
    public List<LunchMenu> findAllLunchMenus() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        return mongoTemplate.find(query, LunchMenu.class);
    }

    // LunchMenu 문서 하나 검색
    public LunchMenu findLunchMenuById(Long id) {
        return mongoTemplate.findById(id, LunchMenu.class);
    }

    // LunchMenu 문서 수정
    public void updateLunchMenu(LunchMenu lunchMenu) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("_id").is(lunchMenu.getId()));
        update.set("foodName", lunchMenu.getFoodName());
        update.set("writer", lunchMenu.getWriter());
        mongoTemplate.updateFirst(query, update, LunchMenu.class);
    }

    // LunchMenu 문서 삭제
    public void deleteLunchMenuById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, LunchMenu.class);
    }

    // 날짜 변환 메서드
    public String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public Date stringToDate(String dateString) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateString);
    }

    // Memo 문서 삽입
    public void mongoMemoInsert(Memo memo) {
        Date date = new Date();
        String converDate = dateToString(date);
        memo.setDateField(converDate);
        mongoTemplate.insert(memo);
    }

    // User2 문서 삽입
    public void mongoUser2Insert(User2 user) {
        mongoTemplate.insert(user);
    }

    // Memo 문서 전체 검색
    public List<Memo> mongoFindAllMemo() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        return mongoTemplate.find(query, Memo.class);
    }

    // 조건 검색
    public List<Memo> mongoSearchFindAll(SearchDB searchDB) {
        List<Memo> memoList = null;
        if(searchDB.getSearchDB().equals("_id")) {
            Criteria criteria = new Criteria("_id");
            criteria.is(Long.parseLong(searchDB.getSearchContent()));
            Query query = new Query(criteria);
            memoList = mongoTemplate.find(query, Memo.class);
        } else if(searchDB.getSearchDB().equals("title")) {
            Query searchQuery = new Query();
            searchQuery.addCriteria(Criteria.where("title").regex(searchDB.getSearchContent()));
            memoList = mongoTemplate.find(searchQuery, Memo.class);
        } else if(searchDB.getSearchDB().equals("message")) {
            Query searchQuery = new Query();
            searchQuery.addCriteria(Criteria.where("message").regex(searchDB.getSearchContent()));
            memoList = mongoTemplate.find(searchQuery, Memo.class);
        }
        return memoList;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
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

    // User2 문서 하나 검색
    public User2 mongoFindOneUser2Email(String email) {
        Criteria criteria = new Criteria("email");
        criteria.is(email);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, User2.class);
    }

    // Memo 문서 하나 검색
    public Memo mongoFindOneMemo(ObjectId id) {
        return mongoTemplate.findById(id, Memo.class);
    }

    // Memo 문서 수정
    public void mongoMemoUpdate(Memo memo) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("_id").is(memo.getId()));
        update.set("title", memo.getTitle());
        update.set("message", memo.getMessage());
        update.set("imageFileName", memo.getImageFileName());
        mongoTemplate.updateFirst(query, update, Memo.class);
    }

    // 문서 삭제
    public void deleteDb(String key, String value) {
        Criteria criteria = new Criteria(key);
        criteria.is(value);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, Memo.class);
    }
}
