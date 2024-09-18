package com.mammon;

import com.mammon.leaf.enums.DocketType;
import com.mammon.leaf.service.LeafCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest(classes = {MammonMerchantApplication.class})
class MammonMerchantApplicationTests {

    @Resource
    private LeafCodeService leafCodeService;

    @Test
    void contextLoads() {

        int max = 100;
        int i = 0;
        Set<String> set = new HashSet<>();
        while (i < max) {
            String orderNo = leafCodeService.generateDocketNo(DocketType.CASHIER_ORDER);
            set.add(orderNo);
            i++;
            System.out.println(orderNo);
        }
        System.out.println(set.size());
    }

}
