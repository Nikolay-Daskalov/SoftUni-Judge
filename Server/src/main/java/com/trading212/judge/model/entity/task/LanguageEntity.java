package com.trading212.judge.model.entity.task;

import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;
import com.trading212.judge.model.entity.base.BaseEntity;

import java.time.Instant;
import java.util.Objects;

public class LanguageEntity extends BaseEntity {

    public static final String TABLE_NAME = "code_languages";

    private final CodeLanguageEnum name;

    public LanguageEntity(Integer id, Instant createdAt, CodeLanguageEnum name) {
        super(id, createdAt);
        this.name = name;
    }

    public CodeLanguageEnum getLanguageEnum() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageEntity that = (LanguageEntity) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
