package com.simple.repository;

import com.simple.repository.connect.RedisSession;
import com.simple.repository.connect.SimpleDataSource;
import com.simple.repository.master.Entity;
import com.simple.repository.master.search.BaseSearch;
import com.simple.repository.master.search.Condition;
import com.simple.repository.search.TestSearch;
import com.simple.repository.test.TestRepository;
import com.simple.repository.test.entity.TestEntity;
import com.simple.repository.test.index.TestIndex;
import com.simple.repository.util.SimpleCollectionUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Sql操作测试类
 *
 * @author laiqx
 * @date 2023-05-24
 */
public class SimpleSqlTest {

    private final static Logger log = LoggerFactory.getLogger(SimpleDataSource.class);

    //
    @Test
    public void testIndexQuery() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 14);
        List<TestEntity> entities = new TestRepository().select(TestIndex.TEST_SELECT, map, TestEntity.class);
    }

    @Test
    public void add() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        TestEntity entity = new TestEntity();
        entity.name = "张三";
        entity.age = 18;
        new TestRepository().add(entity);
    }

    @Test
    public void addList() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        TestEntity entity = new TestEntity();
        entity.name = "张三";
        entity.age = 18;
        TestEntity entity2 = new TestEntity();
        entity2.name = "李四";
        entity2.age = 18;
        new TestRepository().add(Arrays.asList(entity, entity2));
    }

    @Test
    public void update() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        TestEntity entity = new TestEntity();
        entity.name = "张三";
        entity.age = 18;
        Condition condition = new Condition().eq(TestEntity.ID, 2);
        new TestRepository().update(entity, condition);
    }


    @Test
    public void delete() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        new TestRepository().delete(1);
    }

    @Test
    public void deleteList() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        new TestRepository().delete(Arrays.asList(1, 2));
    }

    @Test
    public void deleteCondition() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        new TestRepository().delete(new Condition().eq(TestEntity.ID, 1));
    }


    @Test
    public void get() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        TestEntity entity = new TestRepository().get(1);
        entity = new TestRepository().get(new Condition().eq(TestEntity.ID, 1));

        List<TestEntity> entities = new TestRepository().list(Arrays.asList(1, 2, 3));

        entities = new TestRepository().list(new BaseSearch());

        entities = new TestRepository().list(new Condition().in(TestEntity.ID, Arrays.asList(1, 2, 3)));

        BaseSearch baseSearch = new BaseSearch();
        baseSearch.setPageNum(1);
        baseSearch.setLoadSize(10);
        baseSearch.setCondition(new Condition().like(TestEntity.NAME, "张"));
        baseSearch.putSort(TestEntity.ID, BaseSearch.SortType.asc);
        entities = new TestRepository().list(baseSearch);
    }

    @Test
    public void count() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        Integer size = new TestRepository().count(new Condition().like(TestEntity.NAME, "张"));
    }

    @Test
    public void list() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        List<TestEntity> entities = new TestRepository().list(Arrays.asList(1, 2));
    }

    public void listPage() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        BaseSearch baseSearch = new BaseSearch();
        baseSearch.setPageNum(1);
        baseSearch.setLoadSize(10);
        List<TestEntity> entities = new TestRepository().list(baseSearch);
    }

    @Test
    public void selectSql() throws IOException {
        SimpleStart.run(new String[]{"--simple.env=dev"});
        TestSearch testSearch = new TestSearch();
        testSearch.name = "张";
        List<TestEntity> entities = new TestRepository().select(TestIndex.TEST_SELECT, testSearch, TestEntity.class);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "张%");
        new TestRepository().select(TestIndex.TEST_SELECT, map, TestEntity.class);
    }

    @Test
    public void dmlSql() throws IOException {
        TestEntity entity = new TestEntity();
        entity.name = "张";
        new TestRepository().execute(TestIndex.UPDATE_TEST, entity);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "张");
        new TestRepository().execute(TestIndex.UPDATE_TEST, map);
    }

    @Test
    public void getCache() throws IOException {
        TestEntity entity = new TestRepository().getCache(1);

        Map<Integer, TestEntity> entityMap = new TestRepository().getCache(Arrays.asList(1, 2, 3));
    }

    @Test
    public void delCache() throws IOException {
        new TestRepository().delCache(1);

        new TestRepository().delAllCache();
        RedisSession.set("simple:test", "hello world", 1000L);
        RedisSession.open();


        TestEntity entity = new TestEntity();
        entity.name = "张三";
        entity.age = 18;
        TestEntity entity2 = new TestEntity();
        entity2.name = "李四";
        entity2.age = 18;
        List<TestEntity> entities = new ArrayList<>();
        entities.add(entity);
        entities.add(entity2);
        Map<String, TestEntity> entityMap = SimpleCollectionUtil.listToMap(entities, TestEntity.NAME, String.class);

        Map<Integer, String> paramMap = SimpleCollectionUtil.listToParamMap(entities, TestEntity.AGE, Integer.class, TestEntity.NAME, String.class);

        List<String> names = SimpleCollectionUtil.listParam(entities, TestEntity.NAME, String.class);

        Set<String> nameSet = SimpleCollectionUtil.listParamToSet(entities, TestEntity.NAME, String.class);


        List<Map<String, Object>> ListObjMap = new ArrayList();
        Map<String, Object> objMap = new HashMap();
        objMap.put("age", 10);
        objMap.put("name", "张三");
        ListObjMap.add(objMap);
        objMap = new HashMap();
        objMap.put("age", 12);
        objMap.put("name", "李四");
        ListObjMap.add(objMap);
        List<TestEntity> testEntities = SimpleCollectionUtil.listMapToObject(ListObjMap, TestEntity.class);



    }

}
