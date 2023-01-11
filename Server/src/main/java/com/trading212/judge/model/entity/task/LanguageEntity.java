package com.trading212.judge.model.entity.task;

import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;
import com.trading212.judge.model.entity.base.BaseEntity;

import java.time.Instant;
import java.util.Objects;

public class LanguageEntity extends BaseEntity {

    public static final String TABLE_NAME = "languages";

    private final CodeLanguageEnum codeLanguageEnum;

    public LanguageEntity(Integer id, Instant createdAt, CodeLanguageEnum codeLanguageEnum) {
        super(id, createdAt);
        this.codeLanguageEnum = codeLanguageEnum;
    }

    public CodeLanguageEnum getLanguageEnum() {
        return codeLanguageEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageEntity that = (LanguageEntity) o;
        return codeLanguageEnum == that.codeLanguageEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeLanguageEnum);
    }
}
