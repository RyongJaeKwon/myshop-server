package com.kwon.myshop.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QItemImage is a Querydsl query type for ItemImage
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QItemImage extends BeanPath<ItemImage> {

    private static final long serialVersionUID = -706104396L;

    public static final QItemImage itemImage = new QItemImage("itemImage");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Integer> ord = createNumber("ord", Integer.class);

    public QItemImage(String variable) {
        super(ItemImage.class, forVariable(variable));
    }

    public QItemImage(Path<? extends ItemImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemImage(PathMetadata metadata) {
        super(ItemImage.class, metadata);
    }

}

