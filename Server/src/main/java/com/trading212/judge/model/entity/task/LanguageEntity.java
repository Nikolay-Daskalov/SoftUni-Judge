package com.trading212.judge.model.entity.task;

import com.trading212.judge.model.entity.task.enums.LanguageEnum;
import com.trading212.judge.model.entity.base.BaseEntity;

import java.time.Instant;
import java.util.Objects;

public class LanguageEntity extends BaseEntity {

    public static final String TABLE_NAME = "languages";

    private final LanguageEnum languageEnum;

    public LanguageEntity(Integer id, Instant createdAt, LanguageEnum languageEnum) {
        super(id, createdAt);
        this.languageEnum = languageEnum;
    }

    public LanguageEnum getLanguageEnum() {
        return languageEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageEntity that = (LanguageEntity) o;
        return id.equals(that.id) && languageEnum == that.languageEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, languageEnum);
    }
}
