package com.stream;

import com.example.App;
import com.example.entity.TUser;
import com.example.enums.WeekDayEnum;
import com.example.mapper.TUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = App.class)
class MybatisEnumTest {

	@Autowired
	private TUserMapper userMapper;

	@Test
	public void testInsert() {
		TUser userDO = new TUser();
		userDO.setName("MyBatis枚举测试");
		userDO.setAge(18);
		userDO.setRestDay(WeekDayEnum.FRIDAY);

		userMapper.insert(userDO);
	}

	@Test
	public void testSelect() {
		TUser userDO = userMapper.selectByPrimaryKey(1L);
		System.out.println(userDO);
	}

}