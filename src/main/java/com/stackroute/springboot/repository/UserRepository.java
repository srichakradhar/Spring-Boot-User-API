package com.stackroute.springboot.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.stackroute.springboot.model.User;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
	
	/*
	 * CRUD methods based on id property
	 * findById(String id);
	 */
	
	//Query Methods

	//SQL
	//"select username, password from user_details where username ='abcd' and password = 'abcd'"
	@Query("{'username': {$in : [?0]},'password': {$in : [?1]}}")
	public User validateUser(String username, String password);
	//public List<Course> userCourses(String id);
	
	  public User findByusername(); 
	  public User findByname();
	  public User findByid();
	  @DeleteQuery (value="{'id' : $0}")
	  public boolean deleteByid (String id);
	 
}
