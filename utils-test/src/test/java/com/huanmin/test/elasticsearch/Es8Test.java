package com.huanmin.test.elasticsearch;



import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.LongTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.utils.server.WebApplication;
import com.huanmin.test.entity.UserEsEneity;
import lombok.extern.slf4j.Slf4j;
import org.huanmin.es8.utli.Es8Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huanmin
 * @date 2024/1/5
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebApplication.class})
@Slf4j
public class Es8Test {
    @Autowired
    private Es8Client es8Client;

    @Test
    public void createIndexSettingsMappings() throws Exception {
        es8Client.createIndexSettingsMappings(UserEsEneity.class);

    }

    @Test
    public void aliases() throws Exception {
        System.out.println(es8Client.aliases());
    }

    @Test
    public void indexs() throws Exception {
        System.out.println(es8Client.indexs());
    }

    @Test
    public void selectDocIdExists() throws Exception {
        if (es8Client.docIdexists(UserEsEneity.class, "1")) {
            System.out.println("product exists");
        }

    }

    @Test
    public void queryAll() throws Exception {
        List<UserEsEneity> querys = es8Client.queryAll(UserEsEneity.class);
        System.out.println(querys);

    }

    @Test
    public void addData() throws IOException {
        UserEsEneity userEsEneity = new UserEsEneity();
        userEsEneity.setId(22L);
        userEsEneity.setName("xxxxa");
        userEsEneity.setAge(22);
        userEsEneity.setDec("xxxxxxxx");
        userEsEneity.setPrice(22.1);
        userEsEneity.setSku("aaa1");
        String s = es8Client.addData(userEsEneity, true);
        System.out.println(s);
    }

    @Test
    public void addDatas() throws IOException {
        UserEsEneity userEsEneity = new UserEsEneity();
        userEsEneity.setId(23L);
        userEsEneity.setName("hu");
        userEsEneity.setAge(22);
        userEsEneity.setDec("游泳");
        userEsEneity.setPrice(22.1);
        userEsEneity.setSku("aaa1");

        UserEsEneity userEsEneity1 = new UserEsEneity();
        userEsEneity1.setId(24L);
        userEsEneity1.setName("an");
        userEsEneity1.setAge(22);
        userEsEneity1.setDec("");
        userEsEneity1.setPrice(22.2);
        userEsEneity1.setSku("vvvvvv");

        List<UserEsEneity> list = new ArrayList<>();
        list.add(userEsEneity);
        list.add(userEsEneity1);
        es8Client.addDatas(list, true);
    }

    @Test
    public void getDocId() throws IOException {
        UserEsEneity docId = es8Client.getDocId("24", UserEsEneity.class);
        System.out.println(docId);
    }

    @Test
    public void complexQuery_MatchAll() throws IOException {
        Query query = Query.of(q -> q.matchAll(m -> m));
        List<UserEsEneity> userEsEneities = es8Client.complexQuery(query, UserEsEneity.class);
        System.out.println(userEsEneities);
    }

    @Test
    public void complexQuery_MatchAll_Alals() throws IOException {
        Query query = Query.of(q -> q.matchAll(m -> m));
        List<UserEsEneity> userEsEneities = es8Client.complexQuery(query, UserEsEneity.class);
        System.out.println(userEsEneities);
    }

    @Test
    public void complexQuery_MatchQuery() throws IOException {
        Query query = Query.of(q -> q.match(m -> m.field("name").query("xxxxa")));
        List<UserEsEneity> userEsEneities = es8Client.complexQuery(query, UserEsEneity.class);
        System.out.println(userEsEneities);
    }

    @Test
    public void complexQuery_query_bool_must() throws IOException {
        Query age = Query.of(q -> q.match(m -> m.field("age").query(22)));
        Query price = Query.of(q -> q.match(m -> m.field("price").query(22.1)));

        Query bool = Query.of(q -> q.bool(b -> b.must(age).must(price)));
        List<UserEsEneity> userEsEneities = es8Client.complexQuery(bool, UserEsEneity.class);
        System.out.println(userEsEneities);
    }

    @Test
    public void complexQueryHighlight() throws IOException {
        Query dec = Query.of(q -> q.matchPhrase(m -> m.field("dec").query("匹配")));
        List<Map<String, Object>> maps = es8Client.complexQueryHighlight(dec, UserEsEneity.class, "dec");

        System.out.println(maps);
    }

    @Test
    public void complexQuery_query_complexQueryAggregations() throws IOException {
        Query query = Query.of(q -> q.matchAll(m -> m));

        Aggregate age = es8Client.complexQueryAggregations(query, a ->
                        a.terms(t -> t.field("age"))
                , UserEsEneity.class);

        for (LongTermsBucket longTermsBucket : age.lterms().buckets().array()) {

            System.out.println("key:" + longTermsBucket.key() + ":共多少:" + longTermsBucket.docCount());
        }

        Aggregate name = es8Client.complexQueryAggregations(query, a ->
                        a.terms(t -> t.field("name"))
                , UserEsEneity.class);

        for (StringTermsBucket stringTermsBucket : name.sterms().buckets().array()) {
            System.out.println("key:" + stringTermsBucket.key() + ":共多少:" + stringTermsBucket.docCount());
        }


        Aggregate price = es8Client.complexQueryAggregations(query, a ->
                        a.avg(t -> t.field("price"))
                , UserEsEneity.class);

        System.out.println(price.avg().value());
    }

    @Test
    public void delDocId() throws IOException {
        es8Client.delDocId("23", UserEsEneity.class);
    }

    @Test
    public void delQuery() throws IOException {
        Query price = Query.of(q -> q.match(m -> m.field("price").query("0.0")));
        es8Client.delQuery(price, UserEsEneity.class);
    }

    @Test
    public void upDocId() throws Exception {
        UserEsEneity userEsEneity1 = new UserEsEneity();
//        userEsEneity1.setId(24L);
//        userEsEneity1.setName("an");
        userEsEneity1.setAge(21);
        userEsEneity1.setDec("嘻嘻嘻嘻嘻嘻擦擦擦");
        userEsEneity1.setPrice(28.2);
        userEsEneity1.setSku("mmmmm");
        es8Client.upDocId("241", userEsEneity1, true);
    }

    @Test
    public void upQuery() throws Exception {

        UserEsEneity userEsEneity1 = new UserEsEneity();
//        userEsEneity1.setId(24L);
//        userEsEneity1.setName("an");
//        userEsEneity1.setAge(21);
        userEsEneity1.setDec("嘻嘻嘻嘻嘻嘻擦擦擦");
//        userEsEneity1.setPrice(28.2);
//        userEsEneity1.setSku("mmmmm");

        Query name = Query.of(q -> q.matchPhrase(m -> m.field("age").query("33")));
        es8Client.upQuery(name, userEsEneity1, true);
    }
}
