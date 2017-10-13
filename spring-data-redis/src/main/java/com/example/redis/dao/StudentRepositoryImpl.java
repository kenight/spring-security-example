package com.example.redis.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.redis.domain.Student;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

	/**
	 * 通过 Redis Hash 模拟数据库表存储 Student 对象, 存储结构:表名-主键-对象
	 */

	private static final String KEY = "Student";

	@PostConstruct
	private void init() {
		this.hashOps = redisTemplate.opsForHash();
	}

	public void save(Student student) {
		this.hashOps.put(KEY, student.getId(), student);
	}

	public void update(Student student) {
		this.hashOps.put(KEY, student.getId(), student);
	}

	public void delete(String id) {
		this.hashOps.delete(KEY, id);
	}

	public Student find(String id) {
		return this.hashOps.get(KEY, id);
	}

	public List<Student> findAll() {
		Map<String, Student> map = this.hashOps.entries(KEY);
		List<Student> list = new ArrayList<Student>();
		for (String id : map.keySet()) {
			list.add(map.get(id));
		}
		return list;
	}

	private HashOperations<String, String, Student> hashOps;
	@Autowired
	private RedisTemplate<String, Student> redisTemplate;

}
