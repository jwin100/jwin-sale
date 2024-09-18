package com.mammon.goods.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class SpuSpecTupleModel {

    private SpuSpecTupleItemModel specKey;

    private List<SpuSpecTupleItemModel> specValues;
}
